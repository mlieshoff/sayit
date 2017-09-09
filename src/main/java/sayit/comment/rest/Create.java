package sayit.comment.rest;

/**
 * @author
 */
public class Create {

    private String user;
    private String comment;
    private String thread;
    private String file;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Create{" +
                "user='" + user + '\'' +
                ", comment='" + comment + '\'' +
                ", thread='" + thread + '\'' +
                ", file='" + file + '\'' +
                '}';
    }

}
