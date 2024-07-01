package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account createUser(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            try (ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys()) {
                if (pkeyResultSet.next()) {
                    int generatedAccountId = (int) pkeyResultSet.getLong(1);
                    return new Account(generatedAccountId, account.getUsername(), account.getPassword());
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public Account loginUser(String userName, String password){
        String sql = "select * from account where username = ? and password =?";
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                try(ResultSet rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                    }
                }
            }catch(SQLException e){
                System.out.print(e.getMessage());
            }
        return null;
    }

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    public Account getAccountById(int accountId){
        String sql = "Select * from account where account_id = ?";
        try(Connection connection =  ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                preparedStatement.setInt(1, accountId);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return null;
    }
}
