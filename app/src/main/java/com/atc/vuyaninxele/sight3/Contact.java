package com.atc.vuyaninxele.sight3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by vuyani.nxele on 2018/03/09.
 */

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Contact.this, MenuActivity.class));
        finish();

        return super.onOptionsItemSelected(item);
    }
}
