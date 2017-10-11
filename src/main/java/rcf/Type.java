package rcf;

public enum Type {

    NAME("nickname"),
    TOTAL("join"),
    DONATION("spenden"),
    CROWN("truhe"),
    WIN("kampf"),
    WEEK("kw"),
    ;

    private final String pattern;

    Type(String pattern) {
        this.pattern = pattern;
    }

    public static Type get(String name) {
        for (Type type : values()) {
            if (name.toLowerCase().contains(type.pattern)) {
                return type;
            }
        }
        throw new IllegalStateException("could not find type for: " + name);
    }
}
