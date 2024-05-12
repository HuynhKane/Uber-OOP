package vn.iostar.uber.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import vn.iostar.uber.R;
import vn.iostar.uber.activitys.client.RegisterClientActivity;
import vn.iostar.uber.activitys.driver.RegisterDriverActivity;
import vn.iostar.uber.controllers.TaiKhoanController;

public class HomeActivity extends AppCompatActivity {


    private final static int LOGIN_REQUEST_CODE = 7171;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    public static String role="";
    public TaiKhoanController taiKhoanController=new TaiKhoanController();
    private FirebaseAuth.AuthStateListener listener;
    private ProgressDialog progressDialog ;
    protected void onStart() {
        super.onStart();
        delaySplashScreen();
    }

    @Override
    protected void onStop() {

        if(firebaseAuth !=  null && listener != null){
            firebaseAuth.removeAuthStateListener(listener);
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        listener = myFirebaseAuth -> {
            FirebaseUser user = myFirebaseAuth.getCurrentUser();

            if(user != null){
               // progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Welcome"+user.getUid(), Toast.LENGTH_SHORT).show();
                taiKhoanController.SaveAcc(role);
                if(role.equals("client"))
                    taiKhoanController.CheckNum(new TaiKhoanController.DataRetrievedCallback_Bool() {
                        @Override
                        public void onDataRetrieved(boolean num) {
                            if(num){
                                Intent intent=new Intent(HomeActivity.this, RegisterClientActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                startActivity(new Intent(HomeActivity.this, MainActivityClient.class));
                            }
                        }
                    });
                else {
                    taiKhoanController.CheckDriverInf(new TaiKhoanController.DataRetrievedCallback_Bool() {
                        @Override
                        public void onDataRetrieved(boolean num) {
                            if(num){
                                Intent intent=new Intent(HomeActivity.this, RegisterDriverActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                startActivity(new Intent(HomeActivity.this, MainActivityDriver.class));
                            }
                        }
                    });

                }


            }
            else{
             //   progressDialog.dismiss();
                handleClickLoginRole();
            }
        };

    }
    private void handleClickLoginRole(){
        setContentView(R.layout.activity_home);
        //TextView btnLogin = findViewById(R.id.btn_login);
        LinearLayout btnLogin_client=findViewById(R.id.btn_login_client);
        LinearLayout btnLogin_driver=findViewById(R.id.btn_login_driver);

        btnLogin_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role="client";
                showLoginDialog();
            }
        });
        btnLogin_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role="driver";
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_sign_in)
                .setPhoneButtonId(R.id.btn_phone_sign_in)
                .setGoogleButtonId(R.id.btn_google_sign_in)
                .build();
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setIsSmartLockEnabled(false)
                .setAlwaysShowSignInMethodScreen(true)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .build(),LOGIN_REQUEST_CODE);

    }


    @SuppressLint("CheckResult")
    private void delaySplashScreen() {

        Completable.timer(3, TimeUnit.SECONDS,
                        AndroidSchedulers.mainThread())
                .subscribe(() ->  firebaseAuth.addAuthStateListener(listener));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            }
            else{
                Toast.makeText(this,"[ERROR]:" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}