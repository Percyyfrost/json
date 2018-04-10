package com.atc.vuyaninxele.sight3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Popup extends AppCompatActivity {

    ListView listView;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            final String json = bundle.getString("sites");
            Sites sites1 = new Gson().fromJson(json, Sites.class);
            final ListView listView = findViewById(R.id.siteinfo);
            info = new ArrayList<>();
            info.addAll(Arrays.asList("NAME:             " + sites1.getSiteName(), "NUMBER:         " + sites1.getSiteNumber(), "LOCATION:      " + sites1.getLocation(), "TYPE:                  ROOFTOP", "SUPERVISOR:    "  + sites1.getSupervisor(), "CONTACT:         " + sites1.getContactDetail()));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, info);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                /**
                 * I created this function to check if the app sending data to exists (error management).
                 * It is to avoid our app from crashing.
                 **/

                public boolean checkIntent(Intent intent) {
                    PackageManager packageManager = getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    boolean isIntentSafe = activities.size() > 0;

                    return isIntentSafe;
                }

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String text = listView.getItemAtPosition(i).toString();

                    boolean contact = text.startsWith("CONTACT");
                    boolean location = text.startsWith("LOCATION");
                    boolean name = text.startsWith("NAME");

                    if (name){
                        Intent intent = new Intent(Popup.this, MapsActivity.class);
                        intent.putExtra("sites",json);
                        intent.putExtra("Lat",getIntent().getExtras().getDouble("Lat"));
                        intent.putExtra("Lng",getIntent().getExtras().getDouble("Lng"));
                        intent.putExtra("CheckClicked", 1);
                        startActivity(intent);
                    }
                    else if (contact) {

                        Uri number = Uri.parse("tel:0817789580");
                        Intent callIntent = new Intent(Intent.ACTION_VIEW, number);

                        String openwith = "Open With:";
                        Intent chooser = Intent.createChooser(callIntent, openwith);
                        if (callIntent.resolveActivity(getPackageManager()) != null) {
                            boolean check = checkIntent(callIntent);

                            if (check) {
                                startActivity(chooser);
                            }
                        }


                    } else if (location) {

                        Uri loc = Uri.parse("geo:0,0?q= 1 Montecasino Blvd, Fourways, Midrand, 2021");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, loc);

                        String openwith = "Open With:";
                        Intent chooser = Intent.createChooser(mapIntent, openwith);
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {

                            boolean check = checkIntent(mapIntent);

                            if (check) {
                                startActivity(chooser);
                            }
                        }
                    }
                }
            });
        }

    }
}
