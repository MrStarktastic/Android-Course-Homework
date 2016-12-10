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
    public static final String EXTRA_BG_TINT_LIST = "com.example.ex2.TINT";
    public static final String EXTRA_SRC = "com.example.ex2.SRC";
    public static final String EXTRA_ALPHA = "com.example.ex2.ALPHA";
    public static final String EXTRA_SCALE = "com.example.ex2.SCALE";
    public static final String EXTRA_ROTATION = "com.example.ex2.ROTATION";

    private static final int[] DRAWABLE_RES =
            {R.drawable.happy_smiley_face, R.drawable.sad_smiley_face};

    private int currentRes;

    private ColorStateList backgroundTintList;
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
        backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        ViewCompat.setBackgroundTintList(imageView, backgroundTintList);

        final SeekBar alphaSeekBar = (SeekBar) findViewById(R.id.seek_bar_alpha);
        alphaSeekBar.setOnSeekBarChangeListener(this);
        alphaText = (TextView) findViewById(R.id.text_alpha);

        final SeekBar scaleSeekBar = (SeekBar) findViewById(R.id.seek_bar_scale);
        scaleSeekBar.setOnSeekBarChangeListener(this);
        scaleText = (TextView) findViewById(R.id.text_scale);

        final SeekBar rotationSeekBar = (SeekBar) findViewById(R.id.seek_bar_rotation);
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
                Intent intent = new Intent(MainActivity.this, SmileyDragActivity.class)
                        .putExtra(EXTRA_BG_TINT_LIST, backgroundTintList)
                        .putExtra(EXTRA_SRC, DRAWABLE_RES[currentRes])
                        .putExtra(EXTRA_ALPHA, imageView.getAlpha())
                        .putExtra(EXTRA_SCALE, imageView.getScaleX())
                        .putExtra(EXTRA_ROTATION, imageView.getRotation());
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seek_bar_alpha:
                imageView.setAlpha(i / 100f);
                alphaText.setText(i + "%");
                break;

            case R.id.seek_bar_scale:
                final float scale = i / 100f;
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
                scaleText.setText(i + "%");
                break;

            case R.id.seek_bar_rotation:
                final float rotation = i * 360f / 100f;
                imageView.setRotation(rotation);
                rotationText.setText(rotation + "\u00b0");
                break;

            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
