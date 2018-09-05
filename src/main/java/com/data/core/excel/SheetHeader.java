package com.data.core.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SheetHeader {
    private String[] headerLabels;
    private Integer alignType;
}
