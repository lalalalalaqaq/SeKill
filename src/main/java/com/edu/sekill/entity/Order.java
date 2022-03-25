package com.edu.sekill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-23
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain=true)
public class Order {
    private Integer id;
    private Integer sid;
    private String name;
    private Date createtime;
}
