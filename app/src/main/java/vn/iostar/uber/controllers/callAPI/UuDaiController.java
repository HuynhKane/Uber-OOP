package vn.iostar.uber.controllers.callAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.models.VehicleType;
import vn.iostar.uber.retrofit.ApiResponse;

public interface UuDaiController {
    @GET("uber/voucher/all")
    Call<ApiResponse<UuDai>> getListUuDai();

    @PUT("uber/voucher/choose/{idVoucher}")
    Call<ApiResponse<UuDai>> chooseVoucher(@Path("id") String idVoucher);
    @PUT("uber/voucher/canclechoose/{idVoucher}")
    Call<ApiResponse<UuDai>> canclechooseVoucher(@Path("id") String idVoucher);

    @POST("uber/voucher/add")
    Call<UuDai> addUuDai(@Body UuDai uuDai);
    @DELETE("uber/voucher/delete/{id}")
    Call<UuDai> deleteUuDai(@Path("id") int id);
}
