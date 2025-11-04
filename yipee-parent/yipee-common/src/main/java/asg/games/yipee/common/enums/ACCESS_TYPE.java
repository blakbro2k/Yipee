package asg.games.yipee.common.enums;

/**
 * Access visibility types for the table:
 * <ul>
 *   <li>PRIVATE – Only invited players may join.</li>
 *   <li>PUBLIC – Open to any player.</li>
 *   <li>PROTECTED – Joinable with specific constraints.</li>
 * </ul>
 */
public enum ACCESS_TYPE {

    PRIVATE(Constants.ENUM_VALUE_PRIVATE), PUBLIC(Constants.ENUM_VALUE_PUBLIC), PROTECTED(Constants.ENUM_VALUE_PROTECTED);
    private final String accessType;

    ACCESS_TYPE(String accessType) {
        this.accessType = accessType;
    }

    public String getValue() {
        return accessType;
    }
}
