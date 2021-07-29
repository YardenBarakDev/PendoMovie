package com.ybdev.pendomovie.mvvm.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ybdev.pendomovie.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

}