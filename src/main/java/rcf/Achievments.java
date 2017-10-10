package rcf;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import java.util.Set;

public class Achievments {

    private static final JexlEngine JEXL = new JexlEngine();

    static {
        JEXL.setCache(512);
        JEXL.setLenient(false);
        JEXL.setSilent(false);
    }

    private final Cube cube;

    private final JexlContext jexlContext;

    private final Set<Achievment> achievments;

    public Achievments(Cube cube, Set<Achievment> achievments) {
        this.cube = cube;
        this.achievments = achievments;
        jexlContext = new MapContext();
        jexlContext.set("c", new RfcFunctions(jexlContext, cube));
    }

    public void compute() {
        for (String nick : cube.getNicks()) {
            jexlContext.set("nick", nick);
            for (Achievment achievment : achievments) {
                String formula = achievment.getFormula();
                int result = (int) JEXL.createExpression(formula).evaluate(jexlContext);
                int level = achievment.getLevel(result);
                if (level >= 0) {
                    System.out.println(nick + " " + achievment.getId() + " -> " + result + " : " + level);
                }
            }
        }
    }

}
