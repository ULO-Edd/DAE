package pt.ipleiria.estg.dei.ei.dae.project.ws;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.var;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.RecoverPasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.TokenDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.LoginDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.project.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.security.TokenIssuer;
import pt.ipleiria.estg.dei.ei.dae.project.utils.RandomString;

import java.util.Random;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {

    @EJB
    private UserBean userBean;
    @EJB
    private EmailBean emailBean;

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getEmail(), auth.getPassword())) {
            var userLoggedIn = LoginDTO.from(userBean.find(auth.getEmail()));
            var token = TokenIssuer.issue(userLoggedIn.getId());

            var tokenDTO = TokenDTO.from(token);
            tokenDTO.setUser(userLoggedIn);

            return Response.ok(tokenDTO).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    //This method will not be implemented safely because to do so meant we'd have less time to focus on the main functionalities.
    //Ideally, we would generate a secure token, store it, and send an email to the user with a link to reset their password.
    // For now, we will just check if the email exists, generate a "random" password and force the user to change it.
    @POST
    @Path("/recover-password")
    public Response recoverPassword(@Valid RecoverPasswordDTO payload) {
        var user = userBean.find(payload.getEmail());
        if (user != null) {

            String pw = RandomString.genString(8);

            userBean.recoverPassword(user, pw);

            emailBean.send(user.getEmail(),
                    "Password Recovery",
                    "Your temporary password is: "+ pw + " Please change it after logging in.");
            return Response.ok("{\"message\":\"Recovery Email Sent\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/logout")
    public Response logout() {
        // In a stateless JWT authentication, logout can be handled on the client side
        //If we had a server-side session, we would invalidate it here.
        //By removing the refresh token from the database or cache.
        return Response.ok().build();
    }
}
