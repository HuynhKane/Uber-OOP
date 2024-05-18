package vn.iostar.uber.controllers.callAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.iostar.uber.models.VehicleType;

public interface UuDaiController {
    @GET("uber/voucher/all")
    Call<ArrayList<VehicleType>> getListLoaiXe();

    @POST("uber/voucher/add")
    Call<VehicleType> addLoaiXe(@Body VehicleType loaiXe);
    @DELETE("uber/vehicle/type/delete/{id}")
    Call<VehicleType> deleteLoaiXe(@Path("id") int id);
}
