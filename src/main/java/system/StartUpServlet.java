package system;

import system.service.MigrationService;
import system.service.ServiceFactory;
import system.util.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class StartUpServlet extends javax.servlet.http.HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        Log.info(this, "init", "call super.init()");
        super.init(config);
        try {
            Log.info(this, "init", "start migration");
            ServiceFactory.getService(MigrationService.class).migrate(false);
        } catch (Throwable e) {
            Log.error(this, "init", "error while migration: %s", e.getMessage());
            throw new IllegalStateException(e);
        }
    }

}
