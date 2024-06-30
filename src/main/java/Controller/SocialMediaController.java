package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService  = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */



     
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserHandler);
        app.post("/login", this::loginUserHandler);



        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedUser = accountService.createAccount(account);
        if (addedUser != null) {
            context.status(200).json(mapper.writeValueAsString(addedUser));
        } else {
            context.status(400).result("");
        }
    }
    private void loginUserHandler(Context cxt) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(cxt.body(), Account.class);
        Account loginUser = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (loginUser != null) {
            cxt.status(200).json(loginUser);
        } else {
            cxt.status(401).result("");
        }
    }
}



// - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
// - If the registration is not successful, the response status should be 400. (Client error)