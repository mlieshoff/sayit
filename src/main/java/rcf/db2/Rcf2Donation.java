package rcf.db2;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Rcf2Donation {

    private final int userHash;
    private final int donations;

    public Rcf2Donation(int userHash, int donations) {
        this.userHash = userHash;
        this.donations = donations;
    }

    public int getUserHash() {
        return userHash;
    }

    public int getDonations() {
        return donations;
    }

}
