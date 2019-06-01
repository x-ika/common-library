package com.simplejcode.commons.misc;

public class HttpResponse {

    private int responseCode;

    private String response;

    private String error;


    public HttpResponse(int responseCode, String response, boolean ok) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public HttpResponse(int responseCode, String error) {
        this.responseCode = responseCode;
        this.error = error;
    }


    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
