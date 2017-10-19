package rcf.db2;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Rcf2Nick {

    private final int userHash;
    private final String nick;

    public Rcf2Nick(int userHash, String nick) {
        this.userHash = userHash;
        this.nick = nick;
    }

    public int getUserHash() {
        return userHash;
    }

    public String getNick() {
        return nick;
    }

}
