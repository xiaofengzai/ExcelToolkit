package com.data.core.excel.export;

import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.utils.CollectionUtil;
import com.data.core.excel.SheetHeader;
import lombok.Data;

import java.util.List;

@Data
public class SheetConfig {
    private List<SheetHeader> headers;
    private List<CellDataTypeConfig> cellDataTypeConfigs;
    private Integer sheetIndex=0;
    private Integer lineSize;
    private String sheetNamePattern="第%d页";
    public String getSheetName(){
        return String.format(sheetNamePattern,sheetIndex+1);
    }
    public boolean needSortNumber(){
        return getDataLabelsLength()-cellDataTypeConfigs.size()==1;
    }

    public Integer getDataLabelsLength(){

        return CollectionUtil.isEmpty(headers)?0:headers.get(headers.size()-1).getHeaderLabels().length;
    }

}
