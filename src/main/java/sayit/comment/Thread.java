package sayit.comment;

import java.util.Date;

/**
 * @author Michael Lieshoff, 08.07.16
 */
public class Thread {

    private final long id;
    private final Date timestamp;
    private final String user;

    public Thread(long id, Date timestamp, String user) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

}
