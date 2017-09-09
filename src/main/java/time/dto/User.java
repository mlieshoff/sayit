package time.dto;

/**
 * @author Michael Lieshoff, 30.08.17
 */
public class User {

    private final String _email;
    private final String _username;

    private final long _passwordHash;

    public User(String email, String username, long passwordHash) {
        _email = email;
        _username = username;
        _passwordHash = passwordHash;
    }

    public String getEmail() {
        return _email;
    }

    public String getUsername() {
        return _username;
    }

    public long getPasswordHash() {
        return _passwordHash;
    }

}
