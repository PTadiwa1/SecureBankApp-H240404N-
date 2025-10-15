import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String passwordHash; // SHA-256 hex

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
