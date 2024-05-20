package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import vn.iostar.uber.R;

public class ContactFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ImageView call = (ImageView) findViewById(R.id.btn_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ContactFormActivity.this);
                alert.setTitle("Call");
                alert.setMessage("Nhập số điện thoại");
// Set an EditText view to get user input
                final EditText input = new EditText(ContactFormActivity.this );
                alert.setView(input);
                alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                    String value = String.valueOf(input.getText());
                    // Do something with value!
                    Intent callintent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+value));
                    //Yeu cau allow to call
                    if (ActivityCompat.checkSelfPermission(ContactFormActivity.this,
                            android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(ContactFormActivity.this,new String[]{android.Manifest.permission.CALL_PHONE},1);
                        return;
                    }
                    startActivity(callintent);
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

            }
        });
    }
}