package pt.ipleiria.estg.dei.ei.dae.project.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.var;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.CommentBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.HistoryUserBean;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import pt.ipleiria.estg.dei.ei.dae.project.entities.HistoryUser;
import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.project.security.Hasher;

import java.security.Principal;
import java.util.List;

@Path("users") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
@Authenticated
public class UserService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;

    @EJB 
    private HistoryUserBean historyUserBean;

    @EJB
    private CommentBean commentBean;

    @GET
    @Path("/me")
    public Response getUser() throws Exception {
        var id = securityContext.getUserPrincipal();

        var user = userBean.find(Long.decode(id.getName()));
        var userDTO = UserDTO.from(user);

        return Response.ok(userDTO).build();
    }

    @PATCH
    @Path("/me")
    public Response updateUser(UserEditDTO userDTO) throws Exception {
        var principal = securityContext.getUserPrincipal();
        long id = Long.decode(principal.getName());

        userBean.update(id, userDTO);

        var updatedUserDTO = UserDTO.from(userBean.find(id));
        if (userDTO.getName() != null)
            historyUserBean.logAction(id, -1, ActionType.CHANGE_NAME);

        if (userDTO.getEmail() != null)
            historyUserBean.logAction(id, -1, ActionType.CHANGE_EMAIL);

        return Response.ok(updatedUserDTO).build();
    }

    @GET
    @Authenticated
    @Path("/me/comments")
    public Response getUserComments() {
        Principal principal = securityContext.getUserPrincipal();
        long id = Long.decode(principal.getName());
        
        List<Comment> comments = commentBean.findAllFromUser(id);
        List<CommentDTO> commentDTOs = CommentDTO.from(comments);
        return Response.ok(commentDTOs).build();
    }

    @GET 
    @Path("/me/history")
    @Authenticated
    public Response getUserHistory() {
        Principal principal = securityContext.getUserPrincipal();
        long id = Long.decode(principal.getName());

        List<HistoryUser> historys = historyUserBean.findByUser(id);
        List<HistoryUserDTO> historyDTOs = HistoryUserDTO.fromList(historys);

        return Response.ok(historyDTOs).build();
    }

    @PUT
    @Path("/me/password")
    @Authenticated
    public Response setPassword(@Valid PasswordDTO pw) {
        String id = securityContext.getUserPrincipal().getName();
        var user = userBean.find(Long.decode(id));

        if (!Hasher.verify(pw.getOldPassword(), user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!pw.getNewPassword().equals(pw.getConfirmNewPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"New password and confirmation do not match\"}").build();
        }

        userBean.setPassword(user, pw.getNewPassword());
        return Response.ok("{\"message\":\"Password Updated Successfully\"}").build();

    }

    @GET
    @Path("/{id}/history")
    @Authenticated
    @RolesAllowed({"Administrator"})
    public Response getUserHistorybyID(@PathParam("id") long id) {
        List<HistoryUser> historys = historyUserBean.findByUser(id);
        List<HistoryUserDTO> historyDTOs = HistoryUserDTO.fromList(historys);

        return Response.ok(historyDTOs).build();
    }

}
