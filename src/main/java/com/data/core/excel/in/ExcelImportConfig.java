package com.data.core.excel.in;

import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.DataProcessException;
import com.data.core.excel.export.SheetHeader;
import lombok.Data;

import java.util.List;

/**
 * Created by szty on 2018/9/3.
 */
@Data
public class ExcelImportConfig {
    /**
     * 导入操作ID
     */
    private String oid;
    /**
     * 导入操作的业务ID
     */
    private String bid;

    private Integer startRowIndex=1;

    private List<SheetHeader> headers;

    private Boolean allowBlankRows=true;

    private Integer batchSaveSize=1000;

    private List<CellDataTypeConfig> cellDataTypeConfigs;

    public ExcelImportConfig(String oid, String bid) {
        this.oid = oid;
        this.bid = bid;
    }

    public ExcelImportConfig addHeaders(List<SheetHeader> headers){
        if(headers==null)
            throw new DataProcessException("请设置表头");
        this.startRowIndex=headers.size();
        this.headers=headers;
        return this;
    }
    public ExcelImportConfig addCellDataTypeConfigs(List<CellDataTypeConfig> cellDataTypeConfigs){
        this.cellDataTypeConfigs=cellDataTypeConfigs;
        return this;
    }

    public ExcelImportConfig disableBlankRow(){
        this.allowBlankRows=false;
        return this;
    }



}
