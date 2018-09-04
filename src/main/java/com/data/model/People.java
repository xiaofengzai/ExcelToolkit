package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class People {
    private String name;
    private Integer age;
    private String phone;
    private String address;
    private Date birthday;
}
