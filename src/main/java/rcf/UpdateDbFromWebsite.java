package rcf;

import rcf.extract.ExtractedUser;
import rcf.extract.Extractor;
import rcf.service.RcfService;
import system.service.MigrationService;
import system.service.ServiceException;
import system.service.ServiceFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class UpdateDbFromWebsite {

    public static void main(String[] args) throws ServiceException, IOException {
        MigrationService migrationService = ServiceFactory.getService(MigrationService.class);
        migrationService.migrate(false);
        Map<String, ExtractedUser> extractedUsers = new Extractor().start();
        ServiceFactory.getService(RcfService.class).update(extractedUsers);
    }

}
