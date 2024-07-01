package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

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
        app.post("/messages",this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{messageId}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagebyAccountIdHandler);
        app.delete("/messages/{messageId}", this::deleteMessageByIdHandler);
        app.patch("/messages/{messageId}", this::updateMessageByIdHandler);


        return app;
    }
    
    /**
     * This is an example handler for an example endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedUser = accountService.createAccount(account);
        if (addedUser != null) {
            ctx.status(200).json(mapper.writeValueAsString(addedUser));
        } else {
            ctx.status(400).result("");
        }
    }

    private void loginUserHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginUser = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (loginUser != null) {
            ctx.status(200).json(loginUser);
        } else {
            ctx.status(401).result("");
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if(addedMessage!= null){
            ctx.status(200).json(addedMessage);
        }else{
            ctx.status(400).result("");
        }
    }

    private void getMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));
        Message message = messageService.getMessageById(messageId);
        if(message!=null){
            ctx.status(200).json(message);
        }else{
            ctx.status(200).result("");
        }
    }
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("messageId"));
        Message message = messageService.delectMessageById(message_id);
        if(message!=null){
            ctx.status(200).json(message);
        }else{
            ctx.status(200).result("");
        }
    }
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message  message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("messageId"));
        Message updatedMessage = messageService.updateMessageById(message_id, message.getMessage_text());
        if(updatedMessage!= null){
            ctx.status(200).json(updatedMessage);
        }else{
            ctx.status(400).result("");
        }

    }
    private void getMessagebyAccountIdHandler(Context ctx) throws JsonProcessingException{
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List <Message> messages = messageService.getMessagesByAccountId(account_id);
        ctx.status(200).json(messages);
    }
}
