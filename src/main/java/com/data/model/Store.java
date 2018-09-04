package com.data.model;

import lombok.Data;

/**
 * Created by szty on 2018/9/4.
 */
@Data
public class Store {
    private Integer no;
    private String  storeName;
    private String storeNo;
    private String provinceId;
    private String cityId;
    private String regionId;
    private String address;
    private String deviceId;
}
