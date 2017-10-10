package rcf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Cube {

    private Map<String, Integer> totalsByNick = new HashMap<>();
    private Map<String, Integer> totalCrownsByNick = new HashMap<>();
    private Map<String, Integer> totalDonationsByNick = new HashMap<>();
    private Map<String, Double> totalBattlesByNick = new HashMap<>();

    private Map<String, Integer> weekTotalsByNick = new HashMap<>();
    private Map<String, Integer> weekCrownsByNick = new HashMap<>();
    private Map<String, Integer> weekDonationsByNick = new HashMap<>();
    private Map<String, Double> weekBattlesByNick = new HashMap<>();

    private Map<String, Integer> numberOfWeeksByNick = new HashMap<>();

    private Map<String, List<Achievment>> achievmentsByNick = new HashMap<>();

    private Set<Integer> weeks = new TreeSet<>();
    private Set<String> nicks = new HashSet<>();

    private Map<String, Integer> ranksByNick = new HashMap<>();

    private List<IntegerUserData> usersByTotal = null;

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
        numberOfWeeksByNick.put(nickname, ++ i);
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
            IntegerUserData userData = new IntegerUserData();
            userData.setName(entry.getKey());
            userData.setValue(entry.getValue());
            list.add(userData);
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

    public Map<String, List<Achievment>> getAchievmentsByNick() {
        return achievmentsByNick;
    }

    public Set<Integer> getWeeks() {
        return weeks;
    }

    public Set<String> getNicks() {
        return nicks;
    }

    public int getNumberOfWeeksByNick(String nick) {
        return numberOfWeeksByNick.get(nick);
    }

    public Integer getRank(String nick) {
        return ranksByNick.get(nick);
    }

}
