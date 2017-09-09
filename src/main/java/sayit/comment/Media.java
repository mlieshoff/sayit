package sayit.comment;

/**
 * @author Michael Lieshoff, 08.07.16
 */
public class Media {

    private final long reference;

    private final String filename;

    private final int shard;

    public Media(long reference, String filename, int shard) {
        this.reference = reference;
        this.filename = filename;
        this.shard = shard;
    }

    public long getReference() {
        return reference;
    }

    public String getFilename() {
        return filename;
    }

    public int getShard() {
        return shard;
    }

}
