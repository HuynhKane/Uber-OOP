package vn.iostar.uber.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

}