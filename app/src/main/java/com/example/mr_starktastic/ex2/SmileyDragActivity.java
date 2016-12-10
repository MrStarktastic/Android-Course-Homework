package com.example.mr_starktastic.ex2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SmileyDragActivity extends AppCompatActivity {
    public static final String COORDINATES_KEY = "com.example.ex2.COORDINATES";

    private static final String TITLE = "Move it!";

    private ImageView imageView;
    private int orientation;
    private OrientationCoordinates coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smiley_drag);
        setTitle(TITLE);

        Intent data = getIntent();
        imageView = (ImageView) findViewById(R.id.image);
        orientation = getResources().getConfiguration().orientation;

        if (savedInstanceState != null) {
            coords = (OrientationCoordinates) savedInstanceState.getSerializable(COORDINATES_KEY);
            assert coords != null;
            final float[] xy = coords.getCoordinates(orientation);

            if (xy != null)
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setX(xy[0]);
                        imageView.setY(xy[1]);
                    }
                });
        }

        if (data != null) {
            ViewCompat.setBackgroundTintList(imageView,
                    (ColorStateList) data.getParcelableExtra(MainActivity.EXTRA_BG_TINT_LIST));
            imageView.setImageResource(data.getIntExtra(MainActivity.EXTRA_SRC, 0));
            imageView.setAlpha(data.getFloatExtra(MainActivity.EXTRA_ALPHA, 0));
            float scale = data.getFloatExtra(MainActivity.EXTRA_SCALE, 0);
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            imageView.setRotation(data.getFloatExtra(MainActivity.EXTRA_ROTATION, 0));
        }

        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float dx, dy;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = event.getRawX() - imageView.getX();
                        dy = event.getRawY() - imageView.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        imageView.setX(event.getRawX() - dx);
                        imageView.setY(event.getRawY() - dy);
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (coords == null)
            coords = new OrientationCoordinates();

        coords.setCoordinates(orientation, new float[]{imageView.getX(), imageView.getY()});
        outState.putSerializable(COORDINATES_KEY, coords);
    }
}
