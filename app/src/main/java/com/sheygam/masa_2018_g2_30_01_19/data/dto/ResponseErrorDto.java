package com.sheygam.masa_2018_g2_30_01_19.data.dto;

public class ResponseErrorDto {
    private int code;
    private String message;

    public ResponseErrorDto() {
    }

    public ResponseErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
