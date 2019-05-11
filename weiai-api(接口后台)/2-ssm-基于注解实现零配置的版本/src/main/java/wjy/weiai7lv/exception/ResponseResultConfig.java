package wjy.weiai7lv.exception;

public class ResponseResultConfig {

    public enum ResponseResult{
        EXCEPTION(-1,"出现异常！"),
        SUCCESS(0,"操作成功！"),
        NOTLOGIN(1,"未登录！"),
        NOTPERM(2,"没有权限"),
        NOTPERM_BINDLOVER(3,"没有权限，尚未成功绑定情侣关系！"),
        NOTPERM_ADMIN(4,"没有权限，不是管理员！");

        private int errorCode;
        private String errorMessage;

        ResponseResult(int errorCode,String errorMessage){
            this.errorCode=errorCode;
            this.errorMessage=errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }

    public final static WebResult getResponseResult(ResponseResult result){
        WebResult modelMap = new WebResult();
        modelMap.setErrorCode(result.getErrorCode());
        modelMap.setErrorMessage(result.getErrorMessage());
        return modelMap;
    }

}
