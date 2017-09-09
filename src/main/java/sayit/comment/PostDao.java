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

import org.apache.commons.lang3.StringUtils;
import system.dao.Dao;
import system.dao.DaoException;
import system.dao.RowTransformer;
import system.util.Lambda;
import system.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PostDao extends Dao {

    public long post(final long thread, final String username, final String comment) throws DaoException {
        return doInDao(new Lambda<Long>() {
            @Override
            public Long exec(Object... params) throws SQLException {
                long uuid = UUID.randomUUID().getMostSignificantBits();
                update("insert into post (id, thread, user, comment) values(?, ?, ?, ?);",
                        uuid,
                        thread,
                        username,
                        comment
                );
                return uuid;
            }});
    }

    public Map<Long, List<Post>> getBulk(final Set<Long> threadIds) throws DaoException {
        Log.info(this, "getBulk", "threadIds: %s", threadIds);
        if (threadIds.size() == 0) {
            return Collections.emptyMap();
        }
        return doInDao(new Lambda<Map<Long, List<Post>>>() {
            @Override
            public Map<Long, List<Post>> exec(final Object... params) throws SQLException {
                Map<Long, List<Post>> map = new LinkedHashMap<>();
                List<Post> list = query(new RowTransformer<Post>() {
                    @Override
                    public Post transform(ResultSet resultSet) throws SQLException {
                        return new Post(
                                resultSet.getLong(1),
                                resultSet.getLong(2),
                                resultSet.getTimestamp(3),
                                resultSet.getString(4),
                                resultSet.getString(5)
                        );
                    }
                }, "select id, thread, created, user, comment from post where thread in (" + StringUtils.join(threadIds, ',') + ") order by created desc");
                Log.info(this, "getBulk", "list: %s", list.size());
                for (Post post : list) {
                    List<Post> posts = map.get(post.getThread());
                    if (posts == null) {
                        posts = new ArrayList<>();
                        map.put(post.getThread(), posts);
                    }
                    posts.add(post);
                }
                Log.info(this, "getBulk", "map: %s", map.size());
                return map;
            }});
    }

}