package pt.ipleiria.estg.dei.ei.dae.project.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.CommentPayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.ToggleHidePayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.CommentBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.HistoryUserBean;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.security.Authenticated;

import java.security.Principal;


@Path("comments")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class CommentService {

    @EJB
    private CommentBean commentBean;
    @EJB
    private UserBean userBean;
    @EJB 
    private HistoryUserBean historyUserBean;

    @Context
    private SecurityContext securityContext;


    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response editComment(@PathParam("id") long id, CommentPayloadDTO dto) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());
            User user = userBean.find(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            if (commentBean.find(id).getAuthor().getId() != userId) {
                return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You can only edit your own comments\"}").build();
            }

            commentBean.update(id, dto);
            historyUserBean.logAction(userId, commentBean.find(id).getPublication().getId() , ActionType.EDIT_COMMENT); 
            return Response.ok("{\"message\": \"Comment updated successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response toggleHideComment(@PathParam("id") long id, ToggleHidePayloadDTO payload) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            long userId = Long.parseLong(principal.getName());
            User user = userBean.find(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            if (commentBean.find(id).getAuthor().getId() != userId && user.getRole().toString().equals("collaborator")) {
                return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You can only edit your own comments\"}").build();
            }
            
            commentBean.toggleHidden(id, payload);
            historyUserBean.logAction(userId, -1, ActionType.HIDE_COMMENT);
            return Response.ok( "{\"message\": \"Comment updated successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
