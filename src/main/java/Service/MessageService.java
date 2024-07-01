package Service;

import DAO.MessageDAO;
import Model.Message;
import DAO.AccountDAO;;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();    
        accountDAO = new AccountDAO();
    }
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    // - The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
    public Message createMessage(Message message){
        if(message.getMessage_text().length()>255 ||
            accountDAO.getAccountById(message.getPosted_by())==null){
            
            return null;
        }
        return messageDAO.createMessage(message);
    }

}
