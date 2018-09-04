package com.data.core.excel.in;

import com.alibaba.fastjson.JSONObject;
import com.data.core.excel.*;
import com.data.core.excel.SheetHeader;
import com.data.core.excel.enums.DataTypeEnum;
import com.data.core.excel.utils.ConfigUtil;
import com.data.core.excel.utils.EnumHelper;
import com.data.core.excel.utils.FileUtil;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by szty on 2018/9/3.
 */
@Data
public class WorkBookProcess {
    private  final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private BufferedInputStream in;
    private ExcelImportConfig excelImportConfig;
    public Workbook preProcess(BufferedInputStream in) {
        return createWorkbook(in);
    }
    private   Workbook createWorkbook(BufferedInputStream inputStream){
        try {
            String headerInfo= FileUtil.getFileHeader(inputStream,4);
            if(headerInfo.equals("d0cf11e0")){
                return new HSSFWorkbook(inputStream);
            }else if(headerInfo.equals("504b0304")){
                return new XSSFWorkbook(inputStream);
            }else
                throw new DataProcessException("文件格式不支持");
        } catch (IOException e) {
            logger.severe("解析Excel文件异常");
            throw new DataProcessException("解析Excel文件异常");
        }
    }

    public  void validate(BufferedInputStream in, Sheet sheet){
        List<SheetHeader> sheetHeaders=excelImportConfig.getHeaders();
        if(sheetHeaders.size()>0){
            validateHeader(in,sheet,sheetHeaders);
        }
    }

    public  Integer  execute(Consumer<List<JSONObject>> saveData ){
        Integer rowCount=0;
        Workbook workbook=preProcess(in);
        validate(in,workbook.getSheetAt(0));
        Integer startRow=excelImportConfig.getStartRowIndex();
        Sheet sheet=workbook.getSheetAt(0);
        List<JSONObject> list=new ArrayList<>();
        for(int i=startRow,excelLines=sheet.getLastRowNum();i<=excelLines;i++){
            Row row=sheet.getRow(i);
            if(row==null){
                if(!excelImportConfig.getAllowBlankRows())
                    throw  new DataProcessException(ConfigUtil.getRowWarnMessage(i,"为空"));
                else
                    continue;
            }
            JSONObject jsonObject=readRow(row);
            if(jsonObject!=null){
                jsonObject.put("oid",excelImportConfig.getOid());
                jsonObject.put("bid",excelImportConfig.getBid());
                list.add(jsonObject);
                rowCount++;
            }
            if(rowCount>0 && rowCount%DataConstant.BATCH_SAVE_SIZE==0){
                saveData.accept(list);
                list=new ArrayList<>();
            }
        }
        if(list.size()>0){
            saveData.accept(list);
            list=new ArrayList<>();
        }
        return rowCount;
    }

    private JSONObject readRow(Row row){
        Boolean valid=false;
        JSONObject object=new JSONObject();
        List<CellDataTypeConfig> configs=excelImportConfig.getCellDataTypeConfigs();
        Integer cols=configs.size();
        List<DataProcessException> emptyExceptions=new ArrayList<>();
        Integer emptyCols=0;
        for (int i=0;i<cols;i++){
            try {
                CellDataTypeConfig config=configs.get(i);
                if(row==null){
                    if(!excelImportConfig.getAllowBlankRows()){
                        throw new DataProcessException(ConfigUtil.getRowWarnMessage(row.getRowNum(),"数据为空"));
                    }else
                        return null;
                }
                DataTypeEnum dataTypeEnum= EnumHelper.getEnum(DataTypeEnum.class,config.getDataType());
                Cell cell=row.getCell(i);
                if(cell==null || cell.getCellType()==Cell.CELL_TYPE_BLANK || (cell.getCellType()==Cell.CELL_TYPE_STRING && ConfigUtil.emptyObject(cell.getStringCellValue()))){
                    emptyCols++;
                    object.put(config.getKey(),ConfigUtil.getEmptyValue(config));
                    continue;
                }else{
                    valid=true;
                    switch (dataTypeEnum){
                        case TIME:
                            object.put(config.getKey(),ConfigUtil.toDateValue(cell,config));
                            break;
                        case BOOLEAN:
                            object.put(config.getKey(),ConfigUtil.toBooleanValue(cell,config));
                        case NUMBER:
                            object.put(config.getKey(),ConfigUtil.toNumberValue(cell,config));
                        default:
                            object.put(config.getKey(),ConfigUtil.toStringValue(cell,config));
                    }

                }
            }catch (DataProcessException dpe){
                if(dpe.getType().equals(DataConstant.EMPTY_CELL_EXCEPTION_TYPE)){
                    emptyExceptions.add(dpe);
                }else
                    throw new DataProcessException(dpe.getMessage());
            }catch (Exception e){
                throw new DataProcessException("读取数据异常");
            }
        }
        if(emptyCols.equals(cols)){
            if(!excelImportConfig.getAllowBlankRows()){
                logger.severe(ConfigUtil.getRowWarnMessage(row.getRowNum(),"数据为空"));
                throw new DataProcessException(ConfigUtil.getRowWarnMessage(row.getRowNum(),"数据为空"));
            }else
                return null;
        }else if(emptyExceptions.size()>0)
            throw  emptyExceptions.get(0);
        return valid?object:null;
    }

    public  void validateHeader(BufferedInputStream inputStream, Sheet sheet , List<SheetHeader> sheetHeaders){
        for (int index=0,length=sheetHeaders.size();index<length; index++) {
            SheetHeader header=sheetHeaders.get(index);
            Row row=sheet.getRow(index);
            Integer headerLength=header.getHeaderLabels().length;
            if(header==null || row==null || headerLength>(row.getLastCellNum()-row.getFirstCellNum())){
                closeInputStream(inputStream);
                logger.severe("模板表头为空或表头不匹配");
                throw new DataProcessException("模板格式不正确");
            }
            Cell headerCell=null;
            for (int i=0;i<headerLength;i++){
                headerCell=row.getCell(i);
                try {
                    if( !headerCell.getStringCellValue().equals(header.getHeaderLabels()[i])) {
                        closeInputStream(inputStream);
                        logger.severe("模板表头不匹配");
                        throw new DataProcessException("模板格式不正确");
                    }
                }catch (Exception e){
                    closeInputStream(inputStream);
                    logger.severe("模板表头数据类型不匹配");
                    throw new DataProcessException("模板格式不正确");
                }

            }
        }
    }

    public  void closeInputStream(BufferedInputStream inputStream){
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public WorkBookProcess(BufferedInputStream in, ExcelImportConfig excelImportConfig) {
        this.in = in;
        this.excelImportConfig = excelImportConfig;
    }


}
