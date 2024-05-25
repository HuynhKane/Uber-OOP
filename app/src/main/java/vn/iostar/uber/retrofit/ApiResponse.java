package vn.iostar.uber.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vn.iostar.uber.models.UuDai;

public class ApiResponse<T> {
    @SerializedName("data")
    private ArrayList<T> data;
    @SerializedName("httpStatus")
    private String httpStatus;
    @SerializedName("message")
    private String message;

    public ApiResponse() {
        this.data = new ArrayList();
        this.httpStatus = "";
        this.message = "";
    }

    public ApiResponse(ArrayList<T> data, String httpStatus, String message) {
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
