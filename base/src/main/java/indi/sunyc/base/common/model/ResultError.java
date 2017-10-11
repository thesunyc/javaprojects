package indi.sunyc.base.common.model;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class ResultError {

    private String code;
    private String message;

    public ResultError(String code,String message){
        this.setCode(code);
        this.setMessage(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
