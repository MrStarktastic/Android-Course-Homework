package com.example.mr_starktastic.ex1;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PREF_KEY = "TEXT_VISIBILITY";

    private static final String TEXT = "Hello World!";
    private static final String SHOW_TEXT = "Show";
    private static final String HIDE_TEXT = "Hide";

    private SharedPreferences preferences;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(MODE_PRIVATE);

        textView = (TextView) findViewById(R.id.hello_world_text);
        textView.setText(TEXT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.primary_text_light));
        textView.setVisibility(preferences.getBoolean(PREF_KEY, false)
                ? View.VISIBLE : View.INVISIBLE);

        Button showTextButton = (Button) findViewById(R.id.show_text_button);
        showTextButton.setText(SHOW_TEXT);
        showTextButton.setOnClickListener(this);

        Button hideTextButton = (Button) findViewById(R.id.hide_text_button);
        hideTextButton.setText(HIDE_TEXT);
        hideTextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_text_button:
                preferences.edit().putBoolean(PREF_KEY, true).apply();
                textView.setVisibility(View.VISIBLE);
                break;

            case R.id.hide_text_button:
                preferences.edit().putBoolean(PREF_KEY, false).apply();
                textView.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }
}
