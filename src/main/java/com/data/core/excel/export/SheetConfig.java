package com.data.core.excel.export;

import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.DataProcessException;
import com.data.core.excel.SheetHeader;
import com.data.core.excel.utils.CollectionUtil;
import lombok.Data;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

@Data
public class SheetConfig {
    private List<SheetHeader> headers;
    private List<CellDataTypeConfig> cellDataTypeConfigs;
    private Integer sheetIndex=0;
    private Integer lineSize;
    private Integer rowMaxWidth;
    private String sheetNamePattern="第%d页";
    private List<CellRangeAddress> mergeRegions=new ArrayList<>();
    private Integer dataLabelsLength=0;

    public SheetConfig setHeaders(List<SheetHeader> headers) {
        this.headers = headers;
        if(!CollectionUtil.isEmpty(headers)){
            dataLabelsLength=headers.get(headers.size()-1).getHeaderLabels().size();
        }
        return this;
    }

    public String getSheetName(){
        return String.format(sheetNamePattern,sheetIndex+1);
    }
    public boolean needSortNumber(){
        return dataLabelsLength-cellDataTypeConfigs.size()==1;
    }

    public Integer getDataLabelsLength(){

        return CollectionUtil.isEmpty(headers)?0:headers.get(headers.size()-1).getHeaderLabels().size();
    }

    public  SheetConfig addMergeRegions(String mergeLabelName,Integer ... regions){
        if(regions.length!=4)
            throw new DataProcessException("合并区设置不正确");
        if(CollectionUtil.isEmpty(headers))
            throw new DataProcessException("请先设置headers");
        mergeLabel(mergeLabelName,regions);
        if(regions.length!=4 || regions[0]>regions[1] || regions[2]>regions[3])
            throw new DataProcessException("合并区域设置不正确");
        this.mergeRegions.add(new CellRangeAddress(regions[0],regions[1],regions[2],regions[3]));
        return this;
    }

    private void mergeLabel(String mergeLabelName,Integer ... regions){
        Integer startRow=regions[0],endRow=regions[1];
        for (int j=startRow;j<=endRow;j++){
            SheetHeader header=headers.get(j);
            if(header==null)
                continue;
            Integer startCell=regions[2],endCel=regions[3];
            for (int k=startCell;k<=endCel;k++){
                header.getHeaderLabels().add(k, mergeLabelName);
            }
        }
    }
}
