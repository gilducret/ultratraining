package ultrathlete.com.training.util;

public class TimeUtil {
    public static String MakeReadable(long seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }
}
