package time;

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
import org.mili.utils.sql.dao.DaoFactory;
import org.mili.utils.sql.service.Service;
import org.mili.utils.sql.service.ServiceException;
import time.dao.UserDao;
import time.dto.User;

public class TimeService extends Service {

    private UserDao userDao = DaoFactory.getDao(UserDao.class);

    public User register(final String username, final String email, final String password) throws ServiceException {
        return doInService(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws Exception {
                User userByName = userDao.findByUsername(username);
                if (userByName == null) {
                    User userByEmail = userDao.findByEmail(email);
                    if (userByEmail == null) {
                        return userDao.create(username, email, password);
                    } else {
                        throw new IllegalStateException("email already exists!");
                    }
                } else {
                    throw new IllegalStateException("username already exists!");
                }
            }
        });
    }

    public User login(final String username, final String password) throws ServiceException {
        return doInService(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws Exception {
                User user = userDao.findByUsernameAndPassword(username, password);
                if (user == null) {
                    throw new IllegalStateException("user not found!");
                } else {
                    return user;
                }
            }
        });
    }

}