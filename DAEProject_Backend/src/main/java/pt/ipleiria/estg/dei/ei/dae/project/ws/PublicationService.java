package pt.ipleiria.estg.dei.ei.dae.project.ws;

import java.io.File;
import java.io.InputStream;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.CommentBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.PublicationBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.TagBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.HistoryUserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.HistoryPublicationBean;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;
import pt.ipleiria.estg.dei.ei.dae.project.security.Authenticated;

import jakarta.json.bind.JsonbBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("publications")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class  PublicationService {

    @EJB
    private PublicationBean publicationBean;
    @EJB
    private UserBean userBean;
    @EJB
    private TagBean tagBean;
    @EJB
    private CommentBean commentBean;
    @EJB
    private HistoryUserBean historyUserBean;
    @EJB 
    private HistoryPublicationBean historyPublicationBean;

    @Context
    private SecurityContext securityContext;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    @GET
    @Path("/")
    @Authenticated
    public Response getAllPosts(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("sort") @DefaultValue("date") String sort,
            @QueryParam("order") @DefaultValue("desc") String order,
            @QueryParam("search") @DefaultValue("") String search) {

        Principal principal = securityContext.getUserPrincipal();
        long userId = Long.decode(principal.getName());

        User user = userBean.find(userId);

        List<Publication> publications = publicationBean.findAll(page, limit, sort, order, search, user);
        List<PublicationSummaryDTO> dtos = PublicationSummaryDTO.from(publications);

        long total = publicationBean.count();

        return Response.ok(dtos)
                .header("X-Total-Count", total)
                .build();
    }

    @GET
    @Path("/me")
    @Authenticated
    public Response getUserPosts(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("sort") @DefaultValue("date") String sort,
            @QueryParam("order") @DefaultValue("desc") String order) {

        Principal principal = securityContext.getUserPrincipal();
        try {
            long userId = Long.parseLong(principal.getName());

            User user = userBean.find(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            List<Publication> publications = publicationBean.findBySubmitter(userId, page, limit, sort, order);
            List<PublicationSummaryDTO> dtos = PublicationSummaryDTO.from(publications);
            long total = publicationBean.countByUser(user);
            return Response.ok(dtos)
                    .header("X-Total-Count", total)
                    .build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\": \"Invalid User ID in Token\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPublicationById(@PathParam("id") long id) {
        Publication publication = publicationBean.findWithComments(id);
        if (publication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Publication not found\"}").build();
        }

        // Visibility Check
        if (publication.isHidden()) {
            Principal principal = securityContext.getUserPrincipal();
            if (principal == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("You must be logged in to view hidden publications.").build();
            }
            long userId = Long.parseLong(principal.getName());
            User user = userBean.find(userId);

            boolean canView = user != null && (
                user.getRole() == Role.ADMINISTRATOR ||
                user.getRole() == Role.MODERATOR ||
                publication.getSubmitter().getId() == userId
            );

            if (!canView) {
                return Response.status(Response.Status.FORBIDDEN).entity("You do not have permission to view this publication.").build();
            }
        }

        PublicationDetailsDTO dto = PublicationDetailsDTO.from(publication);
        dto.setTags(TagDTO.from(publication.getTags()));
        dto.setComments(CommentDTO.from(publication.getComments()));
        return Response.ok(dto).build();
    }

    @GET 
    @Path("/{id}/history")
    public Response getPublicationHistory(@PathParam("id") long id) {
        try {
            List<HistoryPublicationDTO> historyDTOs = HistoryPublicationDTO.fromList(
                    historyPublicationBean.findByPublication(id)
            );
            return Response.ok(historyDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/")
    @Authenticated
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createNewPublication(MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

            List<InputPart> dataParts = uploadForm.get("data");
            if (dataParts == null || dataParts.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"Missing 'data' part\"}").build();
            }
            String jsonMetadata = dataParts.get(0).getBody(String.class, null);
            
            PublicationCreateDTO dto = JsonbBuilder.create().fromJson(jsonMetadata, PublicationCreateDTO.class);

            Principal principal = securityContext.getUserPrincipal();
            try {
                long userId = Long.parseLong(principal.getName());
                User submitter = userBean.find(userId);


                if (submitter == null) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }

                LinkedList<Tag> tags = new LinkedList<>();
                if (dto.getTags() != null && !dto.getTags().isEmpty()) {
                    for (Long tagId : dto.getTags()) {
                        Tag tag = tagBean.find(tagId);
                        if (tag != null) {
                            tags.add(tag);
                        }
                        else {
                            return Response.status(Response.Status.BAD_REQUEST)
                                    .entity("Tag with id " + tagId + " not found").build();
                        }
                    }
                }

                String filePath = "";
                List<InputPart> fileParts = uploadForm.get("file");
                if (fileParts != null && !fileParts.isEmpty()) {
                    InputPart filePart = fileParts.get(0);
                    
                    String safeTitle = dto.getTitle().replaceAll("[^a-zA-Z0-9]", "_");
                    String fileName = "pub_" + System.currentTimeMillis() + "_" + safeTitle + ".pdf";
                    
                    InputStream inputStream = filePart.getBody(InputStream.class, null);
                    filePath = saveFile(inputStream, fileName);
                }

                LinkedList<String> authorsList = new LinkedList<>();
                if (dto.getAuthors() != null) authorsList.addAll(dto.getAuthors());

                FileType fileType;
                try {
                    if (dto.getType() == null) throw new IllegalArgumentException("Type is missing");
                    fileType = FileType.valueOf(dto.getType());
                } catch (Exception e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Invalid FileType: " + dto.getType()).build();
                }

                publicationBean.create(
                        submitter,
                        dto.getTitle(),
                        authorsList,
                        filePath,
                        fileType,
                        dto.getAbstractText(),
                        tags
                );



                Publication pub = publicationBean.findBySubmitter(userId, 1, 1, "date", "desc").get(0);
                for (Tag t : tags) {
                    historyPublicationBean.logTagAction(userId, pub.getId(), t);
                }
                historyUserBean.logAction(userId, pub.getId(), ActionType.UPLOAD_PUBLICATION);
                historyPublicationBean.logAction(userId, pub.getId(), ActionType.CREATE_PUBLICATION);
                return Response.status(Response.Status.CREATED).entity("{\"message\": \"Publication created successfully\"}").build();
            } catch (NumberFormatException e) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\": \"Invalid User ID in Token\"}").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Authenticated
    @Path("/{id}/vote")
    public Response votePublication(@PathParam("id") long id, VoteDTO voteDTO) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());

            VoteResponseDTO response = publicationBean.vote(id, userId, voteDTO.getVoteType());
            
            historyUserBean.logAction(userId, id ,ActionType.RATE_PUBLICATION);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/download/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("filename") String filename) {
        if (filename == null || filename.isEmpty() || filename.contains("..")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        File file = new File(UPLOAD_DIR + File.separator + filename);
        
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"File not found\"}").build();
        }

        return Response.ok(file)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
    }

    @DELETE
    @Authenticated
    @Path("/{id}")
    public Response deletePublication(@PathParam("id") long id) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());
            User user = userBean.find(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
    
            List<Publication> pub = publicationBean.findBySubmitter(userId, 1, 1, "date", "desc");

            if (pub.isEmpty() || pub.get(0).getId() != id) {
                System.out.println(">>> User is not the submitter of the publication. Checking Role: " + user.getRole().toString());
                if (user.getRole().toString().equals("administrator") || user.getRole().toString().equals("moderator") ) {
                    publicationBean.delete(id);
                    return Response.ok("{\"message\": \"Publication deleted successfully by admin/moderator\"}").build();
                }
                return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You can only delete your own publications\"}").build();
            } 

            publicationBean.delete(id);
            historyUserBean.logAction(userId, id ,ActionType.HIDE_PUBLICATION);
            historyPublicationBean.logAction(userId, id ,ActionType.HIDE_PUBLICATION);
            return Response.ok("{\"message\": \"Publication deleted successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Authenticated
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editPublication(@PathParam("id") long id, MultipartFormDataInput input) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());
            User user = userBean.find(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            Publication publication = publicationBean.find(id);
            if (publication == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Publication not found").build();
            }

            if (publication.getSubmitter().getId() != userId) {
                boolean isAdminOrMod = user.getRole() == Role.ADMINISTRATOR || user.getRole() == Role.MODERATOR;
                if (!isAdminOrMod) {
                    return Response.status(Response.Status.FORBIDDEN).entity("You can only edit your own publications").build();
                }
            }

            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

            List<InputPart> dataParts = uploadForm.get("data");
            if (dataParts != null && !dataParts.isEmpty()) {
                String jsonMetadata = dataParts.get(0).getBody(String.class, null);
                PublicationCreateDTO dto = JsonbBuilder.create().fromJson(jsonMetadata, PublicationCreateDTO.class);

                if (dto.getTitle() != null) publication.setTitle(dto.getTitle());
                if (dto.getAuthors() != null) publication.setAuthors(dto.getAuthors());
                if (dto.getAbstractText() != null) publication.setAbstractText(dto.getAbstractText());

                if (dto.getType() != null) {
                    try {
                        publication.setType(FileType.valueOf(dto.getType()));
                    } catch (IllegalArgumentException e) {
                        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid FileType: " + dto.getType()).build();
                    }
                }

                if (dto.getTags() != null) {
                    LinkedList<Tag> tags = new LinkedList<>();
                    for (Long tagId : dto.getTags()) {
                        Tag tag = tagBean.find(tagId);
                        if (tag != null) tags.add(tag);
                    }
                    publication.setTags(tags);
                }
            }

            List<InputPart> fileParts = uploadForm.get("file");
            if (fileParts != null && !fileParts.isEmpty()) {
                InputPart filePart = fileParts.get(0);
                InputStream inputStream = filePart.getBody(InputStream.class, null);

                String safeTitle = publication.getTitle().replaceAll("[^a-zA-Z0-9]", "_");
                String fileName = "pub_" + System.currentTimeMillis() + "_" + safeTitle + ".pdf";

                String filePath = saveFile(inputStream, fileName);
                if (filePath != null) {
                    publication.setAttachedFile(filePath);
                }
            }

            publicationBean.update(publication);
            historyUserBean.logAction(userId, id ,ActionType.EDIT_PUBLICATION);
            historyPublicationBean.logAction(userId, id ,ActionType.EDIT_PUBLICATION);

            return Response.ok("Publication updated successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
@Path("/{id}/hide")
@Authenticated
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
public Response toggleHidePost(@PathParam("id") long id, ToggleHidePayloadDTO payload) {
    try {
        Principal principal = securityContext.getUserPrincipal();
        long userId = Long.parseLong(principal.getName());
        User user = userBean.find(userId);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Publication publication = publicationBean.find(id);
        if (publication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Publication not found\"}").build();
        }

        if (publication.getSubmitter().getId() != userId) {
            if (user.getRole() == Role.ADMINISTRATOR || user.getRole() == Role.MODERATOR) {
                publicationBean.toggleHidden(id, payload);
                return Response.ok("{\"message\": \"Publication updated successfully\"}").build();
            }
            return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You can only edit your own publications\"}").build();
        }
        
        publicationBean.toggleHidden(id, payload);
        return Response.ok("{\"message\": \"Publication updated successfully\"}").build();
    } catch (Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}


    @GET
    @Authenticated
    @Path("/{id}/comments")
    public Response getComments(@PathParam("id") long id) {
        List<Comment> comments = commentBean.findAllFromPost(id);
        List<CommentDTO> commentDTOs = CommentDTO.from(comments);
        return Response.ok(commentDTOs).build();
    }

    @POST
    @Authenticated
    @Path("/{id}/comments")
    public Response comment(@PathParam("id") long id, CommentPayloadDTO payload) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());

            Comment comment = commentBean.create(userId, id, payload.getContent());
            CommentDTO commentDTO = CommentDTO.from(comment);

            historyUserBean.logAction(userId, id ,ActionType.COMMENT_PUBLICATION);
            return Response.ok("{\"message\": \"Comment added successfully\"}").entity(commentDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    private String saveFile(InputStream uploadedInputStream, String fileName) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String finalPath = UPLOAD_DIR + File.separator + fileName;
        Files.copy(uploadedInputStream, new File(finalPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println(">>> Ficheiro guardado em: " + finalPath);
        return finalPath;
    }
}
