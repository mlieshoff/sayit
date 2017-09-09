package sayit.comment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FTPSTest {

    public static void main2(String[] args) {
        new FTPSTest().putFile("sayit2016.lima-ftp.de", 21, "sayit2016", "CBqE92ga6E", "/home/micha/Bilder/badword3.jpg", "sayit2016.lima-city.de/badword3.jpg");
    }

    public static void main(String[] args) {
        String server = "sayit2016.lima-ftp.de";
        int port = 21;
        String user = "sayit2016";
        String pass = "CBqE92ga6E";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("/home/micha/Bilder/badword3.jpg");

            String firstRemoteFile = "sayit2016.lima-city.de/badword4.jpg";
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
  public void putFile(String host,
                      int port,
                      String username,
                      String password,
                      String localFilename,
                      String remoteFilename) {
    try {
      FTPSClient ftpClient = new FTPSClient(false);
      // Connect to host
      ftpClient.connect(host, port);
      int reply = ftpClient.getReplyCode();
      if (FTPReply.isPositiveCompletion(reply)) {

        // Login
        if (ftpClient.login(username, password)) {

          // Set protection buffer size
          ftpClient.execPBSZ(0);
          // Set data channel protection to private
          ftpClient.execPROT("P");
          // Enter local passive mode
          ftpClient.enterLocalPassiveMode();

          // Store file on host
            InputStream is = new FileInputStream(localFilename);
	  if (ftpClient.storeFile(remoteFilename, is)) {
	    is.close();
	  } else {
	    System.out.println("Could not store file");
	  }
	  // Logout
	  ftpClient.logout();

        } else {
          System.out.println("FTP login failed");
        }

        // Disconnect
    	ftpClient.disconnect();

      } else {
        System.out.println("FTP connect to host failed");
      }
    } catch (IOException ioe) {
        ioe.printStackTrace();
      System.out.println("FTP client received network error");
    }
  }
}