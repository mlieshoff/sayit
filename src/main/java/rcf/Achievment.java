package rcf;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Achievment {

    private final String id;
    private final String title;
    private final String formula;
    private final String image;
    private final String mode;
    private final boolean active;

    private final AchievmentType achievmentType;

    private final List<Integer> thresholds = new ArrayList<>();

    public Achievment(String id, String title, String formula, String image, String mode, AchievmentType achievmentType,
            List<Integer> thresholds, boolean active) {
        this.id = id;
        this.title = title;
        this.formula = formula;
        this.image = image;
        this.mode = mode;
        this.achievmentType = achievmentType;
        this.active = active;
        if (CollectionUtils.isNotEmpty(thresholds)) {
            this.thresholds.addAll(thresholds);
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFormula() {
        return formula;
    }

    public String getImage() {
        return image;
    }

    public String getMode() {
        return mode;
    }

    public AchievmentType getAchievmentType() {
        return achievmentType;
    }

    public List<Integer> getThresholds() {
        return thresholds;
    }

    public boolean isActive() {
        return active;
    }

    public int getLevel(int result) {
        for (int i = thresholds.size() - 1; i >= 0; i --) {
            int border = thresholds.get(i);
            if ("=".equals(mode)) {
                if (result == border) {
                    return i;
                }
            } else {
                if (result >= border) {
                    return i;
                }
            }
        }
        return -1;
    }

}
