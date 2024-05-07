package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.adapters.TypeVehicalAdapter;
import vn.iostar.uber.models.LoaiXe;

public class Map_TypeVehical extends AppCompatActivity {

    ListView lv_type;
    ArrayList<LoaiXe> listTypeVehical=new ArrayList<>();
    TypeVehicalAdapter typeVehicalAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type_vehical);
        getForm();

    }

    private void getForm() {
        lv_type=findViewById(R.id.lv_type);

        listTypeVehical.add(new LoaiXe("idbike","Bike","10.000đ"));
        listTypeVehical.add(new LoaiXe("idcar","Car","20.000đ"));
        listTypeVehical.add(new LoaiXe("idlimo","Limo","50.000đ"));
        typeVehicalAdapter=new TypeVehicalAdapter(Map_TypeVehical.this,R.layout.item_type_vehical,listTypeVehical);
        lv_type.setAdapter(typeVehicalAdapter);


    }
}