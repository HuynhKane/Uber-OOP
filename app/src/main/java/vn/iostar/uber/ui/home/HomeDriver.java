package vn.iostar.uber.ui.home;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.activitys.MainActivityDriver;
import vn.iostar.uber.activitys.client.ContactFormActivity;
import vn.iostar.uber.activitys.client.FinalBookingFormActivity;
import vn.iostar.uber.controllers.GeocodingHelper;
import vn.iostar.uber.controllers.YeuCauDatXeController;
import vn.iostar.uber.ui.map.MapFragment;

public class HomeDriver extends Fragment {

    private HomeDriverViewModel mViewModel;
    private YeuCauDatXeController yeuCauDatXeController=new YeuCauDatXeController();
    GeocodingHelper geocodingHelpe= new GeocodingHelper();
    public DatabaseReference myrefDriver;

    public class RealtimeArrayList<E> extends ArrayList<E> {
        private final Queue<E> queue = new LinkedList<>();
        private final Object lock = new Object();
        private final Lock wait = new ReentrantLock();
        private final Condition userActionCondition = wait.newCondition();
        private boolean isProcessing = false;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public boolean add(E e) {
            boolean result = super.add(e);
            if (result) {
                addToQueue(e);
            }
            return result;
        }

        @Override
        public void add(int index, E element) {
            super.add(index, element);
            addToQueue(element);
        }

        private void addToQueue(E e) {
            synchronized (lock) {
                queue.offer(e);
                if (!isProcessing) {
                    isProcessing = true;
                    new Thread(this::processQueue).start();
                }
            }
        }

        private void processQueue() {
            while (true) {
                E element;
                synchronized (lock) {
                    element = queue.poll();
                    if (element == null) {
                        isProcessing = false;
                        break;
                    }
                }
                try {
                    waitForUserChoice(element);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void waitForUserChoice(E e) throws InterruptedException {
            wait.lock();
            try {
                mHandler.post(() -> {
                    showCustomDialog((String) e);
                    mHandler.postDelayed(() -> {
                        wait.lock();
                        try {
                            userActionCondition.signal();
                        } finally {
                            wait.unlock();
                        }
                    }, 5000);
                });

                userActionCondition.await();
            } finally {
                wait.unlock();
            }
        }


    }
    RealtimeArrayList<String> listUserWait=new RealtimeArrayList<>();
    public static HomeDriver newInstance() {
        return new HomeDriver();
    }
    private MapFragment mapFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home_driver, container, false);

        // Add MapFragment vào MainFragment

        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);


       if(MainActivityDriver.curPos!=null){
           try {
               yeuCauDatXeController.listenClient(getContext(),MainActivityDriver.curPos, FirebaseAuth.getInstance().getUid(), new YeuCauDatXeController.Retriver_Client_DatabaseReference() {
                   @Override
                   public void onSuccess(String idClient,DatabaseReference myref) {
                       myrefDriver=myref;
                       Toast.makeText(getContext(),idClient,Toast.LENGTH_SHORT).show();
                       listUserWait.add(idClient);
                       Log.d("KHANH",listUserWait.toString());

//                    myref.removeEventListener(YeuCauDatXeController.valueEventListener );
//                    showCustomDialog(idClient,myref);

                   }

                   @Override
                   public void onFail() {

                   }
               });
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeDriverViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showCustomDialog(String idClient) {

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
        TextView countDown = dialogView.findViewById(R.id.countDown);
        LinearLayout nono = dialogView.findViewById(R.id.btn_nono);

        name.setText(idClient);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("Deny this client in: " + millisUntilFinished / 1000+ " s");

            }

            public void onFinish() {
                countDown.setText("Deny!");
                yeuCauDatXeController.denyThisClient(getContext(),MainActivityDriver.curPos, FirebaseAuth.getInstance().getUid(), idClient, new YeuCauDatXeController.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                });

                dialog.dismiss();
            }
        }.start();

        nono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myrefDriver.removeEventListener(YeuCauDatXeController.valueEventListener);
                    yeuCauDatXeController.acceptThisClient(getContext(), MainActivityDriver.curPos, FirebaseAuth.getInstance().getUid(), idClient, new YeuCauDatXeController.Callback() {
                        @Override
                        public void onSuccess() {
                            mapFragment.off();
                            Intent intent=new Intent(getContext(), ContactFormActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail() {

                        }});
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                 dialog.dismiss();
            }
        });

        // Hiển thị dialog
        dialog.show();
    }

}