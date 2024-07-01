package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;
import kotlin.time.MeasureTimeKt;

public class MessageDAO {
    public Message createMessage(Message message){
        String sql= "insert into message (  posted_by, message_text, time_posted_epoch) values (?,?,?)";
        
        try( Connection connection = ConnectionUtil.getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());

                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()){
                    int generatedMessageId = rs.getInt(1);
                    return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }else{
                    throw new SQLException("Message creation failed");
                }

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        String sql = "Select * from message";
        try(
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return messages;
       }

    public Message getMessageById(int messageId){
        String sql = "Select * from message where message_id =?";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }

         }catch(SQLException e){
            System.out.print(e.getMessage());
         }
        return null;
    }

    public boolean deleteMessageById(int messageId){
        String sql = "Delete from message where message_id = ?";
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, messageId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected>0;
            
         }catch(SQLException e){
            System.out.print(e.getMessage());
         }
        return false;
    }

    public Message updateMessageById(int messageId, String newMessage){
        String sql = "Update message SET message_text = ? where message_id=?";
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                preparedStatement.setString(1, newMessage);
                preparedStatement.setInt(2, messageId);

                int rowsAffected= preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    return getMessageById(messageId);
                }

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    public List<Message> getMessagesByAccountId(int accountId){  
        List<Message> messages = new ArrayList<>();
        String sql = "Select * from message where posted_by = ?";
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){

                preparedStatement.setInt(1, accountId);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Message message = new Message(
                            rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")
                    );
                    messages.add(message);
                }
            }catch(SQLException e){
                System.out.print(e.getMessage());
            }
            return messages;
           }
}