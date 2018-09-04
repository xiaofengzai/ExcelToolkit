package com.data.core.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CellDataTypeConfig {
    private String  key;
    /**
     * required
     */
    private Integer emptyAction;
    private Integer dataType;
    private Integer actionType;
    private String defaultValue;
    private String numberFormatPattern;

    public CellDataTypeConfig(String key) {

    }

    public CellDataTypeConfig(String key, Integer emptyAction) {
        this.key = key;
        this.emptyAction = emptyAction;
    }

    public CellDataTypeConfig(String key, Integer emptyAction, Integer dataType,Integer actionType) {
        this(key,emptyAction,dataType,actionType,null);
    }

    public CellDataTypeConfig(String key, Integer emptyAction, Integer dataType,Integer actionType,String defaultValue) {
        this(key,emptyAction,dataType,actionType,defaultValue,null);
    }

    public CellDataTypeConfig(String key, Integer emptyAction, Integer dataType, Integer actionType, String defaultValue, String numberFormatPattern) {
        this.key = key;
        this.emptyAction = emptyAction;
        this.dataType = dataType;
        this.actionType = actionType;
        this.defaultValue = defaultValue;
        this.numberFormatPattern = numberFormatPattern;
    }
}
