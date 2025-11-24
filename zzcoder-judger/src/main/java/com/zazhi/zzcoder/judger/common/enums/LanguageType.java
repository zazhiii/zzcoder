package com.zazhi.zzcoder.judger.common.enums;

public enum LanguageType {
    JAVA("Java"),
    C("C"),
    CPP("C++"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript");

    private final String value;

    LanguageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LanguageType fromValue(String value) {
        for (LanguageType type : LanguageType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown language type: " + value);
    }

}
