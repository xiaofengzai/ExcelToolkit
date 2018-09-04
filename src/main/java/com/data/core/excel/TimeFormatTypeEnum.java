package com.data.core.excel;

public enum TimeFormatTypeEnum  implements BaseEnum<Integer>{
    SecondCN(1,"yyyy年MM月dd日 HH:mm:ss"),
    MinuteCN(2,"yyyy年MM月dd日 HH:mm"),
    DefaultCN(3,"yyyy年MM月dd日"),
    MonthCN(4,"yyyy年MM月"),
    TimeStamp(5,"时间戳"),
    SecondEN(6,"yyyy-MM-dd HH:mm:ss"),
    MinuteEN(7,"yyyy-MM-dd HH:mm"),
    DefaultEN(8,"yyyy-MM-dd"),
    MonthEN(9,"yyyy-MM"),
    SecondEN1(10,"yyyy/MM/dd HH:mm:ss"),
    MinuteEN1(11,"yyyy/MM/dd HH:mm"),
    DefaultEN1(12,"yyyy/MM/dd"),
    MonthEN1(13,"yyyy/MM");
    private Integer value;
    private String pattern;
    TimeFormatTypeEnum(Integer value, String pattern) {
        this.value=value;
        this.pattern=pattern;
    }
    public Integer getValue(){
        return this.value;
    }
    public String getPattern(){
        return this.pattern;
    }
}
