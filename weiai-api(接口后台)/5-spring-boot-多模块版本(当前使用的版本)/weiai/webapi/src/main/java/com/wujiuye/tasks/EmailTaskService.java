package com.wujiuye.tasks;

public interface EmailTaskService {


    void sendEmailCode(String email, String code);

    void sendActivationLink(String email, String link);
}
