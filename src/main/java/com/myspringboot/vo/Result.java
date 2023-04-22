package com.myspringboot.vo;

public class Result<T> {
    private boolean isSuccess = true;
    private String message;
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Result<T> setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
