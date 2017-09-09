package system.service;

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

import system.dao.ConnectionPool;
import system.util.Lambda;
import system.util.Log;

import java.sql.Connection;
import java.sql.SQLException;

public class Service {

    private ConnectionPool connectionPool = new ConnectionPool();

    public <T> T doInService(Lambda<T> lambda, Object... objects) throws ServiceException {
        ConnectionPool.ConnectionContext connectionContext = enable();
        connectionContext.setInTransaction(true);
        Connection connection = connectionContext.getConnection();
        try {
            return lambda.exec();
        } catch(Exception e) {
            rollback(connection);
            throw new ServiceException(e);
        } finally {
            commit(connection);
            disable(connection);
            connectionContext.setInTransaction(false);
        }
    }

    private ConnectionPool.ConnectionContext enable() throws ServiceException {
        try {
            ConnectionPool.ConnectionContext connectionContext = connectionPool.get();
            Connection connection = connectionContext.getConnection();
            if (!connectionContext.isInTransaction()) {
                connection.setAutoCommit(false);
            }
            Log.info(this, "enable", "enable transaction: %s", connection);
            return connectionContext;
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    private void disable(Connection connection) throws system.service.ServiceException {
        try {
            Log.info(this, "disable", "disable transaction: %s", connection);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    private void rollback(Connection connection) throws system.service.ServiceException {
        try {
            Log.info(this, "rollback", "rollback transaction: %s", connection);
            connection.rollback();
        } catch (SQLException e) {
            throw new system.service.ServiceException(e);
        }
    }

    private void commit(Connection connection) throws system.service.ServiceException {
        try {
            Log.info(this, "commit", "commit transaction: %s", connection);
            connection.commit();
        } catch (SQLException e) {
            Log.error(this, "commit", "error: %s", e.getMessage());
            rollback(connection);
            throw new system.service.ServiceException(e);
        }
    }

    public <T> T doReadInService(Lambda<T> lambda, Object... objects) throws system.service.ServiceException {
        try {
            return lambda.exec();
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }

}