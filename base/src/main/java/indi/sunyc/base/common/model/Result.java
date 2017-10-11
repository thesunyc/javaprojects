package indi.sunyc.base.common.model;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class Result {

    private ResultError error;
    private Object result;

    private Result(){}

    public ResultError getError() {
        return error;
    }

    public Result setError(ResultError error) {
        this.error = error;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public Result setResult(Object result) {
        this.result = result;
        return this;
    }

    public Boolean isSuccess() {
        return error == null;
    }

    public static Result newSuccResult(){
        return new Result();
    }

    public static Result newSuccResult(Object result){
        return new Result().setResult(result);
    }

    public static Result newErrorResult(String code,String message){
        return new Result().setError(new ResultError(code,message));
    }
}
