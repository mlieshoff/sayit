package rcf;

import com.google.common.base.Optional;
import org.apache.commons.collections4.CollectionUtils;
import rcf.achievment.Achievment;
import rcf.db.RcfUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Cube {

    private Set<Achievment> achievments = new LinkedHashSet<>();

    private Map<String, RcfUser> users = new HashMap<>();

    private Map<String, Integer> totalsByNick = new HashMap<>();
    private Map<String, Integer> totalCrownsByNick = new HashMap<>();
    private Map<String, Integer> totalDonationsByNick = new HashMap<>();
    private Map<String, Double> totalBattlesByNick = new HashMap<>();

    private Map<String, Integer> weekTotalsByNick = new HashMap<>();
    private Map<String, Integer> weekCrownsByNick = new HashMap<>();
    private Map<String, Integer> weekDonationsByNick = new HashMap<>();
    private Map<String, Double> weekBattlesByNick = new HashMap<>();

    private Map<String, Integer> numberOfWeeksByNick = new HashMap<>();

    private Map<String, Integer> numberOfWeekWinsByNick = new HashMap<>();
    private Map<Integer, String> weekWinnersByWeek = new HashMap<>();

    private Set<Integer> weeks = new TreeSet<>();
    private Set<String> nicks = new HashSet<>();
    private Set<String> bannedNicks = new HashSet<>();
    private Set<String> leaderNicks = new HashSet<>();
    private Set<String> viceNicks = new HashSet<>();
    private Set<String> founderNicks = new HashSet<>();

    private Map<String, Integer> ranksByNick = new HashMap<>();

    private List<IntegerUserData> usersByTotal = null;

    public static final String TOBI = "ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ";

    public Cube() {
        founderNicks.add("dine");
        founderNicks.add("micah");
        founderNicks.add(TOBI);
    }

    public void onTotal(String nickname, int value) {
        totalsByNick.put(nickname, value);
    }

    public void onBattle(String nickname, int week, double value) {
        totalBattlesByNick.put(nickname, value);
        weekBattlesByNick.put(getWeekNickname(nickname, week), value);
    }

    public void onCrown(String nickname, int week, int value) {
        totalCrownsByNick.put(nickname, value);
        weekCrownsByNick.put(getWeekNickname(nickname, week), value);
    }

    public void onDonation(String nickname, int week, int value) {
        totalDonationsByNick.put(nickname, value);
        weekDonationsByNick.put(getWeekNickname(nickname, week), value);
    }

    public void onWeek(String nickname, int week, int value) {
        nicks.add(nickname);
        weeks.add(week);
        weekTotalsByNick.put(getWeekNickname(nickname, week), value);
        Integer i = numberOfWeeksByNick.get(nickname);
        if (i == null) {
            i = 0;
        }
        if (value > 0) {
            numberOfWeeksByNick.put(nickname, ++i);
        }
    }

    private String getWeekNickname(String nickname, int week) {
        return nickname + "_" + week;
    }

    public List<IntegerUserData> getUsersByTotal() {
        if (usersByTotal != null) {
            return usersByTotal;
        }
        List<IntegerUserData> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalsByNick.entrySet()) {
            String nickname = entry.getKey();
            if (!isBanned(nickname)) {
                IntegerUserData userData = new IntegerUserData();
                userData.setName(nickname);
                userData.setValue(entry.getValue());
                userData.setTag(users.get(nickname).getTag());
                list.add(userData);
            }
        }
        Collections.sort(list, new Comparator<IntegerUserData>() {
            @Override
            public int compare(IntegerUserData o1, IntegerUserData o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });
        rankThem(new ArrayList<UserData>(list));
        for (IntegerUserData integerUserData : list) {
            ranksByNick.put(integerUserData.getName(), integerUserData.getRank());
        }
        usersByTotal = list;
        return list;
    }

    public void computeWeeks() {
        for (int week : weeks) {
            String highestWeekNick = null;
            int highest = 0;
            for (String nick : nicks) {
                String weekNick = getWeekNickname(nick, week);
                Integer actual = weekTotalsByNick.get(weekNick);
                if (actual != null) {
                    if (actual > highest) {
                        highest = actual;
                        highestWeekNick = nick;
                    }
                }
            }
            if (highestWeekNick != null) {
                weekWinnersByWeek.put(week, highestWeekNick);
                int number = Optional.fromNullable(numberOfWeekWinsByNick.get(highestWeekNick)).or(0);
                number ++;
                numberOfWeekWinsByNick.put(highestWeekNick, number);
            }
        }
    }

    public void rankThem(List<UserData> list) {
        int visual = 1;
        UserData oldUserData = null;
        for (UserData userData : list) {
            if (oldUserData != null && oldUserData.compareTo(userData) != 0) {
                visual ++;
            }
            userData.setRank(visual);
            oldUserData = userData;
        }
    }

    public Map<String, Integer> getTotalsByNick() {
        return totalsByNick;
    }

    public Map<String, Integer> getTotalCrownsByNick() {
        return totalCrownsByNick;
    }

    public Map<String, Integer> getTotalDonationsByNick() {
        return totalDonationsByNick;
    }

    public Map<String, Double> getTotalBattlesByNick() {
        return totalBattlesByNick;
    }

    public Map<String, Integer> getWeekTotalsByNick() {
        return weekTotalsByNick;
    }

    public Map<String, Integer> getWeekCrownsByNick() {
        return weekCrownsByNick;
    }

    public Map<String, Integer> getWeekDonationsByNick() {
        return weekDonationsByNick;
    }

    public Map<String, Double> getWeekBattlesByNick() {
        return weekBattlesByNick;
    }

    public Set<Integer> getWeeks() {
        return weeks;
    }

    public Collection<String> getNicks() {
        return CollectionUtils.subtract(nicks, bannedNicks);
    }

    public int getNumberOfWeeksByNick(String nick) {
        return numberOfWeeksByNick.get(nick);
    }

    public int getNumberOfWeekWinsByNick(String nick) {
        return Optional.fromNullable(numberOfWeekWinsByNick.get(nick)).or(0);
    }

    public Integer getRank(String nick) {
        return ranksByNick.get(nick);
    }

    public void init() {
        computeWeeks();
        getUsersByTotal();
    }

    public boolean isBanned(String nickname) {
        return bannedNicks.contains(nickname);
    }

    public boolean isLeader(String nickname) {
        return "Leader".equals(users.get(nickname).getRole());
    }

    public boolean isVice(String nickname) {
        return "Co-leader".equals(users.get(nickname).getRole());
    }

    public boolean isFounder(String nickname) {
        return founderNicks.contains(nickname);
    }

    public void addAchievment(Achievment achievment) {
        achievments.add(achievment);
    }

    public Set<Achievment> getAchievments() {
        return achievments;
    }

    public void onUser(RcfUser user) {
        users.put(user.getNickname(), user);
    }
}
