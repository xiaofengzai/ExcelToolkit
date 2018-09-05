package com.data.core.excel.export;

import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.DataProcessException;
import com.data.core.excel.SheetHeader;
import com.data.core.excel.utils.CollectionUtil;
import lombok.Data;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SheetConfig {
    private List<SheetHeader> headers;
    private List<CellDataTypeConfig> cellDataTypeConfigs;
    private Integer sheetIndex=0;
    private Integer lineSize;
    private String sheetNamePattern="第%d页";
    private List<Integer[]> mergeRegions;
    private Integer dataLabelsLength=0;
    private Map<Integer,Map<Integer,Integer>> rowSkipCellMap=new HashMap<>();
    public List<CellRangeAddress> getMergeRegions(){
        if(!CollectionUtil.isEmpty(mergeRegions)){
            List<CellRangeAddress>  list=new ArrayList<>();
            for (int i=0;i<mergeRegions.size();i++){
                Integer[] mergeArea=mergeRegions.get(i);
                if(mergeArea.length!=4 || mergeArea[0]>mergeArea[1] || mergeArea[2]>mergeArea[3])
                    throw new DataProcessException("合并区域设置不正确");
                list.add(new CellRangeAddress(mergeArea[0],mergeArea[1],mergeArea[2],mergeArea[3]));
            }
            return list;
        }
        return  null;
    }

    public SheetConfig setHeaders(List<SheetHeader> headers) {
        this.headers = headers;
        if(!CollectionUtil.isEmpty(headers)){
            dataLabelsLength=headers.get(headers.size()-1).getHeaderLabels().length;
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

        return CollectionUtil.isEmpty(headers)?0:headers.get(headers.size()-1).getHeaderLabels().length;
    }

    public  SheetConfig addMergeRegions(List<Integer[]> mergeRegions){
        this.mergeRegions=mergeRegions;
        if(!CollectionUtil.isEmpty(mergeRegions)) {
            initRowSkipCellMap(mergeRegions);
        }
        return this;
    }

    private void initRowSkipCellMap(List<Integer[]> mergeRegions){
        for (int i=0,length=mergeRegions.size();i<length;i++){
            Integer[] mergeArea=mergeRegions.get(i);
            Integer startRow=mergeArea[0],endRow=mergeArea[1];
            for (int j=startRow;j<=endRow;j++){
                Map<Integer,Integer> rowMap=new HashMap<>();
                if(rowSkipCellMap.containsKey(j)){
                    rowMap=rowSkipCellMap.get(j);
                }else{
                    rowSkipCellMap.put(j,rowMap);
                }
                
            }

        }
    }

}
