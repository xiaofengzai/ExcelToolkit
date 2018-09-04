package com.data.core.excel.enums;

import com.data.core.excel.DataProcessException;

public enum BooleanTypeEnum implements BaseEnum<Integer>{
    YesOrNot(1,new String[]{"是","否"}),
    HasOrNo(2,new String[]{"有","无"}),
    YOrN(3,new String[]{"Y","N"}),
    Sex(3,new String[]{"男","女"}),
    OneOrZero(3,new String[]{"1","0"});
    private int value;
    private String[] name;

    BooleanTypeEnum(Integer value,String[] name){
        this.value=value;
        this.name=name;
    }

    public Integer getValue(){
        return this.value;
    }
    public  String[] getName(){
        return this.name;
    }

    public String getLabel(Boolean bool){
        return bool?this.name[0]:this.name[1];
    }

    public Object getEnumByLabel(String label){
        BooleanTypeEnum[] booleanTypes= BooleanTypeEnum.values();
        for (BooleanTypeEnum item:booleanTypes) {
            if(item.getName()[0].equals(label)){
                return true;
            }else if(item.getName()[1].equals(label)){
                return false;
            }
        }
        try {
            Boolean result=Boolean.valueOf(label);
            return result;
        }catch (Exception e) {
            throw new DataProcessException("布尔值不正确");
        }
    }
}
