package vn.iostar.uber.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.client.FinalBookingFormActivity;
import vn.iostar.uber.controllers.YeuCauDatXeController;

public class HomeDriver extends Fragment {

    private HomeDriverViewModel mViewModel;
    private YeuCauDatXeController yeuCauDatXeController=new YeuCauDatXeController();

    public static HomeDriver newInstance() {
        return new HomeDriver();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        yeuCauDatXeController.listenClient("Dĩ An", "Uz4J0EoWBoNXIcIIjebjT36c91y2", new YeuCauDatXeController.Retriver_Client() {
            @Override
            public void onSuccess(String idClient) {
                Toast.makeText(getContext(),idClient,Toast.LENGTH_SHORT).show();
                Log.d("KHANH",idClient);
                showCustomDialog(idClient);
            }

            @Override
            public void onFail() {

            }
        });
        return inflater.inflate(R.layout.fragment_home_driver, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeDriverViewModel.class);
        // TODO: Use the ViewModel
    }
    private void showCustomDialog(String idClient) {
        // Tạo dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate layout cho dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.element_driver_wait, null);
        builder.setView(dialogView);

        // Tạo dialog từ builder
        AlertDialog dialog = builder.create();

        // Lấy các view từ layout dialog
        TextView name = dialogView.findViewById(R.id.txtName);
        TextView distance = dialogView.findViewById(R.id.txtkm);
        Button ok = dialogView.findViewById(R.id.btn_ok);
        Button nono = dialogView.findViewById(R.id.btn_nono);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        nono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        dialog.show();
    }

}