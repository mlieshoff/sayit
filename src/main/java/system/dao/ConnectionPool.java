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

import com.google.common.base.Optional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    private final static ThreadLocal<ConnectionContext> cache = new ThreadLocal<>();

    public ConnectionContext get() throws SQLException {
        if (cache.get() == null) {
            cache.set(getConnection());
        }
        return cache.get();
    }

    private ConnectionContext getConnection() throws SQLException {
        try {
            return connection(false);
        } catch (Exception e) {
            int i = 0;
            while (i < 10) {
                try {
                    return connection(true);
                } catch (Exception e1) {
                    //
                }
                i ++;
            }
            throw new IllegalStateException(e);
        }
    }

    private ConnectionContext connection(boolean autoReconnect) throws Exception {
        String host = Optional.fromNullable(System.getenv("OPENSHIFT_MYSQL_DB_HOST")).or("localhost");
        String port = Optional.fromNullable(System.getenv("OPENSHIFT_MYSQL_DB_PORT")).or("3306");
        String username = Optional.fromNullable(System.getenv("OPENSHIFT_MYSQL_DB_USERNAME")).or("root");
        String password = Optional.fromNullable(System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD")).or("root");
        String url = "jdbc:mysql://" + host + ":" + port + "/sayit?useUnicode=true&characterEncoding=utf-8";
        if (autoReconnect) {
            url += "?autoReconnect=true";
        }
        Class.forName("com.mysql.jdbc.Driver");
        return new ConnectionContext(DriverManager.getConnection(url, username, password));
    }

    public void close() {
        if (cache.get() != null) {
            cache.remove();
        }
    }

    public static class ConnectionContext {
        private boolean inTransaction;
        private Connection connection;
        private long creationTime = System.currentTimeMillis();

        public ConnectionContext(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public boolean isInTransaction() {
            return inTransaction;
        }

        public void setInTransaction(boolean inTransaction) {
            this.inTransaction = inTransaction;
        }
    }

}