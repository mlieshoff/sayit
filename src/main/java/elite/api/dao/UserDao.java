package elite.api.dao;

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

import elite.api.dto.User;
import org.mili.utils.Lambda;
import org.mili.utils.sql.RowTransformer;
import org.mili.utils.sql.dao.Dao;
import org.mili.utils.sql.dao.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDao extends Dao {

    public static final RowTransformer<User> USER_ROW_TRANSFORMER = new RowTransformer<User>() {
        @Override
        public User transform(ResultSet resultSet) throws SQLException {
            User user = new User();
            user.setUserId(resultSet.getString("userId"));
            user.setCreatedAt(resultSet.getTimestamp("createdAt"));
            user.setLastLogin(resultSet.getTimestamp("lastLogin"));
            return user;
        }
    };

    public User login(final String userId) throws DaoException {
        final User user = findByUserId(userId);
        if (user == null) {
            return null;
        } else {
            user.setLastLogin(new Date());
            return doInDao(new Lambda<User>() {
                @Override
                public User exec(Object... params) throws SQLException {
                    update("update elite_user set lastLogin = ? where userId=?;",
                            user.getLastLogin(),
                            user.getUserId()
                    );
                    return user;
                }
            });
        }
    }

    public User create(final String userId) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                User user = new User();
                user.setUserId(userId);
                user.setCreatedAt(new Date());
                user.setLastLogin(new Date());
                update("insert into elite_user(userId, createdAt, lastLogin) values(?, ?, ?);",
                        user.getUserId(),
                        user.getCreatedAt(),
                        user.getLastLogin()
                );
                return user;
            }
        });
    }

    public User findByUserId(final String userId) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from elite_user where userId=?", userId);
            }});
    }

}