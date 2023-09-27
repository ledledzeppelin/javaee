package edu.whu.framework;

/**
 * 对框架的各种异常进行封装，告诉用户更明确的出错原因
 */
public class BootstrapException extends Exception {

    public enum ErrorType {FILE_NOTFOUND,PROP_READ_ERROR,CLASS_NOTFOUND,INITMETHOD_ERROR,CREATE_OBJECT_ERROR,METHOD_CALL_ERROR }
    private ErrorType errorType;

    public BootstrapException(ErrorType errorType, String message){
        super(message);
        this.errorType =errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

}
