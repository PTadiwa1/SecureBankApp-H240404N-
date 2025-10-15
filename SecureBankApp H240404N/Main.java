import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        DataStore ds = new DataStore("data/users.txt", "data/accounts.txt", "data/transactions.txt");
        ds.loadAll();

        AuthService auth = new AuthService(ds);
        Scanner sc = new Scanner(System.in);

        System.out.println("\u001B[36m" + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                 💳  SECURE BANKING SYSTEM  💳              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + "\u001B[0m");

        while (true) {
            System.out.println("\n\u001B[33mMain Menu:\u001B[0m");
            System.out.println("╔════════════════════════╗");
            System.out.println("║ 1) Register            ║");
            System.out.println("║ 2) Login               ║");
            System.out.println("║ 3) Exit                ║");
            System.out.println("╚════════════════════════╝");
            System.out.print("👉 Choose option: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    auth.registerInteractive(sc);
                    break;
                case "2":
                    User user = auth.loginInteractive(sc);
                    if (user != null) {
                        userMenu(sc, user, ds);
                    }
                    break;
                case "3":
                    System.out.println("\u001B[35mThank you for banking with us! Goodbye.\u001B[0m");
                    ds.saveAll();
                    sc.close();
                    return;
                default:
                    System.out.println("\u001B[31m⚠ Invalid option! Please try again.\u001B[0m");
            }
        }
    }

    private static void userMenu(Scanner sc, User user, DataStore ds) {
        System.out.println("\n\u001B[32mWelcome, " + user.getUsername().toUpperCase() + "! \u001B[0m");
        while (true) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║ 1) Create Account                ║");
            System.out.println("║ 2) View Accounts                 ║");
            System.out.println("║ 3) Deposit Funds                 ║");
            System.out.println("║ 4) Withdraw Funds                ║");
            System.out.println("║ 5) View Transactions             ║");
            System.out.println("║ 6) Logout                        ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("👉 Choose: ");

            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    Account acc = ds.createAccountForUser(user.getUsername());
                    System.out.println("\u001B[32m✅ Created account ID: " + acc.getAccountId() + "\u001B[0m");
                    break;
                case "2":
                    ds.printAccountsForUser(user.getUsername());
                    break;
                case "3":
                    handleDeposit(sc, ds);
                    break;
                case "4":
                    handleWithdraw(sc, ds);
                    break;
                case "5":
                    handleTransactions(sc, ds);
                    break;
                case "6":
                    System.out.println("\u001B[36m👋 Logging out...\u001B[0m");
                    ds.saveAll();
                    return;
                default:
                    System.out.println("\u001B[31m⚠ Invalid option! Try again.\u001B[0m");
            }
        }
    }

    private static void handleDeposit(Scanner sc, DataStore ds) {
        System.out.print("Enter account ID: ");
        String aid = sc.nextLine().trim();
        System.out.print("Enter amount to deposit: ");
        String dam = sc.nextLine().trim();
        try {
            double da = Double.parseDouble(dam);
            if (da <= 0) {
                System.out.println("\u001B[31mAmount must be greater than 0!\u001B[0m");
                return;
            }
            ds.deposit(aid, da);
            System.out.println("\u001B[32m💰 Deposit successful!\u001B[0m");
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mInvalid number entered.\u001B[0m");
        }
    }

    private static void handleWithdraw(Scanner sc, DataStore ds) {
        System.out.print("Enter account ID: ");
        String wid = sc.nextLine().trim();
        System.out.print("Enter amount to withdraw: ");
        String wam = sc.nextLine().trim();
        try {
            double wa = Double.parseDouble(wam);
            if (wa <= 0) {
                System.out.println("\u001B[31mAmount must be greater than 0!\u001B[0m");
                return;
            }
            boolean ok = ds.withdraw(wid, wa);
            if (ok)
                System.out.println("\u001B[32m💸 Withdrawal successful!\u001B[0m");
            else
                System.out.println("\u001B[31mInsufficient funds or invalid account.\u001B[0m");
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mInvalid amount.\u001B[0m");
        }
    }

    private static void handleTransactions(Scanner sc, DataStore ds) {
        System.out.print("Enter account ID: ");
        String tid = sc.nextLine().trim();
        ds.printTransactionsForAccount(tid);
    }
}

