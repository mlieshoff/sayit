package sayit.comment;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.mili.utils.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author
 */
public class Utils {

    private static final ThreadLocal<SimpleDateFormat> DATETIME = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<String>(){{
        add("png");
        add("jpg");
        add("jpeg");
        add("gif");
    }};

    public static int getShardIndex(long uuid) {
//        return (int) (uuid % 10);
        return 1;
    }

    public static boolean uploadFtp(Shard shard, String filename, byte[] content) {
        String server = shard.getHost();
        int port = shard.getPort();
        String user = shard.getUser();
        String pass = shard.getPassword();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String firstRemoteFile = shard.getFolder() + "/" + filename;
            Log.info(Utils.class, "uploadFtp", "filename=%s", firstRemoteFile);
            InputStream inputStream = new ByteArrayInputStream(content);
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            return done;
        } catch (IOException ex) {
            //
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                //
            }
        }
        return false;
    }

    public static String getFileExtension(FileItem fileItem) {
        return FilenameUtils.getExtension(fileItem.getName());
    }

    public static boolean isImageFileExtension(String extension) {
        return IMAGE_EXTENSIONS.contains(extension);
    }

    public static String formatDateTime(Date date) {
        return DATETIME.get().format(date);
    }

}
