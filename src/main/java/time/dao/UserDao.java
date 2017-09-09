package time.dao;

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
import system.util.Crypta;
import system.util.Lambda;
import time.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends Dao {

    public static final RowTransformer<User> USER_ROW_TRANSFORMER = new RowTransformer<User>() {
        @Override
        public User transform(ResultSet resultSet) throws SQLException {
            return new User(
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getLong("password_hash")
            );
        }
    };

    public User create(final String username, final String email, final String password) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                User user = new User(username, email, Crypta.md5(password));
                update("insert into time_user (username, email, password_hash) values(?, ?, ?);",
                        user.getUsername(),
                        user.getEmail(),
                        user.getPasswordHash()
                );
                return user;
            }});
    }

    public User findByUsername(final String username) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from time_user where username=?", username);
            }});
    }

    public User findByEmail(final String email) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from time_user where email=?", email);
            }});
    }

    public User findByUsernameAndPassword(final String username, final String password) throws DaoException {
        return doInDao(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws SQLException {
                return querySingle(USER_ROW_TRANSFORMER, "select * from time_user where username=? and password_hash=?",
                        username,
                        Crypta.md5(password)
                );
            }});
    }

}