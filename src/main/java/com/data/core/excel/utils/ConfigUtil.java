package com.data.core.excel.utils;

import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.DataConstant;
import com.data.core.excel.DataProcessException;
import com.data.core.excel.enums.BooleanTypeEnum;
import com.data.core.excel.enums.EmptyActionTypeEnum;
import com.data.core.excel.enums.NumberActionTypeEnum;
import com.data.core.excel.enums.TimeFormatTypeEnum;
import org.apache.poi.ss.usermodel.Cell;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by szty on 2018/9/3.
 */
public class ConfigUtil {
    private static final Logger logger = Logger.getLogger(ConfigUtil.class.getSimpleName());
    public static Object getEmptyValue(CellDataTypeConfig cellDataTypeConfig){
        Integer emptyType=cellDataTypeConfig.getEmptyAction();
        if(emptyType==null) {
            logger.severe(getColWarnMessage(cellDataTypeConfig.getKey(),"请配置如何处理空值"));
            throw new DataProcessException(getColWarnMessage(cellDataTypeConfig.getKey(),"请配置如何处理空值"));
        }
        EmptyActionTypeEnum emptyAction=EnumHelper.getEnum(EmptyActionTypeEnum.class,emptyType);
        switch (emptyAction){
            case Error:
                logger.severe(getColWarnMessage(cellDataTypeConfig.getKey(),"不能为空"));
                throw new DataProcessException(getColWarnMessage(cellDataTypeConfig.getKey(),"不能为空"), DataConstant.EMPTY_CELL_EXCEPTION_TYPE);
            default:
                if(cellDataTypeConfig.getDefaultValue()==null){
                    logger.severe(getColWarnMessage(cellDataTypeConfig.getKey(),"请设置默认值"));
                    throw  new DataProcessException("请设置默认值");
                }
                return cellDataTypeConfig.getDefaultValue();

        }
    }

    public static String getBooleanValue(Object value,CellDataTypeConfig cellDataTypeConfig){
        Integer booleanType=cellDataTypeConfig.getActionType();
        BooleanTypeEnum booleanTypeEnum=booleanType==null?BooleanTypeEnum.YesOrNot:
                EnumHelper.getEnum(BooleanTypeEnum.class,booleanType);

        return booleanTypeEnum.getLabel(Boolean.valueOf(value.toString()));
    }

    public static Object toBooleanValue(Cell cell,CellDataTypeConfig cellDataTypeConfig){
         final Integer cellType=cell.getCellType();
        Integer booleanType=cellDataTypeConfig.getActionType();
        BooleanTypeEnum booleanTypeEnum=booleanType==null?BooleanTypeEnum.YesOrNot:
                EnumHelper.getEnum(BooleanTypeEnum.class,booleanType);
         switch (cellType){
             case Cell.CELL_TYPE_BOOLEAN:
                 return cell.getBooleanCellValue();
             case Cell.CELL_TYPE_STRING:
                 return booleanTypeEnum.getEnumByLabel(cell.getStringCellValue());
             case Cell.CELL_TYPE_NUMERIC:
                 return booleanTypeEnum.getEnumByLabel(cell.getNumericCellValue()+"");
                 default:
                     throw new DataProcessException(getCellWarnMessage(cell,"格式不正确"));

         }
    }

    public static Object getDateValue(Object value,CellDataTypeConfig cellDataTypeConfig){
        Integer timeType=cellDataTypeConfig.getActionType();
        TimeFormatTypeEnum timeFormatTypeEnum =timeType==null? TimeFormatTypeEnum.DefaultCN : EnumHelper.getEnum(TimeFormatTypeEnum.class,timeType);
        if(timeFormatTypeEnum.equals(TimeFormatTypeEnum.TimeStamp)){
            Date date=(Date)value;
            return date.getTime();
        }
        return DateUtil.format((Date)value, timeFormatTypeEnum.getPattern());
    }

    public static Object toDateValue(Cell cell, CellDataTypeConfig cellDataTypeConfig){
        Integer timeType=cellDataTypeConfig.getActionType();
        Integer cellType=cell.getCellType();
        if (!cellType.equals(Cell.CELL_TYPE_STRING))
            throw new DataProcessException("时间格式不对");
        TimeFormatTypeEnum timeFormatTypeEnum =timeType==null? TimeFormatTypeEnum.DefaultCN : EnumHelper.getEnum(TimeFormatTypeEnum.class,timeType);
        Date date=DateUtil.parse(cell.getStringCellValue(), timeFormatTypeEnum.getPattern());
        if(timeFormatTypeEnum.equals(TimeFormatTypeEnum.TimeStamp))
            return date.getTime();
        return date;
    }

    /**
     * 用于导出
     * @param value
     * @param cellDataTypeConfig
     * @return
     */
    public static Object getNumberValue(Object value,CellDataTypeConfig cellDataTypeConfig){
        Number number=NumberUtil.getNumber(value);
        return getNumberObject(null,cellDataTypeConfig,number);
    }

    /**
     * 用于导入
     * @param cell
     * @param cellDataTypeConfig
     * @return
     */
    public static Object toNumberValue(Cell cell,CellDataTypeConfig cellDataTypeConfig){
        Number number=null;
        if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
            number=cell.getNumericCellValue();
        }else if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
            number=NumberUtil.getNumber(cell.getStringCellValue());
        }else
            throw new DataProcessException(getCellWarnMessage(cell,"格式不正确"));
        return getNumberObject(cell, cellDataTypeConfig, number);

    }

    private static Object getNumberObject(Cell cell, CellDataTypeConfig cellDataTypeConfig, Number number) {
        Integer numberAction = cellDataTypeConfig.getActionType();
        NumberActionTypeEnum numberActionTypeEnum =numberAction==null? NumberActionTypeEnum.Plain: EnumHelper.getEnum(NumberActionTypeEnum.class,numberAction);
        switch (numberActionTypeEnum){
            case ZoomOut:
                return number.doubleValue()*100;
            case ZoomIn:
                return NumberUtil.format(number.doubleValue()/100.0);
            case Format:
                String pattern=cellDataTypeConfig.getNumberFormatPattern();
                if(emptyObject(pattern))
                    throw new DataProcessException(cell==null?getColWarnMessage(cellDataTypeConfig.getKey(),"请设置数值格式化的模板"):getColWarnMessage(cell,"请设置数值格式化的模板"));
                return String.format(pattern,number);
            default:
                return number;

        }
    }

    public static String toStringValue(Cell cell,CellDataTypeConfig cellDataTypeConfig){
        final Integer cellType=cell.getCellType();
        switch (cellType){
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return df.format(cell.getNumericCellValue());
                default:
                    throw new DataProcessException(getCellWarnMessage(cell,"数据格式不正确"));
        }

    }

    private static DecimalFormat df = new DecimalFormat("0");


    public static boolean emptyObject(Object object){
        return object==null|| object.toString().isEmpty();
    }

    public static String getCellWarnMessage(Cell cell,String message){
        return  String.format("第%d行第%d列,%s",cell.getRowIndex()+1,cell.getColumnIndex()+1,message);
    }
    public static String getColWarnMessage(Cell cell,String message){
        return  String.format("第%d列,%s",cell.getColumnIndex()+1,message);
    }
    public static String getColWarnMessage(String key,String message){
        return  String.format("第%s列,%s",key,message);
    }

    public static String getRowWarnMessage(Integer index,String message){
        return String.format("第%d行%s",index+1,message);

    }
}
