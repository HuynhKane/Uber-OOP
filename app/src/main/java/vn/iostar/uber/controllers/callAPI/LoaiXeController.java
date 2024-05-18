package vn.iostar.uber.controllers.callAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.VehicleType;

public interface LoaiXeController {
    @GET("uber/vehicle/type/all")
    Call<ArrayList<VehicleType>> getListLoaiXe();

    @POST("uber/vehicle/type/add")
    Call<VehicleType> addLoaiXe(@Body VehicleType loaiXe);

    @DELETE("uber/vehicle/type/delete/{id}")
    Call<Void> deleteLoaiXe(@Path("id") String id);

}
