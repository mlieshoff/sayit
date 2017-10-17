package rcf.db;

/**
 * @author Michael Lieshoff, 16.10.17
 */
public class RcfClanWeek {

    private final int id;
    private final int year;
    private final int week;

    public RcfClanWeek(int id, int year, int week) {
        this.id = id;
        this.year = year;
        this.week = week;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

}
