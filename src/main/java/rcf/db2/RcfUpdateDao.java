package rcf.db2;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import jcrapi.model.MembersItem;
import org.mili.utils.Lambda;
import org.mili.utils.sql.RowTransformer;
import org.mili.utils.sql.dao.Dao;
import org.mili.utils.sql.dao.DaoException;
import org.mili.utils.sql.dao.DaoFactory;
import rcf.db.RcfClanWeek;
import rcf.db.RcfDao;
import rcf.db.RcfUser;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RcfUpdateDao extends Dao {

    public static final RowTransformer<Rcf2Tag> TAG_TRANSFORMER = new RowTransformer<Rcf2Tag>() {
        @Override
        public Rcf2Tag transform(ResultSet resultSet) throws SQLException {
            return new Rcf2Tag(resultSet.getInt("userHash"), resultSet.getString("tag"));
        }
    };

    public static final RowTransformer<Rcf2Nick> NICK_TRANSFORMER = new RowTransformer<Rcf2Nick>() {
        @Override
        public Rcf2Nick transform(ResultSet resultSet) throws SQLException {
            return new Rcf2Nick(resultSet.getInt("userHash"), resultSet.getString("nick"));
        }
    };

    public static final RowTransformer<Rcf2Role> ROLE_TRANSFORMER = new RowTransformer<Rcf2Role>() {
        @Override
        public Rcf2Role transform(ResultSet resultSet) throws SQLException {
            return new Rcf2Role(resultSet.getInt("userHash"), resultSet.getInt("role"));
        }
    };

    public static final RowTransformer<Rcf2Donation> DONATION_TRANSFORMER = new RowTransformer<Rcf2Donation>() {
        @Override
        public Rcf2Donation transform(ResultSet resultSet) throws SQLException {
            return new Rcf2Donation(resultSet.getInt("userHash"), resultSet.getInt("donations"));
        }
    };

    public static final RowTransformer<Rcf2Crown> CROWN_TRANSFORMER = new RowTransformer<Rcf2Crown>() {
        @Override
        public Rcf2Crown transform(ResultSet resultSet) throws SQLException {
            return new Rcf2Crown(resultSet.getInt("userHash"), resultSet.getInt("crowns"));
        }
    };

    public void updateMember(final MembersItem membersItem) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                Date modifiedAt = new Date();
                updateTag(membersItem, modifiedAt);
                updateNick(membersItem, modifiedAt);
                updateRole(membersItem, modifiedAt);
                updateDonations(membersItem, modifiedAt);
                updateCrowns(membersItem, modifiedAt);
                return null;
            }});
    }

    public void updateMember2(final MembersItem membersItem, Date modifiedAt) throws SQLException {
        updateTag(membersItem, modifiedAt);
        updateNick(membersItem, modifiedAt);
        updateRole(membersItem, modifiedAt);
        updateDonations(membersItem, modifiedAt);
        updateCrowns(membersItem, modifiedAt);
    }

    private void updateTag(MembersItem membersItem, Date modifiedAt) throws SQLException {
        Rcf2Tag last = querySingle(TAG_TRANSFORMER, "select * from rcf2_tag where tag=? and current=1;", membersItem.getTag());
        String tag = membersItem.getTag();
        int userHash = getUserHash(membersItem);
        if (last == null) {
            update("insert into rcf2_tag (userHash, tag, modifiedAt) values(?, ?, ?);", userHash, tag, modifiedAt);
        }
    }

    private int getUserHash(MembersItem membersItem) {
        return Hashing.md5().hashString(membersItem.getTag(), Charset.forName("UTF-8")).asInt();
    }

    private void updateNick(MembersItem membersItem, Date modifiedAt) throws SQLException {
        String nick = membersItem.getName();
        int userHash = getUserHash(membersItem);
        Rcf2Nick last = querySingle(NICK_TRANSFORMER, "select * from rcf2_nick where userHash=? and current=1;", userHash);
        if (last == null) {
            update("insert into rcf2_nick (userHash, nick, modifiedAt) values(?, ?, ?);", userHash, nick, modifiedAt);
        } else if (!last.getNick().equals(membersItem.getName())) {
            update("update rcf2_nick set current=0 where userHash=? and current=1;", userHash);
            update("insert into rcf2_nick (userHash, nick, modifiedAt) values(?, ?, ?);", userHash, nick, modifiedAt);
        }
    }

    private void updateRole(MembersItem membersItem, Date modifiedAt) throws SQLException {
        int role = membersItem.getRole();
        int userHash = getUserHash(membersItem);
        Rcf2Role last = querySingle(ROLE_TRANSFORMER, "select * from rcf2_role where userHash=? and current=1;", userHash);
        if (last == null) {
            update("insert into rcf2_role (userHash, role, modifiedAt) values(?, ?, ?);", userHash, role, modifiedAt);
        } else if (last.getRole() != role) {
            update("update rcf2_role set current=0 where userHash=? and current=1;", userHash);
            update("insert into rcf2_role (userHash, role, modifiedAt) values(?, ?, ?);", userHash, role, modifiedAt);
        }
    }

    private void updateDonations(MembersItem membersItem, Date modifiedAt) throws SQLException {
        int donations = membersItem.getDonations();
        int userHash = getUserHash(membersItem);
        Rcf2Donation last = querySingle(DONATION_TRANSFORMER, "select * from rcf2_donations where userHash=? and current=1;", userHash);
        if (last == null) {
            update("insert into rcf2_donations (userHash, donations, modifiedAt) values(?, ?, ?);", userHash, donations, modifiedAt);
        } else if (last.getDonations() != donations) {
            update("update rcf2_donations set current=0 where userHash=? and current=1;", userHash);
            update("insert into rcf2_donations (userHash, donations, modifiedAt) values(?, ?, ?);", userHash, donations, modifiedAt);
        }
    }

    private void updateCrowns(MembersItem membersItem, Date modifiedAt) throws SQLException {
        int crowns = membersItem.getClanChestCrowns();
        int userHash = getUserHash(membersItem);
        Rcf2Crown last = querySingle(CROWN_TRANSFORMER, "select * from rcf2_crowns where userHash=? and current=1;", userHash);
        if (last == null) {
            update("insert into rcf2_crowns (userHash, crowns, modifiedAt) values(?, ?, ?);", userHash, crowns, modifiedAt);
        } else if (last.getCrowns() != crowns) {
            update("update rcf2_crowns set current=0 where userHash=? and current=1;", userHash);
            update("insert into rcf2_crowns (userHash, crowns, modifiedAt) values(?, ?, ?);", userHash, crowns, modifiedAt);
        }
    }

    public void dbTransfer() throws DaoException {
        final Calendar NOW = new GregorianCalendar();
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                RcfDao rcfDao = DaoFactory.getDao(RcfDao.class);
                List<RcfUser> allOldUsers = rcfDao.getUsers2();
                List<RcfClanWeek> rcfClanWeeks = rcfDao.getRcfClanWeeks2();
                for (RcfUser rcfUser : allOldUsers) {
                    MembersItem membersItem = new MembersItem();
                    membersItem.setTag(rcfUser.getTag());
                    membersItem.setName(rcfUser.getNickname());
                    membersItem.setRole(1);
                    for (RcfClanWeek rcfClanWeek : rcfClanWeeks) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.setWeekDate(rcfClanWeek.getYear(), rcfClanWeek.getWeek(), Calendar.SUNDAY);
                        if (calendar.getTimeInMillis() >= NOW.getTimeInMillis()) {
                            calendar = NOW;
                        }
                        membersItem.setClanChestCrowns(Optional.fromNullable(rcfDao.getCrownsForWeekAndUser2(rcfClanWeek, rcfUser)).or(0));
                        membersItem.setDonations(Optional.fromNullable(rcfDao.getSpendsForWeekAndUser2(rcfClanWeek, rcfUser)).or(0));
                        updateMember2(membersItem, calendar.getTime());
                    }
                }
                return null;
            }});
    }
}