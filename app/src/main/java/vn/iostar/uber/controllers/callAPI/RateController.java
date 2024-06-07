package vn.iostar.uber.controllers.callAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.iostar.uber.models.DanhGia;
import vn.iostar.uber.models.UuDai;

public interface RateController {
    @GET("uber/rate/{iddriver}")
    Call<ArrayList<DanhGia>> getListUuDaiByDriver(@Path("iddriver") int id);

    @POST("uber/rate/add")
    Call<DanhGia> addUuDai(@Body DanhGia danhGia);
    @DELETE("uber/rate/delete/{id}")
    Call<DanhGia> deleteUuDai(@Path("id") int id);


}
