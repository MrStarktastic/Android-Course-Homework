package com.example.mr_starktastic.ex2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    /**
     * Key names used to pass data from this activity to {@link SmileyDragActivity} via an {@link Intent}.
     * The string values don't matter, they just have to be unique.
     */
    public static final String EXTRA_BG_TINT_LIST = "com.example.ex2.TINT";
    public static final String EXTRA_SRC = "com.example.ex2.SRC";
    public static final String EXTRA_ALPHA = "com.example.ex2.ALPHA";
    public static final String EXTRA_SCALE = "com.example.ex2.SCALE";
    public static final String EXTRA_ROTATION = "com.example.ex2.ROTATION";

    /**
     * Resource identifiers for the 2 different smiley drawables
     */
    private static final int[] DRAWABLE_RES =
            {R.drawable.happy_smiley_face, R.drawable.sad_smiley_face};

    /**
     * Current drawable resource identifier (either happy or sad)
     */
    private int currentRes;

    private ImageView imageView;
    private TextView alphaText;
    private TextView scaleText;
    private TextView rotationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(DRAWABLE_RES[currentRes = 0]);

        /**
         * {@link ColorStateList} is usually used for more sophisticated reasons
         * (changing view color based on states: pressed/enabled, etc...)
         * Here, it's just used as a color container and later can be passed in
         * a method such as the one below it in order to tint the background of a view.
         */
        final ColorStateList backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        ViewCompat.setBackgroundTintList(imageView, backgroundTintList);

        /**
         * {@link SeekBar} can be referred to as a "slider" for clarity's sake.
         */

        /**
         * The terms alpha and opacity are equivalent.
         */
        SeekBar alphaSeekBar = (SeekBar) findViewById(R.id.seek_bar_alpha);
        alphaSeekBar.setOnSeekBarChangeListener(this);
        alphaText = (TextView) findViewById(R.id.text_alpha);

        SeekBar scaleSeekBar = (SeekBar) findViewById(R.id.seek_bar_scale);
        scaleSeekBar.setOnSeekBarChangeListener(this);
        scaleText = (TextView) findViewById(R.id.text_scale);

        SeekBar rotationSeekBar = (SeekBar) findViewById(R.id.seek_bar_rotation);
        rotationSeekBar.setOnSeekBarChangeListener(this);
        rotationText = (TextView) findViewById(R.id.text_rotation);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(DRAWABLE_RES[currentRes = (currentRes + 1) % 2]);
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**
                 * The {@link Intent} represents the intent to start another activity on top of the current one.
                 * Observes that it receives the instance of the current activity,
                 * AKA {@link android.content.Context} and then the class on the activity we want to start.
                 * In addition we can pass extra (pun intended) data for the upcoming activity.
                 */
                Intent intent = new Intent(MainActivity.this, SmileyDragActivity.class)
                        .putExtra(EXTRA_BG_TINT_LIST, backgroundTintList)
                        .putExtra(EXTRA_SRC, DRAWABLE_RES[currentRes])
                        .putExtra(EXTRA_ALPHA, imageView.getAlpha())
                        .putExtra(EXTRA_SCALE, imageView.getScaleX())
                        .putExtra(EXTRA_ROTATION, imageView.getRotation());
                // And this is where the magic happens.
                startActivity(intent);
                // True must be returned to let the app know that it indeed consumed a long click.
                return true;
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // By default, progress is a value between 0 and 100 (inclusive).

        switch (seekBar.getId()) {
            case R.id.seek_bar_alpha:
                imageView.setAlpha(progress / 100f);
                alphaText.setText(progress + "%");
                break;

            case R.id.seek_bar_scale:
                final float scale = progress / 100f;
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
                scaleText.setText(progress + "%");
                break;

            case R.id.seek_bar_rotation:
                final float rotation = progress * 360f / 100f;
                imageView.setRotation(rotation);
                // \u00b0 is the unicode id for the degree symbol
                rotationText.setText(rotation + "\u00b0");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        /**
         * No use for this method but the interface {@link SeekBar.OnSeekBarChangeListener} requires it
         */
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        /**
         * No use for this method but the interface {@link SeekBar.OnSeekBarChangeListener} requires it
         */
    }
}
