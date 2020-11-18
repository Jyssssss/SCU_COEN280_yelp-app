package jshih.model.helpers;

import java.util.List;

import jshih.model.models.Time;

public final class DayTimeHelper {
    private static int midnightMinutes = 1440;
    public static final String MONDAY = "Monday";
    public static final String TUESDAY = "Tuesday";
    public static final String WEDNESDAY = "Wednesday";
    public static final String THURSDAY = "Thursday";
    public static final String FRIDAY = "Friday";
    public static final String SATURDAY = "Saturday";
    public static final String SUNDAY = "Sunday";

    public static final List<String> DAYS = List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);

    private DayTimeHelper() {
    }

    public static Time getOpenTime(String time) {
        String[] openTime = time.split(":");
        return new Time(Integer.parseInt(openTime[0]), Integer.parseInt(openTime[1]));
    }

    public static Time getCloseTime(String time) {
        String[] closeTime = time.split(":");
        closeTime[0] = closeTime[0].equals("00") && closeTime[1].equals("00") ? "24" : closeTime[0];
        return new Time(Integer.parseInt(closeTime[0]), Integer.parseInt(closeTime[1]));
    }

    public static int getCloseTime(int time) {
        return time == midnightMinutes ? 0 : time;
    }

    public static Time getStartMidnightTime() {
        return new Time(0);
    }

    public static Time getEndMidnightTime() {
        return new Time(midnightMinutes);
    }

    public static String getNextDay(String day) {
        switch (day) {
            case MONDAY:
                return TUESDAY;
            case TUESDAY:
                return WEDNESDAY;
            case WEDNESDAY:
                return THURSDAY;
            case THURSDAY:
                return FRIDAY;
            case FRIDAY:
                return SATURDAY;
            case SATURDAY:
                return SUNDAY;
            case SUNDAY:
                return MONDAY;
            default:
                return null;
        }
    }
}
