package com.data.core.excel;

import lombok.Data;

@Data
public class DataProcessException extends RuntimeException {
    private Integer type;
    public DataProcessException(String message) {
        super(message);
        this.type=null;
    }

    public DataProcessException(String message, Integer type) {
        super(message);
        this.type = type;
    }
}
