package com.example.ty.sexywifi;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by cdubiel on 2/24/18.
 */

public class ProfileActivity extends AppCompatActivity {
    RelativeLayout mRelativeLayout;
    Paint mCirclePaint;
    Paint mBackgroundPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_match);
//        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        //add RelativeLayout
        mRelativeLayout = findViewById(R.id.activity_main_relative_layout);

        //add LayoutParams
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        MatchView match = new MatchView(this);
        match.setLayoutParams(params);
        mRelativeLayout.addView(match);
    }


}