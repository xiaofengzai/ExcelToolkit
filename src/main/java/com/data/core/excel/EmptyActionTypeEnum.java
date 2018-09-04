package com.data.core.excel;

public enum EmptyActionTypeEnum implements BaseEnum<Integer>{
    Error(1,"报错"),
    FillDefault(2,"填充默认值");
    private Integer value;
    private String name;
    EmptyActionTypeEnum(Integer value, String name){
        this.value=value;
        this.name=name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
