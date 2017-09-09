package sayit.comment;

/**
 * @author
 */
public class Shard {

    private final String host;
    private final String folder;
    private final String user;
    private final String password;

    private final int port;

    public Shard(String host, String folder, String user, String password, int port) {
        this.host = host;
        this.folder = folder;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getFolder() {
        return folder;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

}
