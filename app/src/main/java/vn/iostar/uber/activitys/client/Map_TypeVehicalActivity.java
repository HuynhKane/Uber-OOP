package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.adapters.TypeVehicalAdapter;
import vn.iostar.uber.controllers.LoaiXeController;
import vn.iostar.uber.models.LoaiXe;

public class Map_TypeVehicalActivity extends AppCompatActivity {

    ListView lv_type;
    ArrayList<LoaiXe> listTypeVehical=new ArrayList<>();
    TypeVehicalAdapter typeVehicalAdapter;
    public static LoaiXe loaiXe;
    LoaiXeController loaiXeController=new LoaiXeController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type_vehical);
        getForm();

    }

    private void getForm() {
        lv_type=findViewById(R.id.lv_type);
        listTypeVehical.clear();

        ProgressDialog progressDialog = new ProgressDialog(Map_TypeVehicalActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        loaiXeController.getListLoaiXe(new LoaiXeController.DataRetrievedCallback_LoaiXe() {
            @Override
            public void onDataRetrieved(ArrayList<LoaiXe> listLoaiXe) {
                listTypeVehical=listLoaiXe;
                typeVehicalAdapter=new TypeVehicalAdapter(Map_TypeVehicalActivity.this,R.layout.item_type_vehical,listTypeVehical);
                lv_type.setAdapter(typeVehicalAdapter);
                progressDialog.dismiss();
            }
        });
    }
}

