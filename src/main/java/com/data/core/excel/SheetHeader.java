package com.data.core.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SheetHeader {
    private String[] headerLabels;
    private Integer alignType;
    private Integer[][] mergeRegions;
    public List<CellRangeAddress> getMergeRegions(){
        if(mergeRegions!=null && mergeRegions.length>0){
            List<CellRangeAddress>  list=new ArrayList<>();
            for (int i=0;i<mergeRegions.length;i++){
                Integer[] mergeArea=mergeRegions[i];
                if(mergeArea.length!=4)
                    throw new DataProcessException("合并区域设置不正确");
                list.add(new CellRangeAddress(mergeArea[0],mergeArea[1],mergeArea[2],mergeArea[3]));
            }
            return list;
        }
        return  null;
    }
}
