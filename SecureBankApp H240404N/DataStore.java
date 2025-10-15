import java.io.*;
import java.util.*;

public class DataStore {
    private String usersFile;
    private String accountsFile;
    private String transactionsFile;

    // In-memory structures
    private Map usersMap; // username -> User
    private Map accountsMap; // accountId -> Account
    private List transactionsList; // list of Transaction

    // Simple counters for IDs
    private long nextAccountId = 1000;
    private long nextTransactionId = 100000;

    public DataStore(String usersFile, String accountsFile, String transactionsFile) {
        this.usersFile = usersFile;
        this.accountsFile = accountsFile;
        this.transactionsFile = transactionsFile;
        usersMap = new HashMap();
        accountsMap = new HashMap();
        transactionsList = new ArrayList();
    }

    public void loadAll() {
        loadUsers();
        loadAccounts();
        loadTransactions();
    }

    private void ensureDataDirExists(String path) {
        File f = new File(path);
        File dir = f.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
    }

    private void loadUsers() {
        BufferedReader br = null;
        try {
            File f = new File(usersFile);
            if (!f.exists()) {
                ensureDataDirExists(usersFile);
                return;
            }
            br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;
                // format: username|passwordHash
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String passHash = parts[1];
                    User u = new User(username, passHash);
                    usersMap.put(username, u);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception e) {}
        }
    }

    private void loadAccounts() {
        BufferedReader br = null;
        try {
            File f = new File(accountsFile);
            if (!f.exists()) {
                ensureDataDirExists(accountsFile);
                return;
            }
            br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;
                // format: accountId|username|balance
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String aid = parts[0];
                    String username = parts[1];
                    double bal = 0.0;
                    try { bal = Double.parseDouble(parts[2]); } catch (Exception ex) { bal = 0.0; }
                    Account a = new Account(aid, username, bal);
                    accountsMap.put(aid, a);
                    try {
                        long val = Long.parseLong(aid);
                        if (val >= nextAccountId) nextAccountId = val + 1;
                    } catch (Exception ex) {}
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load accounts: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception e) {}
        }
    }

    private void loadTransactions() {
        BufferedReader br = null;
        try {
            File f = new File(transactionsFile);
            if (!f.exists()) {
                ensureDataDirExists(transactionsFile);
                return;
            }
            br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;
                // format: txId|accountId|type|amount|timestamp
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String txId = parts[0];
                    String accountId = parts[1];
                    String type = parts[2];
                    double amount = 0.0;
                    long ts = 0L;
                    try { amount = Double.parseDouble(parts[3]); } catch (Exception ex) { amount = 0.0; }
                    try { ts = Long.parseLong(parts[4]); } catch (Exception ex) { ts = 0L; }
                    Transaction t = new Transaction(txId, accountId, type, amount, ts);
                    transactionsList.add(t);
                    try {
                        long val = Long.parseLong(txId);
                        if (val >= nextTransactionId) nextTransactionId = val + 1;
                    } catch (Exception ex) {}
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load transactions: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception e) {}
        }
    }

    public void saveAll() {
        saveUsers();
        saveAccounts();
        saveTransactions();
    }

    private void saveUsers() {
        PrintWriter pw = null;
        try {
            File f = new File(usersFile);
            ensureDataDirExists(usersFile);
            pw = new PrintWriter(new FileWriter(f, false));
            for (Iterator it = usersMap.values().iterator(); it.hasNext();) {
                User u = (User) it.next();
                pw.println(u.getUsername() + "|" + u.getPasswordHash());
            }
        } catch (Exception e) {
            System.out.println("Failed to save users: " + e.getMessage());
        } finally {
            try { if (pw != null) pw.close(); } catch (Exception e) {}
        }
    }

    private void saveAccounts() {
        PrintWriter pw = null;
        try {
            File f = new File(accountsFile);
            ensureDataDirExists(accountsFile);
            pw = new PrintWriter(new FileWriter(f, false));
            for (Iterator it = accountsMap.values().iterator(); it.hasNext();) {
                Account a = (Account) it.next();
                pw.println(a.getAccountId() + "|" + a.getOwnerUsername() + "|" + a.getBalance());
            }
        } catch (Exception e) {
            System.out.println("Failed to save accounts: " + e.getMessage());
        } finally {
            try { if (pw != null) pw.close(); } catch (Exception e) {}
        }
    }

    private void saveTransactions() {
        PrintWriter pw = null;
        try {
            File f = new File(transactionsFile);
            ensureDataDirExists(transactionsFile);
            pw = new PrintWriter(new FileWriter(f, false));
            for (Iterator it = transactionsList.iterator(); it.hasNext();) {
                Transaction t = (Transaction) it.next();
                pw.println(t.getTransactionId() + "|" + t.getAccountId() + "|" + t.getType() + "|" + t.getAmount() + "|" + t.getTimestamp());
            }
        } catch (Exception e) {
            System.out.println("Failed to save transactions: " + e.getMessage());
        } finally {
            try { if (pw != null) pw.close(); } catch (Exception e) {}
        }
    }

    // User operations
    public boolean usernameExists(String username) {
        return usersMap.containsKey(username);
    }

    public void addUser(User u) {
        usersMap.put(u.getUsername(), u);
    }

    public User getUser(String username) {
        return (User) usersMap.get(username);
    }

    // Account operations
    public Account createAccountForUser(String username) {
        String aid = String.valueOf(nextAccountId++);
        Account a = new Account(aid, username, 0.0);
        accountsMap.put(aid, a);
        saveAccounts();
        return a;
    }

    public void printAccountsForUser(String username) {
        boolean any = false;
        for (Iterator it = accountsMap.values().iterator(); it.hasNext();) {
            Account a = (Account) it.next();
            if (a.getOwnerUsername().equals(username)) {
                System.out.println("Account ID: " + a.getAccountId() + " | Balance: " + a.getBalance());
                any = true;
            }
        }
        if (!any) {
            System.out.println("No accounts found for user: " + username);
        }
    }

    public Account getAccount(String accountId) {
        return (Account) accountsMap.get(accountId);
    }

    public boolean deposit(String accountId, double amount) {
        Account a = getAccount(accountId);
        if (a == null) return false;
        a.deposit(amount);
        String txId = String.valueOf(nextTransactionId++);
        Transaction t = new Transaction(txId, accountId, "DEPOSIT", amount, System.currentTimeMillis());
        transactionsList.add(t);
        saveAccounts();
        saveTransactions();
        return true;
    }

    public boolean withdraw(String accountId, double amount) {
        Account a = getAccount(accountId);
        if (a == null) return false;
        boolean ok = a.withdraw(amount);
        if (!ok) return false;
        String txId = String.valueOf(nextTransactionId++);
        Transaction t = new Transaction(txId, accountId, "WITHDRAW", amount, System.currentTimeMillis());
        transactionsList.add(t);
        saveAccounts();
        saveTransactions();
        return true;
    }

    public void printTransactionsForAccount(String accountId) {
        boolean any = false;
        for (Iterator it = transactionsList.iterator(); it.hasNext();) {
            Transaction t = (Transaction) it.next();
            if (t.getAccountId().equals(accountId)) {
                System.out.println("TxID: " + t.getTransactionId() + " | " + t.getType() + " | " + t.getAmount() + " | " + new java.util.Date(t.getTimestamp()));
                any = true;
            }
        }
        if (!any) {
            System.out.println("No transactions found for account: " + accountId);
        }
    }
}
