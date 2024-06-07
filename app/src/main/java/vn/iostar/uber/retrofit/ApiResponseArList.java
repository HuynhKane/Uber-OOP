package vn.iostar.uber.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiResponseArList<T> {
    @SerializedName("data")
    private ArrayList<T> data;
    @SerializedName("httpStatus")
    private String httpStatus;
    @SerializedName("message")
    private String message;

    public ApiResponseArList() {
        this.data = new ArrayList();
        this.httpStatus = "";
        this.message = "";
    }

    public ApiResponseArList(ArrayList<T> data, String httpStatus, String message) {
        this.data = data;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // Getters and Setters
    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
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
