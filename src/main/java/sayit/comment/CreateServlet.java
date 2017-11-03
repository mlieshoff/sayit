package sayit.comment;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.mili.utils.Log;
import org.mili.utils.sql.service.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class CreateServlet extends javax.servlet.http.HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            String user = null;
            String comment = null;
            String thread = null;
            FileItem theFile = null;
            List<FileItem> fileItems = upload.parseRequest(req);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    if ("user".equals(fileItem.getFieldName())) {
                        user = fileItem.getString();
                    } else if ("thread".equals(fileItem.getFieldName())) {
                        thread = fileItem.getString();
                    } else if ("comment".equals(fileItem.getFieldName())) {
                        comment = fileItem.getString();
                    }
                } else {
                    theFile = fileItem;
                }
            }
            if (StringUtils.isBlank(user)) {
                user = "anonym";
            }
            Log.info(this, "doPost", "user=%s, comment=%s, thread=%s", user, comment, thread);
            SayItService sayItService = ServiceFactory.getService(SayItService.class);
            if (StringUtils.isBlank(thread)) {
                sayItService.create(user, comment, theFile);
            } else {
                sayItService.post(Long.valueOf(thread), user, comment, theFile);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        resp.sendRedirect("/");
    }

}
