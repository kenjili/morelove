package wujiuye.morelove.api.tasks;

public interface EmailTaskService {


    void sendEmailCode(String email, String code);

    void sendActivationLink(String email, String link);
}
