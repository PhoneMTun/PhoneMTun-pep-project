package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message createMessage(Message message){
        String sql= "insert into message (  posted_by, message_text, time_posted_epoch) values (?,?,?)";
        
        try( Connection connection = ConnectionUtil.getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());

                preparedStatement.executeUpdate();
                try(ResultSet rs = preparedStatement.getGeneratedKeys()){
                    if(rs.next()){
                        int generatedMessageId = rs.getInt(1);
                        return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                    }else{
                        throw new SQLException("Message creation failed");
                    }

                }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    
}
