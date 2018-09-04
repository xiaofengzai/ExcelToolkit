package com.data.core.excel;

public enum StringActionTypeEnum implements BaseEnum<Integer> {
    DEFAULT(1,"原始输出");
    private Integer value;
    private String name;

    StringActionTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
