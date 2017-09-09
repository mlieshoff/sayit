package system.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Michael Lieshoff, 01.09.17
 */
public class Crypta {

    public static final ThreadLocal<MessageDigest> MESSAGE_DIGEST = new ThreadLocal<MessageDigest>(){
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    public static long md5(String s) {
        MessageDigest md5 = MESSAGE_DIGEST.get();
        md5.reset();
        md5.update(s.getBytes());
        byte[] bKey = md5.digest();
        return ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
    }

}
