package vn.iostar.uber.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.client.Map_TypeVehicalActivity;
import vn.iostar.uber.activitys.client.VoucherActivity;
import vn.iostar.uber.models.LoaiXe;

public class TypeVehicalAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<LoaiXe> List= new ArrayList<LoaiXe>();

    public TypeVehicalAdapter(Activity context, int resource, ArrayList<LoaiXe> list) {

        super(context, resource, list);
        this.resource=resource;
        List = list;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(resource, null);
        ImageView imgHinh = (ImageView)  customView.findViewById(R.id.icon);
        TextView txtTen =(TextView) customView.findViewById(R.id.ten);
        TextView txtgia =(TextView) customView.findViewById(R.id.gia);
        if(imgHinh!=null && txtTen!=null && txtgia!=null){
            txtTen.setText(List.get(position).getTenLoaiXe());
            txtgia.setText(List.get(position).getGia().toString()+" đ");
            String id=List.get(position).getIdLoaiXe();
            switch (id) {
                case "idbike": {
                    imgHinh.setImageResource(R.drawable.ic_bike);
                    break;
                }
                case "idcar": {
                    imgHinh.setImageResource(R.drawable.ic_car);
                    break;
                }
                case "idlimo": {
                    imgHinh.setImageResource(R.drawable.ic_limo);
                    break;
                }

            }
        }

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoucherActivity.class);
                Map_TypeVehicalActivity.loaiXe=List.get(position);
                Log.d("loaiXe",List.get(position).getTenLoaiXe() );
                context.startActivity(intent);
            }
        });
        return customView;
    }
}
