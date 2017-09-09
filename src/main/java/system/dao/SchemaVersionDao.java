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

import system.util.Lambda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SchemaVersionDao extends Dao {

    private boolean mustUpdate = false;

    public static final String TABLE = "schemaversion";

    public void init(final boolean dropAndCreate) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                try {
                    querySingle(new RowTransformer<Object>() {
                        @Override
                        public Object transform(ResultSet resultSet) throws SQLException {
                            return null;
                        }
                    }, "select max(id) from " + TABLE);
                } catch (SQLException e) {
                    mustUpdate = true;
                }
                if (mustUpdate || dropAndCreate) {
                    update("drop table if exists schemaversion;");
                    update("CREATE TABLE if not exists schemaversion("
                            + "id int NOT NULL AUTO_INCREMENT, "
                            + "version int NOT NULL, "
                            + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                            + "PRIMARY KEY (id))");
                }
                return null;
            }});
    }

    public void executeScript(final String script) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                String[] split = script.split("[;]");
                for (String sql : split) {
                    if (sql.length() > 2 && !sql.startsWith("--")) {
                        execute(sql);
                    }
                }
                return null;
            }});
    }

    public int readLastSchemaVersion() throws DaoException {
        return doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                List<Integer> list = query(new RowTransformer<Integer>() {
                    @Override
                    public Integer transform(ResultSet resultSet) throws SQLException {
                        return resultSet.getInt(1);
                    }
                }, "select max(version) from " + TABLE);
                if (list.size() > 0) {
                    return list.get(0);
                }
                return 0;
            }});
    }

    public void setLastSchemaVersion(final int lastSchemaVersion) throws DaoException {
        doInDao(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws SQLException {
                update("insert into schemaversion (version) values(?);", lastSchemaVersion);
                return null;
            }});
    }

}