package vn.iostar.uber.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import vn.iostar.uber.activitys.client.VoucherActivity;
import vn.iostar.uber.models.UuDai;

public class VoucherAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<UuDai> List= new ArrayList<UuDai>();
    public VoucherAdapter(Activity context, int resource, ArrayList<UuDai> list) {
        super(context, resource, list);
        this.context=context;
        List = list;
    }
    private int selectedItemPosition = -1;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_voucher, null);
        TextView txtUudDai = (TextView) customView.findViewById(R.id.txt_discount);
        TextView txtMoTa = (TextView) customView.findViewById(R.id.txt_moTa);
        TextView txtHSD = (TextView) customView.findViewById(R.id.txt_hsd);
        ImageView check=customView.findViewById(R.id.check);
        ImageView icon = (ImageView) customView.findViewById(R.id.ic_voucher);
        String id = List.get(position).getIdUuDai();
        txtUudDai.setText(List.get(position).getUuDai());
        txtHSD.setText(String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(List.get(position).getThoiGian())));
        txtMoTa.setText(List.get(position).getMoTa());
        icon.setImageResource(R.drawable.ic_voucher);

        final View finalListItemView = customView;

        if(selectedItemPosition==-1){
            finalListItemView.setBackgroundColor(Color.TRANSPARENT);
            check.setImageResource(R.drawable.ic_check);
        }
        else {
            if(selectedItemPosition==position){
                finalListItemView.setBackgroundColor(Color.TRANSPARENT);
                check.setImageResource(R.drawable.ic_checked);
            }
            else {
                finalListItemView.setBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
                check.setImageResource(R.drawable.ic_check);
            }
        }


        btnAddVoucher_click(customView,position);//****
        return customView;
    }

    private void btnAddVoucher_click(View customView, int position) {
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition = position;
                VoucherActivity.uuDai=List.get(position);
                VoucherActivity.isChoose=true;
                notifyDataSetChanged();
            }
        });
    }
}
