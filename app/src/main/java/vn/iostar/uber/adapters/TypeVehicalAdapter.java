package vn.iostar.uber.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.models.LoaiXe;

public class TypeVehicalAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<LoaiXe> List= new ArrayList<LoaiXe>();

    public TypeVehicalAdapter(Context context, int resource, ArrayList<LoaiXe> list) {
        super(context, resource, list);
        List = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_type_vehical, null);
        ImageView imgHinh = (ImageView)  customView.findViewById(R.id.icon);
        TextView txtTen =(TextView) customView.findViewById(R.id.ten);
        TextView txtgia =(TextView) customView.findViewById(R.id.gia);
        txtTen.setText(List.get(position).getTenLoaiXe());
        txtgia.setText(List.get(position).getGia());
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
        return customView;
    }
}
