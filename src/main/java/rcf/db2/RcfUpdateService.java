package rcf.db2;

import jcrapi.model.MembersItem;
import org.mili.utils.Lambda;
import org.mili.utils.sql.dao.DaoFactory;
import org.mili.utils.sql.service.Service;
import org.mili.utils.sql.service.ServiceException;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class RcfUpdateService extends Service {

    private RcfUpdateDao _rcfUpdateDao = DaoFactory.getDao(RcfUpdateDao.class);

    public void updateMember(final MembersItem membersItem) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                _rcfUpdateDao.updateMember(membersItem);
                return null;
            }
        });
    }

    public void dbTransfer() throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                _rcfUpdateDao.dbTransfer();
                return null;
            }
        });
    }

}