package rcf.db;

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

import system.dao.Dao;
import system.dao.DaoException;
import system.dao.RowTransformer;
import system.util.Lambda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RcfDao extends Dao {

    public static final RowTransformer<Integer> INT_ROW_TRANSFORMER = new RowTransformer<Integer>() {
        @Override
        public Integer transform(ResultSet resultSet) throws SQLException {
            return resultSet.getInt(1);
        }
    };

    public static final RowTransformer<RcfUser> USER_ROW_TRANSFORMER = new RowTransformer<RcfUser>() {
        @Override
        public RcfUser transform(ResultSet resultSet) throws SQLException {
            RcfUser rcfUser = new RcfUser(
                    resultSet.getInt("id"),
                    resultSet.getString("tag"),
                    resultSet.getTimestamp("joinedat")
            );
            rcfUser.setNickname(resultSet.getString("nickname"));
            rcfUser.setRole(resultSet.getString("role"));
            return rcfUser;
        }
    };

    public static final RowTransformer<RcfClanWeek> CLAN_WEEK_ROW_TRANSFORMER = new RowTransformer<RcfClanWeek>() {
        @Override
        public RcfClanWeek transform(ResultSet resultSet) throws SQLException {
            RcfClanWeek rcfClanWeek = new RcfClanWeek(
                    resultSet.getInt("id"),
                    resultSet.getInt("year"),
                    resultSet.getInt("week")
            );
            return rcfClanWeek;
        }
    };

    public void create(final String tag) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                update("insert into rcf_user (tag) values(?);", tag);
                return null;
            }});
    }

    public void updateNickname(final RcfUser rcfUser, final String nickname) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                update("update rcf_user set nickname=? where id=?;", nickname, rcfUser.getId());
                return null;
            }});
    }

    public void updateRole(final RcfUser rcfUser, final String role) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                update("update rcf_user set role=? where id=?;", role, rcfUser.getId());
                return null;
            }});
    }

    public RcfUser findByTag(final String tag) throws DaoException {
        return doInDao(new Lambda<RcfUser>() {
            @Override
            public RcfUser exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from rcf_user where tag=?", tag);
            }});
    }

    public void createClanWeek(final int year, final int week) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                update("insert into rcf_clan_week(year, week) values(?, ?);",
                        year,
                        week
                );
                return null;
            }});
    }

    public RcfClanWeek findClanWeek(final int year, final int week) throws DaoException {
        return doInDao(new Lambda<RcfClanWeek>() {
            @Override
            public RcfClanWeek exec(Object... params) throws SQLException {
                return querySingle(CLAN_WEEK_ROW_TRANSFORMER, "select * from rcf_clan_week where year=? and week=?",
                        year, week);
            }});
    }

    public void spend(final RcfClanWeek rcfClanWeek, final RcfUser rcfUser, final int spends) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                if (hasSpends(rcfClanWeek, rcfUser)) {
                    update("update rcf_spend_week_user set amount=? where user=? and clanWeek=?;",
                            spends,
                            rcfUser.getId(),
                            rcfClanWeek.getId()
                    );
                } else {
                    update("insert into rcf_spend_week_user(user, clanWeek, amount) values(?, ?, ?);",
                            rcfUser.getId(),
                            rcfClanWeek.getId(),
                            spends
                    );
                }
                return null;
            }
        });
    }

    private boolean hasSpends(final RcfClanWeek rcfClanWeek, final RcfUser rcfUser) throws SQLException {
        return querySingle(INT_ROW_TRANSFORMER, "select count(*) from rcf_spend_week_user where user=? and clanWeek=?", rcfUser.getId(), rcfClanWeek.getId()) == 1;
    }

    public void contribute(final RcfClanWeek rcfClanWeek, final RcfUser rcfUser, final int crowns) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                if (hasContribution(rcfClanWeek, rcfUser)) {
                    update("update rcf_chest_week_user set amount=? where user=? and clanWeek=?;",
                            crowns,
                            rcfUser.getId(),
                            rcfClanWeek.getId()
                    );
                } else {
                    update("insert into rcf_chest_week_user(user, clanWeek, amount) values(?, ?, ?);",
                            rcfUser.getId(),
                            rcfClanWeek.getId(),
                            crowns
                    );
                }
                return null;
            }
        });
    }

    private boolean hasContribution(final RcfClanWeek rcfClanWeek, final RcfUser rcfUser) throws SQLException {
        return querySingle(INT_ROW_TRANSFORMER, "select count(*) from rcf_chest_week_user where user=? and clanWeek=?", rcfUser.getId(), rcfClanWeek.getId()) == 1;
    }

    public RcfUser findByNick(final String nickname) throws DaoException {
        return doInDao(new Lambda<RcfUser>() {
            @Override
            public RcfUser exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from rcf_user where nickname=?", nickname);
            }});
    }

    public List<RcfUser> getUsers() throws DaoException {
        return doInDao(new Lambda<List<RcfUser>>() {
            @Override
            public List<RcfUser> exec(Object... params) throws SQLException {
                return query(USER_ROW_TRANSFORMER, "select * from rcf_user");
            }});
    }

    public List<RcfUser> getUsers2() throws SQLException {
        return query(USER_ROW_TRANSFORMER, "select * from rcf_user");
    }

    public List<RcfClanWeek> getRcfClanWeeks() throws DaoException {
        return doInDao(new Lambda<List<RcfClanWeek>>() {
            @Override
            public List<RcfClanWeek> exec(Object... params) throws SQLException {
                return query(CLAN_WEEK_ROW_TRANSFORMER, "select * from rcf_clan_week");
            }});
    }

    public List<RcfClanWeek> getRcfClanWeeks2() throws SQLException {
        return query(CLAN_WEEK_ROW_TRANSFORMER, "select * from rcf_clan_week");
    }

    public Integer getSpendsForWeekAndUser(final RcfClanWeek week, final RcfUser user) throws DaoException {
        return doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                return querySingle(INT_ROW_TRANSFORMER, "select amount from rcf_spend_week_user where user=? and clanWeek=?", user.getId(), week.getId());
            }});
    }

    public Integer getSpendsForWeekAndUser2(final RcfClanWeek week, final RcfUser user) throws SQLException {
        return querySingle(INT_ROW_TRANSFORMER, "select amount from rcf_spend_week_user where user=? and clanWeek=?", user.getId(), week.getId());
    }

    public Integer getCrownsForWeekAndUser(final RcfClanWeek week, final RcfUser user) throws DaoException {
        return doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                return querySingle(INT_ROW_TRANSFORMER, "select amount from rcf_chest_week_user where user=? and clanWeek=?", user.getId(), week.getId());
            }});
    }

    public Integer getCrownsForWeekAndUser2(final RcfClanWeek week, final RcfUser user) throws SQLException {
        return querySingle(INT_ROW_TRANSFORMER, "select amount from rcf_chest_week_user where user=? and clanWeek=?", user.getId(), week.getId());
    }

}