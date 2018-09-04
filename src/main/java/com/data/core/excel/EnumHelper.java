package com.data.core.excel;

public class EnumHelper {
    public static <T extends BaseEnum> T getEnum(Class<T> clazz,Integer value){
        T[] enums=clazz.getEnumConstants();
        for (int i=0,len=enums.length;i<len;i++){
            if(enums[i].getValue().equals(value))
                return enums[i];
        }
        return null;
    }
}
