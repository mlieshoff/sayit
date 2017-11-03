package elite.api;

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

import elite.api.dao.UserDao;
import elite.api.dto.User;
import org.mili.utils.Lambda;
import org.mili.utils.sql.dao.DaoFactory;
import org.mili.utils.sql.service.Service;
import org.mili.utils.sql.service.ServiceException;

public class EliteService extends Service {

    private UserDao userDao = DaoFactory.getDao(UserDao.class);

    public User login(final String userId) throws ServiceException {
        return doInService(new Lambda<User>() {
            @Override
            public User exec(Object... params) throws Exception {
                User user = userDao.login(userId);
                if (user == null) {
                    user = userDao.create(userId);
                }
                return user;
            }
        });
    }

}