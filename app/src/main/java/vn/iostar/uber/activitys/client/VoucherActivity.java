package vn.iostar.uber.activitys.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.adapters.VoucherAdapter;
import vn.iostar.uber.controllers.UuDaiController;
import vn.iostar.uber.models.UuDai;

public class VoucherActivity  extends AppCompatActivity {

    ListView lv_voucher;
    ArrayList<UuDai> listVoucher =new ArrayList<>();
    VoucherAdapter voucherAdapter;
    LinearLayout btn_next; SearchView searchView;
    public static UuDai uuDai;
    UuDaiController uuDaiController=new UuDaiController();
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
        uuDaiController.getListUuDai(new UuDaiController.DataRetrievedCallback_UuDai() {
            @Override
            public void onDataRetrieved(ArrayList<UuDai> listUuDai) {
                listVoucher = listUuDai;
                voucherAdapter = new VoucherAdapter(VoucherActivity.this,R.layout.item_voucher, listVoucher);
                lv_voucher.setAdapter(voucherAdapter);
                progressDialog.dismiss();
            }
        } );


        btn_next= findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoucherActivity.this, ChooseTypePaymentActivity.class);
                Log.d("uuDaiiiiii",uuDai.getUuDai().toString() );
                startActivity(intent);
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