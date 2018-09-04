package com.data.core.excel;

public enum  DataTypeEnum implements BaseEnum<Integer>{
    STRING(1,"字符串",StringActionTypeEnum.class),
    NUMBER(2,"数值",NumberActionTypeEnum.class),
    TIME(3,"时间",TimeFormatTypeEnum.class),
    BOOLEAN(4,"布尔",BooleanTypeEnum.class);
    ;
    private Integer value;
    private String name;
    private Class<?> clazz;

    DataTypeEnum(Integer value, String name, Class<?> clazz) {
        this.value = value;
        this.name = name;
        this.clazz = clazz;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
