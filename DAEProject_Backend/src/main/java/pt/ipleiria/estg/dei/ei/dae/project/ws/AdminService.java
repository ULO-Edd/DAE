package pt.ipleiria.estg.dei.ei.dae.project.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.AdminUpdateUserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.HistoryUserBean;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;
import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.security.Authenticated;

import java.util.List;

@Path("admin")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Administrator"})
public class AdminService {

    @Context
    private SecurityContext securityContext;

    @EJB 
    private HistoryUserBean historyUserBean;

    @EJB
    private UserBean userBean;

    @GET
    @Path("/users")
    public Response getUsers() {
        List<User> users = userBean.findAll();
        List<UserDTO> userDTOs = UserDTO.from(users);
        return Response.ok(userDTOs).build();
    }

    @GET
    @Path("/users/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = userBean.find(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"User not found\"}").build();
        }
        UserDTO userDTO = UserDTO.from(user);
        return Response.ok(userDTO).build();
    }

    @POST
    @Path("/users")
    public Response createUser(UserCreateDTO dto) {
        try {
            userBean.create(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                    Role.valueOf(dto.getRole())
            );

            User newUser = userBean.find(dto.getEmail());
            historyUserBean.logAction(newUser.getId(), -1, ActionType.CREATE_USER);

            return Response.status(Response.Status.CREATED).entity(UserDTO.from(newUser)).build();

        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/users/{id}")
    public Response updateUserByAdmin(@PathParam("id") long id, AdminUpdateUserDTO dto) {
        try {
            java.security.Principal principal = securityContext.getUserPrincipal();
            long myId = Long.parseLong(principal.getName());

            if (id == myId) {
                if (dto.getRole() != null) {
                     return Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"message\": \"You cannot change your own role.\"}").build();
                }
                if (dto.getIsActive() != null && !dto.getIsActive()) { 
                     return Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"message\": \"You cannot deactivate yourself.\"}").build();
                }
            }
            userBean.adminUpdate(id, dto);
            User user = userBean.find(id);
            historyUserBean.logAction(myId, -1, ActionType.CHANGE_ROLE);

            return Response.ok(UserDTO.from(user)).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/users/{id}")
    public Response deleteUser(@PathParam("id") long id) {
        try {
            userBean.delete(id);
            historyUserBean.logAction(id, -1, ActionType.DELETE_USER);
            return Response.ok("{\"message\": \"User Deleted Successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
