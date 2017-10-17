package rcf.extract;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class ExtractedUser {

    private String tag;
    private String nickaname;
    private String role;
    private int spends;
    private int crowns;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNickaname() {
        return nickaname;
    }

    public void setNickname(String nickaname) {
        this.nickaname = nickaname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSpends() {
        return spends;
    }

    public void setSpends(int spends) {
        this.spends = spends;
    }

    public int getCrowns() {
        return crowns;
    }

    public void setCrowns(int crowns) {
        this.crowns = crowns;
    }

}
