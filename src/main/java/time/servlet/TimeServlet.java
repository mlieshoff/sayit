package time.servlet;

import org.apache.commons.lang3.StringUtils;
import org.mili.utils.sql.service.ServiceException;
import org.mili.utils.sql.service.ServiceFactory;
import time.TimeService;
import time.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class TimeServlet extends javax.servlet.http.HttpServlet {

    private TimeService timeService = ServiceFactory.getService(TimeService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean login = StringUtils.isNotBlank(req.getParameter("login"));
        boolean register = StringUtils.isNotBlank(req.getParameter("register"));
        if (login) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            try {
                User user = timeService.login(username, password);
            } catch (ServiceException e) {
                PrintWriter printWriter = resp.getWriter();
                e.printStackTrace(printWriter);
                printWriter.flush();
            }
        } else if (register) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("get");
        printWriter.flush();
    }

}
