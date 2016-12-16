package com.example.mr_starktastic.ex3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_CORRECT_ANS_COUNT = "com.example.ex3.CORRECT_ANS_COUNT";
    public static final String EXTRA_TOTAL_ANS_COUNT = "com.example.ex3.TOTAL_ANS_COUNT";

    private static final int DEFAULT_CORRECT_ANS_COUNT = 0;
    private static final int DEFAULT_TOTAL_ANS_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView feedbackText = (TextView) findViewById(R.id.feedback_text);
        TextView scoreText = (TextView) findViewById(R.id.correct_answers_text);
        Button tryAgainButton = (Button) findViewById(R.id.try_again_button);

        Intent data = getIntent();
        int correctAnsCount = data.getIntExtra(EXTRA_CORRECT_ANS_COUNT, DEFAULT_CORRECT_ANS_COUNT);
        int totalAnsCount = data.getIntExtra(EXTRA_TOTAL_ANS_COUNT, DEFAULT_TOTAL_ANS_COUNT);
        float correctRatio = (float) correctAnsCount / totalAnsCount;
        String feedback;

        if (correctRatio == 1)
            feedback = getString(R.string.result_excellent);
        else if (correctRatio > 0.5)
            feedback = getString(R.string.result_good);
        else if (correctRatio > 0)
            feedback = getString(R.string.result_mediocre);
        else
            feedback = getString(R.string.result_bad);

        feedbackText.setText(feedback);
        scoreText.setText(correctAnsCount + "/" + totalAnsCount + " " +
                getString(R.string.correct_answers));

        final AlertDialog tryAgainDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.try_again_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_OK); // For onActivityResult of the parent activity
                        finish(); // This is how we close the activity
                    }
                }).setNegativeButton(android.R.string.no, null).create();

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgainDialog.show();
            }
        });
    }
}
