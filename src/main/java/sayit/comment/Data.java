package sayit.comment;

import java.util.List;
import java.util.Map;

/**
 * @author Michael Lieshoff, 08.07.16
 */
public class Data {

    private final Map<Thread, List<Post>> threads;
    private final Map<Long, Media> medias;

    public Data(Map<Thread, List<Post>> threads, Map<Long, Media> medias) {
        this.threads = threads;
        this.medias = medias;
    }

    public Map<Thread, List<Post>> getThreads() {
        return threads;
    }

    public Map<Long, Media> getMedias() {
        return medias;
    }

}
