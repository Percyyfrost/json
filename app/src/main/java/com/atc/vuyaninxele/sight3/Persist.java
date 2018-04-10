package com.atc.vuyaninxele.sight3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vuyani.nxele on 2018/03/06.
 */

public class Persist extends SplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
