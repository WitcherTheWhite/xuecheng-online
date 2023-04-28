package com.xuecheng.base.exception;

public class XueChengPlusException extends RuntimeException {

    private String errMessage;

    public XueChengPlusException() {}

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
