package com.winteree.api.entity;

import lombok.Data;

import java.util.List;

/**
 * <p>Title: ListData</p>
 * <p>Description: 数据承载类</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:26
 */
@Data
public class ListData<T> {
    List<T> data;
    Long total;
}
