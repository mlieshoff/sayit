package elite.api.dto;

import java.util.Date;

/**
 * @author Michael Lieshoff, 30.08.17
 */
public class User {

    private String userId;

    private Date createdAt;
    private Date lastLogin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

}
