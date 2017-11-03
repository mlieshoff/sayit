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

import org.mili.utils.Lambda;
import org.mili.utils.sql.RowTransformer;
import org.mili.utils.sql.dao.Dao;
import org.mili.utils.sql.dao.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ShardDao extends Dao {

    private Map<Integer, Shard> cache = new HashMap<>();

    public Shard get(final int id) throws DaoException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        Shard shard = doInDao(new Lambda<Shard>() {
            @Override
            public Shard exec(Object... params) throws SQLException {
                return querySingle(new RowTransformer<Shard>() {
                    @Override
                    public Shard transform(ResultSet resultSet) throws SQLException {
                        return new Shard(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                    }
                }, "select host, folder, user, password, port from shard where id = ?", id);
            }});
        cache.put(id, shard);
        return shard;
    }

}