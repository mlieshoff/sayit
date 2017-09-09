package sayit.comment;

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
import java.util.UUID;

public class ThreadDao extends Dao {

    public Long create(final String username) throws DaoException {
        return doInDao(new Lambda<Long>() {
            @Override
            public Long exec(Object... params) throws SQLException {
                long uuid = UUID.randomUUID().getMostSignificantBits();
                update("insert into thread (id, user) values(?, ?);",
                        uuid,
                        username
                );
                return uuid;
            }});
    }

    public List<Thread> top() throws DaoException {
        return doInDao(new Lambda<List<Thread>>() {
            @Override
            public List<Thread> exec(Object... params) throws Exception {
                return query(new RowTransformer<Thread>() {
                    @Override
                    public Thread transform(ResultSet resultSet) throws SQLException {
                        return new Thread(resultSet.getLong(1), resultSet.getTimestamp(2), resultSet.getString(3));
                    }
                }, "select id, created, user from thread order by created desc limit 100");
            }
        });
    }

}