package romanmoran.com.mappingyou.enums;

/**
 * Created by roman on 07.08.2017.
 */

public enum GENDER {
    MALE("M"),FEMALE("F"),UNKNOWN("");
    private String value;

    GENDER(String value) {
        this.value = value;
    }

    public static GENDER fromValue(String value) {
        for (GENDER type : GENDER.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }

}
