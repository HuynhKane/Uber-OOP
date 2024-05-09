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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.UuDai;

public class VoucherAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<UuDai> List= new ArrayList<UuDai>();
    public VoucherAdapter(Context context, int resource, ArrayList<UuDai> list) {
        super(context, resource, list);
        List = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_voucher, null);
        TextView txtUudDai = (TextView) customView.findViewById(R.id.txt_discount);
        TextView txtMoTa = (TextView) customView.findViewById(R.id.txt_moTa);
        TextView txtHSD = (TextView) customView.findViewById(R.id.txt_hsd);
        ImageView icon = (ImageView) customView.findViewById(R.id.ic_voucher);
        String id = List.get(position).getIdUuDai();
        txtUudDai.setText(List.get(position).getUuDai());
        txtHSD.setText(String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(List.get(position).getThoiGian())));
        txtMoTa.setText(List.get(position).getMoTa());
        icon.setImageResource(R.drawable.ic_voucher);
        return customView;
    }
}
