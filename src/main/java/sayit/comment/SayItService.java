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

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.mili.utils.Lambda;
import org.mili.utils.Log;
import org.mili.utils.sql.dao.DaoException;
import org.mili.utils.sql.dao.DaoFactory;
import org.mili.utils.sql.service.Service;
import org.mili.utils.sql.service.ServiceException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static sayit.comment.Utils.uploadFtp;

public class SayItService extends Service {

    private ThreadDao threadDao = DaoFactory.getDao(ThreadDao.class);
    private MediaDao mediaDao = DaoFactory.getDao(MediaDao.class);
    private ShardDao shardDao = DaoFactory.getDao(ShardDao.class);
    private PostDao postDao = DaoFactory.getDao(PostDao.class);

    public void create(final String username, final String comment, final FileItem fileItem) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                long thread = threadDao.create(username);
                long post = postDao.post(thread, username, comment);
                uploadIfNeccessary(post, fileItem);
                return null;
            }
        });
    }

    private void uploadIfNeccessary(long post, FileItem fileItem) throws DaoException, IOException {
        if (fileItem != null) {
            String filename = fileItem.getName();
            String extension = FilenameUtils.getExtension(filename);
            Shard shard = shardDao.get(Utils.getShardIndex(post));
            if (uploadFtp(shard, filename, fileItem.get())) {
                if (Utils.isImageFileExtension(extension)) {
                    BufferedImage bufferedImage = Thumbnails
                            .of(new ByteArrayInputStream(fileItem.get()))
                            .size(160, 160)
                            .asBufferedImage();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, FilenameUtils.getExtension(filename), baos);
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    baos.close();
                    uploadFtp(shard, "thumbnail." + filename, imageInByte);
                }
            }
            mediaDao.create(post, filename);
        }
    }

    public void post(final long thread, final String username, final String comment, final FileItem fileItem) throws ServiceException {
        doInService(new Lambda<Void>() {
            @Override
            public Void exec(Object... params) throws Exception {
                long post = postDao.post(thread, username, comment);
                uploadIfNeccessary(post, fileItem);
                return null;
            }
        });
    }

    public Data list() throws ServiceException {
        return doInService(new Lambda<Data>() {
            @Override
            public Data exec(Object... params) throws Exception {
                List<Thread> threads = threadDao.top();
                Log.info(this, "list", "threads: %s", threads.size());

                Map<Long, Thread> threadMap = new LinkedHashMap<>();
                for (Thread thread : threads) {
                    long id = thread.getId();
                    threadMap.put(id, thread);
                }
                Log.info(this, "list", "threadMap: %s", threadMap.size());

                Map<Thread, List<Post>> result = new LinkedHashMap<>();
                Map<Long, List<Post>> map = postDao.getBulk(threadMap.keySet());
                Log.info(this, "list", "map: %s", map.size());

                List<Long> references = new ArrayList<>();
                for (Map.Entry<Long, List<Post>> entry : map.entrySet()) {
                    Thread thread = threadMap.get(entry.getKey());
                    List<Post> posts = entry.getValue();
                    result.put(thread, posts);
                    for (Post post : posts) {
                        references.add(post.getId());
                    }
                }
                Log.info(this, "list", "references: %s", references.size());

                List<Media> mediaList = mediaDao.getBulk(references);
                Map<Long, Media> medias = new HashMap<>();
                for (Media media : mediaList) {
                    medias.put(media.getReference(), media);
                }
                Log.info(this, "list", "medias: %s", medias.size());

                return new Data(result, medias);
            }
        });
    }

}