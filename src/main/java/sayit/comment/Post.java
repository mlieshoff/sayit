package sayit.comment;

import java.util.Date;

/**
 * @author Michael Lieshoff, 08.07.16
 */
public class Post {

    private final long id;
    private final long thread;

    private final Date timestamp;

    private final String user;
    private final String comment;

    public Post(long id, long thread, Date timestamp, String user, String comment) {
        this.id = id;
        this.thread = thread;
        this.timestamp = timestamp;
        this.user = user;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getThread() {
        return thread;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", thread=" + thread +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
