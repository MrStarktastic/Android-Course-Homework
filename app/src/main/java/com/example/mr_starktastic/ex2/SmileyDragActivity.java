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
    /**
     * Key name used to pass an {@link OrientationCoordinates} object via a {@link Bundle}.
     */
    public static final String COORDINATES_KEY = "com.example.ex2.COORDINATES";

    /**
     * This activity's title (shows up in the {@link android.support.v7.app.ActionBar}).
     */
    private static final String TITLE = "Move it!";

    private ImageView imageView;
    private int orientation;
    private OrientationCoordinates coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smiley_drag);
        setTitle(TITLE);

        /**
         * This {@link Intent} is the one that was passed from the parent activity, containing data.
         */
        Intent data = getIntent();

        imageView = (ImageView) findViewById(R.id.image);

        /**
         * This is how you get the device's current screen orientation.
         */
        orientation = getResources().getConfiguration().orientation;

        /**
         * If this activity has a saved instance state
         * (after being recreated, presumably due to orientation change), we can restore the state.
         */
        if (savedInstanceState != null) {
            coords = (OrientationCoordinates) savedInstanceState.getSerializable(COORDINATES_KEY);

            // It can't be null, but Android Studio doesn't get it.
            if (coords != null) {
                final float[] xy = coords.getCoordinates(orientation);

                if (xy != null)
                    /*
                    The post method adds an operation to a queue and executes it after the view
                    is done settling. The position of a view is a bit more comprehensive than one
                    might think. Without posting this operation, the view's position on screen will
                    be based off the screen's middle coordinates plus the coordinates that we pass below.
                    We don't want that to happen. What we do want is to set the view's absolute position
                    to (xy[0], xy[1]).
                     */
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setX(xy[0]);
                            imageView.setY(xy[1]);
                        }
                    });
            }
        }

        /**
         * This is where we unpack the data from the {@link Intent} so that the {@link ImageView}
         * here will look the same as it looked in the parent activity.
         */
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
            /**
             * The following variables represent the distance between a finger
             * to the view's position.
             */
            private float dx, dy;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    /**
                     * When a finger is detected on the view.
                     */
                    case MotionEvent.ACTION_DOWN:
                        // event.getRaw{X, Y} is the event's absolute position on screen
                        dx = event.getRawX() - imageView.getX();
                        dy = event.getRawY() - imageView.getY();
                        break;

                    /**
                     * When a finger moves on top of the view.
                     */
                    case MotionEvent.ACTION_MOVE:
                        imageView.setX(event.getRawX() - dx);
                        imageView.setY(event.getRawY() - dy);
                        break;
                }

                /**
                 * True is returned to confirm the consumption of a touch event.
                 */
                return true;
            }
        });
    }

    /**
     * This method is triggered upon orientation changes
     * (also when this activity is stopped but it doesn't matter here).
     *
     * @param outState The data container which will be passed to onCreate when this activity is recreated.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (coords == null)
            coords = new OrientationCoordinates();

        coords.setCoordinates(orientation, new float[]{imageView.getX(), imageView.getY()});
        /**
         * This is why {@link OrientationCoordinates} is {@link java.io.Serializable}.
         * Because {@link Bundle} obviously doesn't have a method for putting our custom objects.
         */
        outState.putSerializable(COORDINATES_KEY, coords);
    }
}
