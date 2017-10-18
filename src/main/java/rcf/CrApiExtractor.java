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
package rcf;

import jcrapi.Api;
import jcrapi.model.ClanChest;
import jcrapi.model.DetailedClan;
import jcrapi.model.MembersItem;
import rcf.extract.ExtractedUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Lieshoff
 */
public class CrApiExtractor {

    public Map<String, ExtractedUser> start() {
        Map<String, ExtractedUser> map = new HashMap<>();
        Api api = new Api("http://api.cr-api.com/");
        DetailedClan detailedClan = api.getClan("RP88QQG");
        ClanChest clanChest = detailedClan.getClanChest();
        for (MembersItem membersItem : detailedClan.getMembers()) {
            String nick = membersItem.getName();
            String tag = membersItem.getTag();
            String role = membersItem.getRoleName();
            int donations = membersItem.getDonations();
            int crowns = membersItem.getClanChestCrowns();
            ExtractedUser extractedUser = new ExtractedUser();
            extractedUser.setNickname(nick);
            extractedUser.setTag(tag);
            extractedUser.setRole(role);
            extractedUser.setSpends(donations);
            extractedUser.setCrowns(crowns);
            map.put(extractedUser.getTag(), extractedUser);
            System.out.printf("%50s, %10s, %10s, %5d, %5d\n", nick, tag, role, donations, crowns);
        }
        System.out.println(detailedClan);
        return map;
    }

}
