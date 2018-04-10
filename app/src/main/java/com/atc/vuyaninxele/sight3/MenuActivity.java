package com.atc.vuyaninxele.sight3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.firebase.auth.FirebaseAuth;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.6));

        CircleMenu circleMenu = findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#ffffff"),
                R.drawable.ic_add, R.drawable.ic_remove)
                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.home)
                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.settings)
                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.contact)
                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.logout)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if (index == 0){
                            startActivity(new Intent(MenuActivity.this, MainActivity.class));
                        }
                        else if (index == 1){
                            startActivity(new Intent(MenuActivity.this, Settings.class));
                        }
                        else if (index == 2){
                            startActivity(new Intent(MenuActivity.this, Contact.class));
                        }
                        else if (index == 3){

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.signOut();
                            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
