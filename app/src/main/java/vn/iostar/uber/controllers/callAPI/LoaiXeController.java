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


public interface LoaiXeController {
    @GET("uber/vehicle/type/all")
    Call<ArrayList<LoaiXe>> getListLoaiXe();

    @POST("uber/vehicle/type/add")
    Call<LoaiXe> addLoaiXe(@Body LoaiXe loaiXe);

    @DELETE("uber/vehicle/type/delete/{id}")
    Call<Void> deleteLoaiXe(@Path("id") String id);

}
