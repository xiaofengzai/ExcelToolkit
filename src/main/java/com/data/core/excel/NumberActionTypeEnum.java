package com.data.core.excel;

public enum NumberActionTypeEnum implements BaseEnum<Integer> {
    Plain(1,"原样输出"),
    ZoomIn(2,"放大100倍"),
    ZoomOut(3,"缩小一百被"),
    Format(4,"格式化输出"),;
    private Integer value;
    private String name;
    NumberActionTypeEnum(Integer value, String name){
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
