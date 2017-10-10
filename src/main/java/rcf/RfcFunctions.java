package rcf;

import org.apache.commons.jexl2.JexlContext;

public class RfcFunctions {

    private final Cube cube;
    private final JexlContext jexlContext;

    public RfcFunctions(JexlContext jexlContext, Cube cube) {
        this.jexlContext = jexlContext;
        this.cube = cube;
    }

    public Integer numberOfWeeks() {
        String nick = (String) jexlContext.get("nick");
        return cube.getNumberOfWeeksByNick(nick);
    }

    public Integer rank() {
        String nick = (String) jexlContext.get("nick");
        return cube.getRank(nick);
    }

}
