import java.util.Scanner;
import java.security.MessageDigest;

public class AuthService {
    private DataStore ds;

    public AuthService(DataStore ds) {
        this.ds = ds;
    }

    public void registerInteractive(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();
        if (username.length() == 0) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (ds.usernameExists(username)) {
            System.out.println("Username already exists.");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if (password.length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }
        String hash = hashPassword(password);
        User u = new User(username, hash);
        ds.addUser(u);
        ds.saveAll();
        System.out.println("User registered successfully.");
    }

    public User loginInteractive(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        User u = ds.getUser(username);
        if (u == null) {
            System.out.println("Login failed.");
            return null;
        }
        String hash = hashPassword(password);
        if (hash.equals(u.getPasswordHash())) {
            System.out.println("Login successful.");
            return u;
        } else {
            System.out.println("Login failed.");
            return null;
        }
    }

    // SHA-256 hex
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = password.getBytes("UTF-8");
            md.update(bytes);
            byte[] digest = md.digest();
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hex = Integer.toHexString(v);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
