package rcf;

public class UserAchievment {

    private final Achievment achievment;
    private final int level;

    public UserAchievment(Achievment achievment, int level) {
        this.achievment = achievment;
        this.level = level;
    }

    public Achievment getAchievment() {
        return achievment;
    }

    public int getLevel() {
        return level;
    }

}
