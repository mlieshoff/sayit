package rcf.db2;

import jcrapi.Api;
import jcrapi.model.DetailedClan;
import jcrapi.model.MembersItem;
import system.service.MigrationService;
import system.service.ServiceException;
import system.service.ServiceFactory;

import java.io.IOException;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class Update {

    private final RcfUpdateService rcfUpdateService = ServiceFactory.getService(RcfUpdateService.class);

    public static void main(String[] args) throws ServiceException, IOException {
        new Update().start();
    }

    private void start() throws ServiceException {
        MigrationService migrationService = ServiceFactory.getService(MigrationService.class);
        migrationService.migrate(false);
        rcfUpdateService.dbTransfer();
        Api api = new Api("http://api.cr-api.com/");
        DetailedClan detailedClan = api.getClan("RP88QQG");
        for (MembersItem membersItem : detailedClan.getMembers()) {
            updateMember(membersItem);
        }
    }

    private void updateMember(MembersItem membersItem) throws ServiceException {
        rcfUpdateService.updateMember(membersItem);
    }

}
