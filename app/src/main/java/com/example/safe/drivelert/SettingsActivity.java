package com.example.safe.drivelert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsContainer, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_general, rootKey);
            // Phone Number Edit Change Listner
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_dialNo)));

            // SMS Message Edit Change Listner
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_smsMessage)));

            bindPreferenceSummaryToValue(findPreference("key_driveTime"));

        }

        private static void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), "123"));
        }

        private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                if (preference instanceof EditTextPreference) {
                    if (preference.getKey().equals("key_dialNo")) {
                        // update the changed gallery name to summary filed
                        preference.setSummary(stringValue);
                    } else if (preference.getKey().equals("key_smsMessage")) {
                        // update the changed gallery name to summary filed
                        preference.setSummary(stringValue + " My Current Location:https://www.google.com/maps/search/?api=1&query=0.0,0.0");
                    }
                } else {
                    preference.setSummary(stringValue);
                }
                return true;
            }
        };

    }
}