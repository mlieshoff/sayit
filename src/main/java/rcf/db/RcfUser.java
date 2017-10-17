package rcf.db;

import java.util.Date;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class RcfUser {

    private final int id;
    private final String tag;
    private final Date joinedAt;

    private String role;
    private String nickname;

    public RcfUser(int id, String tag, Date joinedAt) {
        this.id = id;
        this.tag = tag;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
