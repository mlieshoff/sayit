package rcf.db2;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Rcf2Crown {

    private final int userHash;
    private final int crowns;

    public Rcf2Crown(int userHash, int crowns) {
        this.userHash = userHash;
        this.crowns = crowns;
    }

    public int getUserHash() {
        return userHash;
    }

    public int getCrowns() {
        return crowns;
    }

}
