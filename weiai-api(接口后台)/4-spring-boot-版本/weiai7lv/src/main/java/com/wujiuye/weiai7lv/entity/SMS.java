package com.wujiuye.weiai7lv.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 短信验证码，用于注册验证与找回密码验证
 * @author wjy
 */
@Data
public class SMS implements Serializable {

    private String phoneNumber;//手机号
    private String code;//验证码
    private long sendDateTime;//发送时间，单位为秒
    private int overdue;//有效期，单位为秒
}
