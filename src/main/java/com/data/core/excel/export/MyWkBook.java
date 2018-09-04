package com.data.core.excel.export;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.data.core.excel.*;
import com.data.core.excel.utils.CollectionUtil;
import com.data.core.excel.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;


import java.util.List;
import java.util.function.Consumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyWkBook {
    private Workbook workbook;
    private SheetConfig sheetConfig;
    public  <T> void addData(List<T> list, Consumer<List<T>> before){
        if(before!=null){
             before.accept(list);
        }
         JSONArray array=new JSONArray((List<Object>) list);
        writeDataToSheet(array);
    }
    public <T> void addData(List<T> list){
        addData(list,null);
    }

    private void writeDataToSheet(JSONArray jsonArray){
        if(workbook.getSheet(sheetConfig.getSheetName())==null){
            validateAndInitHeader();
        }
        setSheetData(workbook.getSheet(sheetConfig.getSheetName()),jsonArray);

    }

    private   void setSheetData(Sheet sheet,JSONArray data) {
        int headerRows=sheetConfig.getHeaders().size();
        List<CellDataTypeConfig> keyConfigs=sheetConfig.getCellDataTypeConfigs();
        if(data==null)
            return ;
        Row row;
        for(int i=0,line=data.size();i<line;i++){
            row = sheet.createRow(i + headerRows);
            JSONObject jsonObject=data.getJSONObject(i);
            CellDataTypeConfig cellDataTypeConfig=null;
            if(sheetConfig.needSortNumber()){
                row.createCell(0).setCellValue(i+1);  //序号列
                for(int j=0,cel=keyConfigs.size();j<cel;j++){
                    cellDataTypeConfig=sheetConfig.getCellDataTypeConfigs().get(j);
                    row.createCell(j+1).setCellValue(filterValue(jsonObject,cellDataTypeConfig).toString());
                }
            }else{
                for(int j=0,cel=keyConfigs.size();j<cel;j++){
                    cellDataTypeConfig=sheetConfig.getCellDataTypeConfigs().get(j);
                    row.createCell(j).setCellValue(filterValue(jsonObject,cellDataTypeConfig).toString());
                }
            }

        }
    }

    private Object filterValue(JSONObject object,CellDataTypeConfig cellDataTypeConfig){
        String key=cellDataTypeConfig.getKey();
        if(!object.containsKey(key))
            throw new DataProcessException("数据不存在key:"+key);
        Object value=object.get(key);
        if(ConfigUtil.emptyObject(value)){
            return ConfigUtil.getEmptyValue(cellDataTypeConfig);
        }
        if(value instanceof java.lang.Boolean){
            return ConfigUtil.getBooleanValue(value,cellDataTypeConfig);
        }else if(value instanceof java.util.Date){
            return ConfigUtil.getDateValue(value,cellDataTypeConfig);
        }else if(value instanceof Number){
            return ConfigUtil.getNumberValue(value,cellDataTypeConfig);
        }else{
            return value;
        }

    }



    private void validateAndInitHeader(){
        List<SheetHeader> headers=sheetConfig.getHeaders();
        List<CellDataTypeConfig> cellDataTypeConfigs =sheetConfig.getCellDataTypeConfigs();
        if(CollectionUtil.isEmpty(headers))
            throw new DataProcessException("请配置表头");
        if(CollectionUtil.isEmpty(cellDataTypeConfigs))
            throw new DataProcessException("请配置列数据类型");
        if(sheetConfig.getDataLabelsLength()>=cellDataTypeConfigs.size() && sheetConfig.getDataLabelsLength()-cellDataTypeConfigs.size()<2){
            Sheet sheet=workbook.createSheet(sheetConfig.getSheetName());
            sheet.setDefaultColumnWidth((short)15);
            for(int i=0,len=headers.size();i<len;i++){
                SheetHeader header=headers.get(i);
                List<CellRangeAddress> cellRangeAddress=header.getMergeRegions();
                if(cellRangeAddress!=null){
                    cellRangeAddress.stream().forEach(item->{
                        sheet.addMergedRegion(item);
                    });
                }
                Row row = sheet.createRow(i);
                String[] headerNames=header.getHeaderLabels();
                for(int j=0,names=headerNames.length;j<names;j++){
                    Cell cell = row.createCell((short) j);
                    cell.setCellValue(headerNames[j]);
                    cell.setCellStyle(getCellStyle(workbook,header.getAlignType()));

                }
            }
        } else
            throw new DataProcessException("表头配置与列数据类型配置不正确");

    }

    private CellStyle getCellStyle(Workbook wb, Integer alignType){
        Font font=wb.createFont();
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short)11);
        CellStyle style = wb.createCellStyle();
        HorizontalAlignment horizontalAlignment=HorizontalAlignment.forInt(alignType);
        style.setAlignment(horizontalAlignment);
        style.setFont(font);
        return  style;
    }

}
