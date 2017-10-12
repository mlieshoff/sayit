package rcf;

import com.google.common.base.Optional;
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

    public Integer banned() {
        String nick = (String) jexlContext.get("nick");
        return cube.isBanned(nick) ? 1 : 0;
    }

    public Integer leader() {
        String nick = (String) jexlContext.get("nick");
        return cube.isLeader(nick) ? 1 : 0;
    }

    public Integer vice() {
        String nick = (String) jexlContext.get("nick");
        return cube.isVice(nick) ? 1 : 0;
    }

    public Integer founder() {
        String nick = (String) jexlContext.get("nick");
        return cube.isFounder(nick) ? 1 : 0;
    }

    public Integer weekWins() {
        String nick = (String) jexlContext.get("nick");
        return Optional.fromNullable(cube.getNumberOfWeekWinsByNick(nick)).or(0);
    }

}
