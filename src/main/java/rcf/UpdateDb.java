package rcf;

import org.mili.utils.sql.service.MigrationService;
import org.mili.utils.sql.service.ServiceException;
import org.mili.utils.sql.service.ServiceFactory;
import rcf.extract.ExtractedUser;
import rcf.extract.Extractor;
import rcf.service.RcfService;

import java.io.IOException;
import java.util.Map;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class UpdateDb {

    public static void main(String[] args) throws ServiceException, IOException {
        MigrationService migrationService = ServiceFactory.getService(MigrationService.class);
        migrationService.migrate(false);
        Map<String, ExtractedUser> extractedUsers = new Extractor().start();
//        Map<String, ExtractedUser> extractedUsers = new CrApiExtractor().start();
        for (Map.Entry<String, ExtractedUser> entry : extractedUsers.entrySet()) {
            System.out.println(entry.getValue().getNickaname() + " - " + entry.getValue().getSpends());
        }
        ServiceFactory.getService(RcfService.class).update(extractedUsers);
    }

}
