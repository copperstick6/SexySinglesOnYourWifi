package com.example.ty.sexywifi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button setProfileBtn = findViewById(R.id.buttonSetProfile);
        Button findMatchBtn = findViewById(R.id.buttonFindMatch);

//        handles clicking on match
        findMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                startActivity(i);
            }
        });


        // handles clicking on profile
        setProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the fragment as the main content.
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager
                        .beginTransaction();
                PrefsFragment mPrefsFragment = new PrefsFragment();
                mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.profile);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(getResources().getColor(android.R.color.white));

            EditTextPreference fn = (EditTextPreference) findPreference("firstname");
            EditTextPreference ln = (EditTextPreference) findPreference("lastname");
            ListPreference sx = (ListPreference) findPreference("sex");
            ListPreference sxpf = (ListPreference) findPreference("sex_preference");

            if(fn.getText().equals(""))
                fn.setSummary("none");
            else
                fn.setSummary(fn.getText());

            if(ln.getText().equals(""))
                ln.setSummary("none");
            else
                ln.setSummary(ln.getText());

            sx.setSummary(sx.getValue());
            sxpf.setSummary(sxpf.getValue());

            return view;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /* get preference */
            Preference preference = findPreference(key);

        /* update summary */
            if (key.equals("sex") || key.equals("sex_preference")) {
                preference.setSummary(((ListPreference) preference).getValue());
            }
            if (key.equals("firstname") | key.equals("lastname")) {
                if(((EditTextPreference) preference).getText().equals(""))
                    preference.setSummary("none");
                else
                    preference.setSummary(((EditTextPreference) preference).getText());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

    }


}
