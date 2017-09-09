package system.dao;

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

import system.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcTemplate {

    private ConnectionPool connectionPool = new ConnectionPool();
    private Map<String, PreparedStatement> cache = new ConcurrentHashMap<>();

    protected <T> List<T> query(system.dao.RowTransformer<T> rowTransformer, String sql, Object... objects) throws SQLException {
        sql = normalize(sql);
        Log.info(this, "query", connectionPool.get() + " - query: " + sql);
        PreparedStatement preparedStatement = cache.get(sql);
        if (preparedStatement == null) {
            preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
            cache.put(sql, preparedStatement);
        }
        fillWithObjects(preparedStatement, objects);
        List<T> list = new ArrayList<>();
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {
            Log.info(this, "query", (connectionPool.get() + " - call transformer"));
            list.add(rowTransformer.transform(result));
        }
        Log.info(this, "query", (connectionPool.get() + " - size: " + list.size()));
        return list;
    }

    private void fillWithObjects(PreparedStatement preparedStatement, Object[] objects) throws SQLException {
        if (objects != null) {
            for (int i = 0; i < objects.length; i ++) {
                Object o = objects[i];
                if (o instanceof String) {
                    o = o.toString().trim();
                }
                preparedStatement.setObject(i + 1, o);
                Log.info(this, "fillWithObjects", "    " + (i + 1) + " = " + o);
            }
        }
    }

    protected <T> T querySingle(system.dao.RowTransformer<T> rowTransformer, String sql, Object... objects) throws SQLException {
        List<T> list = query(rowTransformer, sql, objects);
        int size = list.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return list.get(0);
        } else {
            throw new IllegalStateException("more than 1 result found[" + size + "]!");
        }
    }

    private String normalize(String sql) {
        sql = sql.replace("\r\n", "").replace("\n", "");
        sql = sql.replaceAll("\\s\\s+"," ");
        return sql;
    }

    protected int update(String sql, Object... objects) throws SQLException {
        sql = normalize(sql);
        Log.info(this, "update", connectionPool.get() + " - update: " + sql);
        PreparedStatement preparedStatement = cache.get(sql);
        if (preparedStatement == null) {
            preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
            cache.put(sql, preparedStatement);
        }
        fillWithObjects(preparedStatement, objects);
        return preparedStatement.executeUpdate();
    }

    protected boolean execute(String sql, Object... objects) throws SQLException {
        sql = normalize(sql);
        Log.info(this, "execute", connectionPool.get() + " - execute: " + sql);
        PreparedStatement preparedStatement = cache.get(sql);
        if (preparedStatement == null) {
            preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
            cache.put(sql, preparedStatement);
        }
        if (objects != null) {
            for (int i = 0; i < objects.length; i ++) {
                Object o = objects[i];
                if (o instanceof String) {
                    o = o.toString().trim();
                }
                preparedStatement.setObject(i + 1, o);
            }
        }
        return preparedStatement.execute();
    }

    protected long getTransactionCreationTime() throws SQLException {
        return connectionPool.get().getCreationTime();
    }

}