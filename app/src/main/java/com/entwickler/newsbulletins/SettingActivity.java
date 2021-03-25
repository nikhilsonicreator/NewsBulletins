package com.entwickler.newsbulletins;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private TextView setting_select_language_txt;
    private Toolbar toolbar;
    private String start="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar=findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

         setting_select_language_txt = findViewById(R.id.setting_select_language_txt);

         SharedPreferences sharedPreferences = getSharedPreferences("language_save",MODE_PRIVATE);
         String set_text = sharedPreferences.getString("language_key","Select Language");

         setting_select_language_txt.setText(set_text);
         start = set_text;

         setting_select_language_txt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                 builder.setTitle("Choose a language");

                 final String[] language_keys = getResources().getStringArray(R.array.arrays_language_keys);
                 final String[] language_values = getResources().getStringArray(R.array.arrays_language_values);
                 builder.setItems(language_keys, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                         SharedPreferences sharedPreferences = getSharedPreferences("language_save",MODE_PRIVATE);
                         SharedPreferences.Editor editor = sharedPreferences.edit();

                         editor.putString("language_key",language_keys[which]);
                         editor.putString("language_value",language_values[which]);

                         editor.apply();
                         setting_select_language_txt.setText(language_keys[which]);

                     }
                 });

                 AlertDialog dialog = builder.create();
                 dialog.show();

             }
         });

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (!start.equals(setting_select_language_txt.getText().toString().trim())) {

                SearchFragment.get_news_feeds();
                LiveNewsFragment.get_news_feeds();
        }

        super.onBackPressed();
    }
}