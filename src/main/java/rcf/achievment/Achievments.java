package rcf.achievment;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import rcf.Cube;
import rcf.RfcFunctions;
import rcf.UserAchievment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Achievments {

    private static final JexlEngine JEXL = new JexlEngine();

    static {
        JEXL.setCache(512);
        JEXL.setLenient(false);
        JEXL.setSilent(false);
    }

    private final Cube cube;

    private final JexlContext jexlContext;

    public Achievments(Cube cube) {
        this.cube = cube;
        jexlContext = new MapContext();
        jexlContext.set("c", new RfcFunctions(jexlContext, cube));
    }

    public Map<String, List<UserAchievment>> compute() {
        Map<String, List<UserAchievment>> map = new HashMap<>();
        for (String nick : cube.getNicks()) {
            List<UserAchievment> list = map.get(nick);
            if (list == null) {
                list = new ArrayList<>();
                map.put(nick, list);
            }
            jexlContext.set("nick", nick);
            for (Achievment achievment : cube.getAchievments()) {
                if (achievment.isActive()) {
                    String formula = achievment.getFormula();
                    int result = (int) JEXL.createExpression(formula).evaluate(jexlContext);
                    int level = achievment.getLevel(result);
                    if (level >= 0) {
                        list.add(new UserAchievment(achievment, level, result));
                    }
                }
            }
        }
        return map;
    }

}
