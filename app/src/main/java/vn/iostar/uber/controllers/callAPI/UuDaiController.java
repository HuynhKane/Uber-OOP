package vn.iostar.uber.controllers.callAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.retrofit.ApiResponseArList;
import vn.iostar.uber.retrofit.ApiResponseString;

public interface UuDaiController {
    @GET("uber/voucher/all")
    Call<ApiResponseArList<UuDai>> getListUuDai();
    @PUT("uber/voucher/choose/{idVoucher}")
    Call<ApiResponseString<String>> chooseVoucher(@Path("idVoucher") String idVoucher);
    @PUT("uber/voucher/canclechoose/idVoucher")
    Call<ApiResponseArList<String>> canclechooseVoucher(@Path("idVoucher") String idVoucher);

    @POST("uber/voucher/add")
    Call<UuDai> addUuDai(@Body UuDai uuDai);
    @DELETE("uber/voucher/delete/{id}")
    Call<UuDai> deleteUuDai(@Path("id") int id);
}
