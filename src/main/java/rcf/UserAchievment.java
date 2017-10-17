package rcf;

import rcf.achievment.Achievment;

public class UserAchievment {

    private final Achievment achievment;
    private final int level;
    private final int count;

    public UserAchievment(Achievment achievment, int level, int count) {
        this.achievment = achievment;
        this.level = level;
        this.count = count;
    }

    public Achievment getAchievment() {
        return achievment;
    }

    public int getLevel() {
        return level;
    }

    public int getCount() {
        return count;
    }

}
