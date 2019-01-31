package com.sheygam.masa_2018_g2_30_01_19.data.dto;

public class AuthResponseDto {
    private String token;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
