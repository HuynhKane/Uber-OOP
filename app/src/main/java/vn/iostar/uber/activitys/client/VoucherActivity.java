package vn.iostar.uber.activitys.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.adapters.VoucherAdapter;
import vn.iostar.uber.controllers.callAPI.UuDaiController;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.retrofit.ApiResponseArList;
import vn.iostar.uber.retrofit.ApiResponseString;
import vn.iostar.uber.retrofit.RetrofitService;

public class VoucherActivity  extends AppCompatActivity {

    ListView lv_voucher;
    ArrayList<UuDai> listVoucher =new ArrayList<>();
    VoucherAdapter voucherAdapter;
    LinearLayout btn_next; SearchView searchView;
    public static UuDai uuDai;
    public static  boolean isChoose=false;


    RetrofitService retrofitService=new RetrofitService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        getForm();

    }

    private void getForm() {
        lv_voucher=findViewById(R.id.lv_voucher);
        LinearLayout btn_x=findViewById(R.id.x);
        listVoucher.clear();
        ProgressDialog progressDialog = new ProgressDialog(VoucherActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        UuDaiController uuDaiController = retrofitService.getRetrofit().create(UuDaiController.class);

        uuDaiController.getListUuDai().enqueue(new Callback<ApiResponseArList<UuDai>>() {
            @Override
            public void onResponse(Call<ApiResponseArList<UuDai>> call, Response<ApiResponseArList<UuDai>> response) {
                assert response.body() != null;
                if(response.body().getHttpStatus().equals("OK")){
                    listVoucher = response.body().getData();
                    voucherAdapter = new VoucherAdapter(VoucherActivity.this,R.layout.item_voucher, listVoucher);
                    lv_voucher.setAdapter(voucherAdapter);
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(VoucherActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponseArList<UuDai>> call, Throwable throwable) {

            }
        });
//        uuDaiController.getListUuDai(new UuDaiController.DataRetrievedCallback_UuDai() {
//            @Override
//            public void onDataRetrieved(ArrayList<UuDai> listUuDai) {
//                listVoucher = listUuDai;
//                voucherAdapter = new VoucherAdapter(VoucherActivity.this,R.layout.item_voucher, listVoucher);
//                lv_voucher.setAdapter(voucherAdapter);
//                progressDialog.dismiss();
//            }
//        } );



        btn_next= findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChoose){isChoose=false;
                    UuDaiController uuDaiController = retrofitService.getRetrofit().create(UuDaiController.class);

                    uuDaiController.chooseVoucher(uuDai.getIdUuDai()).enqueue(new Callback<ApiResponseString<String>>() {
                        @Override
                        public void onResponse(Call<ApiResponseString<String>> call, Response<ApiResponseString<String>> response) {
                            Toast.makeText(VoucherActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ApiResponseString<String>> call, Throwable throwable) {
                            //Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(VoucherActivity.this, ChooseTypePaymentActivity.class);
                    Log.d("uuDaiiiiii",uuDai.getUuDai().toString() );
                    startActivity(intent);

                }
                else {
                    Toast.makeText(VoucherActivity.this,"Vui loòng chọn 1 voucher",Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchView=findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<UuDai> temp=new ArrayList<UuDai>();
                for(UuDai ud: listVoucher){
                    if(ud.getUuDai().toLowerCase().contains(newText.toLowerCase()) ||ud.getMoTa().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(ud);
                    }
                }
                voucherAdapter = new VoucherAdapter(VoucherActivity.this,R.layout.item_voucher, temp);
                lv_voucher.setAdapter(voucherAdapter);
                voucherAdapter.notifyDataSetChanged();
                return false;

            }
        });

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VoucherActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}