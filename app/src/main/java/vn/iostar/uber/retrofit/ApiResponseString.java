package vn.iostar.uber.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiResponseString<T> {
    @SerializedName("data")
    private T data;
    @SerializedName("httpStatus")
    private String httpStatus;
    @SerializedName("message")
    private String message;

    public ApiResponseString() {

        this.httpStatus = "";
        this.message = "";
    }

    public ApiResponseString(T data, String httpStatus, String message) {
        this.data = data;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T   data) {
        this.data = data;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
