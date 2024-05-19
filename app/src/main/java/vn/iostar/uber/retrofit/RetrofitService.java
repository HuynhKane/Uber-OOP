package vn.iostar.uber.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(){
        initialRetrofit();
    }
    private void initialRetrofit(){
         retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.2.60:8080/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}