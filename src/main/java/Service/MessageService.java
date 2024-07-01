package Service;

import DAO.MessageDAO;
import Model.Message;
import DAO.AccountDAO;
import java.util.List;

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
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || 
            message.getMessage_text().trim().isEmpty() ||
            message.getMessage_text().length() > 255 ||
            accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message delectMessageById(int messageId){
        Message message= messageDAO.getMessageById(messageId);
        if(message!= null || messageDAO.deleteMessageById(messageId)){
            return message;
    }
        return null;
    }

    public Message updateMessageById(int messageId, String newMessage) {
        Message oldMessage = messageDAO.getMessageById(messageId);
        if (oldMessage == null || newMessage == null || newMessage.isEmpty() || newMessage.length() > 255) {
            return null;
        }
        oldMessage.setMessage_text(newMessage);
        return messageDAO.updateMessageById(messageId, newMessage);
    }
    
    public List<Message> getMessagesByAccountId(int accountId){
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
