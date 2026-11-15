package pt.ipleiria.estg.dei.ei.dae.project.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.var;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.CreateTagDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.TagDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.TagBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean; 
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;
import pt.ipleiria.estg.dei.ei.dae.project.security.Authenticated;

import java.security.Principal; 
import java.util.List;
import java.util.stream.Collectors;

@Path("tags")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class TagService {

    @EJB
    private TagBean tagBean;

    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;


    @GET
    @Path("/")
    public List<TagDTO> getAllTags() {
        return TagDTO.from(tagBean.findAll());
    }

    @POST
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"Administrator", "Moderator"})
    public Response createTag(CreateTagDTO tagDTO) {
        tagBean.create(tagDTO.getName());
        var createdTag = tagBean.find(tagDTO.getName());
        var createdTagDTO = TagDTO.from(createdTag);
        return Response.status(Response.Status.CREATED).entity(createdTagDTO).build();
    }

    @PATCH
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"Administrator", "Moderator"})
    public Response updateTag(@PathParam("id") long id, CreateTagDTO tagDTO) {
        tagBean.update(id, tagDTO.getName());
        var updatedTag = tagBean.find(id);
        var updatedTagDTO = TagDTO.from(updatedTag);
        return Response.ok(updatedTagDTO).build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"Administrator", "Moderator"})
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") long id) {
        tagBean.delete(id);
        return Response.ok("{\"message\":\"Tag Deleted Successfully\"}").build();
    }

    // URL: /tags/me
    @GET
    @Path("/me")
    public Response getMySubscriptions() {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());

            List<Tag> subscriptions = userBean.getSubscriptions(userId);
            
            return Response.ok(TagDTO.from(subscriptions)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // URL: /tags/{id}/subscribe
    @POST
    @Path("/{id}/subscribe")
    public Response subscribeTag(@PathParam("id") long tagId) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());

            userBean.subscribe(userId, tagId);
            
            return Response.ok("{\"message\": \"Subscribed successfully\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/unsubscribe")
    public Response unsubscribeTag(@PathParam("id") long tagId) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());

            userBean.unsubscribe(userId, tagId);
            
            return Response.ok("{\"message\": \"Unsubscribed successfully\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
