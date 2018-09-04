package com.data.core.excel.utils;

import java.math.BigDecimal;

public class NumberUtil {
    public static  Double format(Double number){
        BigDecimal bg = new BigDecimal(number);
        Double result= bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return  result;
    }

    public static  Number getNumber(Object value){
        Number number=null;
        if(value instanceof java.lang.Integer){
            number=Integer.valueOf(value.toString());
        }else if(value instanceof Long){
            number=Integer.valueOf(value.toString());
        }else if(value instanceof Double){
            number =Double.valueOf(value.toString());
        }else if(value instanceof Short){
            number=Short.valueOf(value.toString());
        }
        return number;
    }
}
