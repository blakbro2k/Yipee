package asg.games.yipee.game;

public class Version {
    private static final String release = "0";
    private static final String major = "0";
    private static final String minor = "0";
    private static final String patch = "0";
    private static final String releaseSeparator = ".";
    private static final String patchSeparator = "p";

    public static String printVersion() {
        return release + releaseSeparator + major + releaseSeparator + minor + patchSeparator + patch;
    }
}