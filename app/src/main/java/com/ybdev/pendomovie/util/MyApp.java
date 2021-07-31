package com.ybdev.pendomovie.util;

import android.app.Application;

import com.ybdev.pendomovie.retrofit.MyRetrofit;
import com.ybdev.pendomovie.room_db.MyRoomDB;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyRoomDB.init(this); //initiate room db
        MyRetrofit.getInstance(); //initiate retrofit
    }
}