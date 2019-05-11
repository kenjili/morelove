package wjy.weiai7lv.tasks;

public interface EmailTaskService {


    void sendEmailCode(String email,String code);

    void sendActivationLink(String email,String link);
}
