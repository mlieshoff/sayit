package rcf;

public enum Type {

    NAME("Nickname"),
    TOTAL("Join"),
    DONATION("Spenden"),
    CROWN("Truhe"),
    WIN("Kampf"),
    WEEK("KW"),
    ;

    private final String pattern;

    Type(String pattern) {
        this.pattern = pattern;
    }

    public static Type get(String name) {
        for (Type type : values()) {
            if (name.contains(type.pattern)) {
                return type;
            }
        }
        throw new IllegalStateException("could not find type for: " + name);
    }
}
