package rcf.db2;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Rcf2Tag {

    private final int userHash;
    private final String tag;

    public Rcf2Tag(int userHash, String tag) {
        this.userHash = userHash;
        this.tag = tag;
    }

    public int getUserHash() {
        return userHash;
    }

    public String getTag() {
        return tag;
    }

}
