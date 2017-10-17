package rcf.service;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import rcf.db.RcfClanWeek;
import rcf.db.RcfDao;
import rcf.db.RcfUser;
import rcf.extract.ExtractedUser;
import system.dao.DaoException;
import system.dao.DaoFactory;
import system.service.Service;
import system.service.ServiceException;
import system.util.Lambda;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class RcfService extends Service {

    private RcfDao _rcfDao = DaoFactory.getDao(RcfDao.class);

    public void update(final Map<String, ExtractedUser> extractedUsers) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                RcfClanWeek rcfClanWeek = getClanWeek();
                for (ExtractedUser extractedUser : extractedUsers.values()) {
                    RcfUser rcfUser = updateUser(extractedUser);
                    _rcfDao.spend(rcfClanWeek, rcfUser, extractedUser.getSpends());
                    _rcfDao.contribute(rcfClanWeek, rcfUser, extractedUser.getCrowns());
                }
                return null;
            }
        });
    }

    private RcfUser updateUser(ExtractedUser extractedUser) throws DaoException {
        String tag = extractedUser.getTag();
        String nickname = extractedUser.getNickaname();
        String role = extractedUser.getRole();
        if (StringUtils.isBlank(role)) {
            role = "Member";
        }
        RcfUser rcfUser = _rcfDao.findByTag(tag);
        if (rcfUser == null) {
            _rcfDao.create(tag);
            rcfUser = _rcfDao.findByTag(tag);
        }
        if (!nickname.equals(rcfUser.getNickname())) {
            _rcfDao.updateNickname(rcfUser, nickname);
        }
        if (!role.equals(rcfUser.getRole())) {
            _rcfDao.updateRole(rcfUser, role);
        }
        return rcfUser;
    }

    private RcfClanWeek getClanWeek() throws DaoException {
        Calendar calendar = new GregorianCalendar();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        return getClanWeek(year, week);
    }

    private RcfClanWeek getClanWeek(int year, int week) throws DaoException {
        RcfClanWeek rcfClanWeek = _rcfDao.findClanWeek(year, week);
        if (rcfClanWeek == null) {
            _rcfDao.createClanWeek(year, week);
            rcfClanWeek = _rcfDao.findClanWeek(year, week);
        }
        return rcfClanWeek;
    }

    public boolean exists(final String nickname) throws ServiceException {
        return doInService(new Lambda<Boolean>() {
            @Override
            public Boolean exec(Object... params) throws Exception {
                return _rcfDao.findByNick(nickname) != null;
            }
        });
    }

    public RcfUser getUserByNick(final String nickname) throws ServiceException {
        return doInService(new Lambda<RcfUser>() {
            @Override
            public RcfUser exec(Object... params) throws Exception {
                return _rcfDao.findByNick(nickname);
            }
        });
    }

    public void setCrowns(final int year, final int week, final RcfUser rcfUser, final int crowns) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                RcfClanWeek rcfClanWeek = getClanWeek(year, week);
                _rcfDao.contribute(rcfClanWeek, rcfUser, crowns);
                return null;
            }
        });
    }

    public void setSpends(final int year, final int week, final RcfUser rcfUser, final int spends) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                RcfClanWeek rcfClanWeek = getClanWeek(year, week);
                _rcfDao.spend(rcfClanWeek, rcfUser, spends);
                return null;
            }
        });
    }

    public void updateUsers(final Map<String, ExtractedUser> extractedUsers) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                for (ExtractedUser extractedUser : extractedUsers.values()) {
                    updateUser(extractedUser);
                }
                return null;
            }
        });
    }

    public List<RcfUser> getUsers() throws ServiceException {
        return doInService(new Lambda<List<RcfUser>>() {
            @Override
            public List<RcfUser> exec(Object... params) throws Exception {
                return _rcfDao.getUsers();
            }
        });
    }

    public List<RcfClanWeek> getClanWeeks() throws ServiceException {
        return doInService(new Lambda<List<RcfClanWeek>>() {
            @Override
            public List<RcfClanWeek> exec(Object... params) throws Exception {
                return _rcfDao.getRcfClanWeeks();
            }
        });
    }

    public int getSpendsForWeekAndUser(final RcfClanWeek week, final RcfUser user) throws ServiceException {
        return Optional.fromNullable(doInService(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws Exception {
                return _rcfDao.getSpendsForWeekAndUser(week, user);
            }
        })).or(0);
    }

    public int getCrownsForWeekAndUser(final RcfClanWeek week, final RcfUser user) throws ServiceException {
        return Optional.fromNullable(doInService(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws Exception {
                return _rcfDao.getCrownsForWeekAndUser(week, user);
            }
        })).or(0);
    }

}