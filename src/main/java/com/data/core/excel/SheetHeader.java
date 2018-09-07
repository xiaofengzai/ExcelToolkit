package com.data.core.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
public class SheetHeader {
    private LinkedList<String> headerLabels=new LinkedList<>();
    private HorizontalAlignment alignType= HorizontalAlignment.LEFT;

    public SheetHeader(LinkedList<String> headerLabels, HorizontalAlignment alignType) {
        this.headerLabels = headerLabels;
        this.alignType = alignType;
    }

    public SheetHeader(String ... headerLabels) {
        this.headerLabels.addAll(Arrays.asList(headerLabels));
    }
}
