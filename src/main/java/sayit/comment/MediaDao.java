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
import org.mili.utils.Lambda;
import org.mili.utils.sql.RowTransformer;
import org.mili.utils.sql.dao.Dao;
import org.mili.utils.sql.dao.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MediaDao extends Dao {

    public Long create(final long reference, final String filename) throws DaoException {
        return doInDao(new Lambda<Long>() {
            @Override
            public Long exec(Object... params) throws SQLException {
                update("insert into media (reference, filename, shard) values(?, ?, ?);",
                        reference,
                        filename,
                        Utils.getShardIndex(reference)
                );
                return null;
            }});
    }

    public List<Media> getBulk(final List<Long> references) throws DaoException {
        return doInDao(new Lambda<List<Media>>() {
            @Override
            public List<Media> exec(Object... params) throws SQLException {
                return query(new RowTransformer<Media>() {
                    @Override
                    public Media transform(ResultSet resultSet) throws SQLException {
                        return new Media(resultSet.getLong(1), resultSet.getString(2), resultSet.getInt(3));
                    }
                }, "select reference, filename, shard from media where reference in (" + StringUtils.join(references, ',') + ")");
            }});
    }

}