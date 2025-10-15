import java.io.Serializable;

public class Transaction implements Serializable {
    private String transactionId;
    private String accountId;
    private String type; // "DEPOSIT" or "WITHDRAW"
    private double amount;
    private long timestamp;

    public Transaction(String transactionId, String accountId, String type, double amount, long timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
