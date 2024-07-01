package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO  = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    

    public Account createAccount(Account account) {
        if (account.getUsername() == null || 
            account.getUsername().isEmpty() ||
            account.getPassword().length() < 4 ||
            accountDAO.getAccountByUsername(account.getUsername()) != null) {
            
            return null;
        }
        return accountDAO.createUser(account);
    }
    public Account loginAccount(String userName, String password){
        if(userName == null || userName.isEmpty() || password == null || password.isEmpty()){
            return null;
        }
        return accountDAO.loginUser(userName, password);
    }
}
