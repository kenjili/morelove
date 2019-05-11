package wjy.weiai7lv.exception;

public class WebException extends Exception{

    private String message;
    private Throwable throwable;

    public WebException(String message){
        super(message);
        this.message = message;
    }

    public WebException(Throwable throwable){
        super(throwable);
        this.throwable = throwable;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
