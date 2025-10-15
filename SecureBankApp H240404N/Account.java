import java.io.Serializable;

public class Account implements Serializable {
    private String accountId;
    private String ownerUsername;
    private double balance;

    public Account(String accountId, String ownerUsername, double balance) {
        this.accountId = accountId;
        this.ownerUsername = ownerUsername;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance = balance - amount;
            return true;
        }
        return false;
    }
}
