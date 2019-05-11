package com.wujiuye.weiai7lv.exception;

import lombok.Data;

/**
 * 自定义接口返回类型，用于统一返回json数据的格式
 */
@Data
public class WebResult {

    private Integer errorCode;
    private String errorMessage;
    private Object data;
}
