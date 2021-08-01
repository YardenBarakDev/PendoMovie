package com.ybdev.pendomovie.mvvm.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.util.NetworkConnectivityCheck;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check network connectivity
        if (!NetworkConnectivityCheck.checkNetworkConnectivity(this)){
            showDialog();
            Toast.makeText(this,"No network connection", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * present a dialog in case there is a network issue
     */
    private void showDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Oops it looks like there is a problem")
                .setMessage("Please make sure you are connected to the network and try again")
                .setIcon(R.drawable.app_icon)
                .setPositiveButton("Ok", (dialog, which) -> /* no op*/ {})
                .show();
    }
}