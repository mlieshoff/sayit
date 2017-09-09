package sayit.comment.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import system.util.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @author
 */
public class RestCreateServlet extends javax.servlet.http.HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = req.getInputStream();
        Preconditions.checkNotNull(inputStream, "no input!");

        String body = IOUtils.toString(inputStream);
        Log.info(this, "Rest: doPost", "body=%s", body);

        Preconditions.checkNotNull(inputStream, "no body!");

        Gson gson = new GsonBuilder().create();
        Create create = gson.fromJson(body, Create.class);

        Log.info(this, "Rest: doPost", "create=%s", create);

        PrintWriter printWriter = resp.getWriter();

        if (create == null) {
            printWriter.append("Please send json object like that:\n");
            create = new Create();
            create.setComment("comment");
            create.setThread("thread name");
            create.setUser("username");
            create.setFile("file content encoded in base 64");
            printWriter.append(gson.toJson(create));
            resp.setStatus(500);
        } else {
            printWriter.append("OK");
            resp.setStatus(200);
        }
        printWriter.flush();
    }

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        Create create = new Create();
        create.setComment("comment");
        create.setThread("thread name");
        create.setUser("username");
        create.setFile("file content encoded in base 64");
        System.out.println(gson.toJson(create));
    }

}
