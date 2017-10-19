package rcf.db2;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Rcf2Role {

    private final int userHash;
    private final int role;

    public Rcf2Role(int userHash, int role) {
        this.userHash = userHash;
        this.role = role;
    }

    public int getUserHash() {
        return userHash;
    }

    public int getRole() {
        return role;
    }

}
