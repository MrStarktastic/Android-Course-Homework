package com.example.mr_starktastic.ex3;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TriviaActivity extends AppCompatActivity
        implements View.OnClickListener, QuestionFragment.OnAnswerChangeListener {
    private static final int REQUEST_CODE_RESULT = 1;

    private ViewPager questionPager;
    private QuestionPagerAdapter questionAdapter;
    private TextView pageIndexText;

    private boolean[] correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // The bottom toolbar has to be visible above the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        questionPager = (ViewPager) findViewById(R.id.pager);
        Button prevButton = (Button) findViewById(R.id.previous_button);
        Button nextButton = (Button) findViewById(R.id.next_button);
        pageIndexText = (TextView) findViewById(R.id.page_index_text);

        questionPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setQuestionIndexText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Resources res = getResources();
        questionAdapter = new QuestionPagerAdapter(getSupportFragmentManager(),
                res.getStringArray(R.array.questions), res.getStringArray(R.array.answers));
        questionPager.setAdapter(questionAdapter);
        setQuestionIndexText(0);
        correctAnswers = new boolean[questionAdapter.getCount()];

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_button:
                int index = questionPager.getCurrentItem() - 1;
                questionPager.setCurrentItem(index > 0 ? index : questionAdapter.getCount() - 1);
                break;

            case R.id.next_button:
                questionPager.setCurrentItem((questionPager.getCurrentItem() + 1)
                        % questionAdapter.getCount());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trivia, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_check_answers) {
            Intent intent = new Intent(TriviaActivity.this, ResultActivity.class)
                    .putExtra(ResultActivity.EXTRA_CORRECT_ANS_COUNT, getCorrectAnswersCount())
                    .putExtra(ResultActivity.EXTRA_TOTAL_ANS_COUNT, correctAnswers.length);
            startActivityForResult(intent, REQUEST_CODE_RESULT);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESULT && resultCode == RESULT_OK) { // Resets trivia
            questionPager.setAdapter(new QuestionPagerAdapter(getSupportFragmentManager(),
                    questionAdapter.questions, questionAdapter.answers));
            questionPager.setCurrentItem(0);
            setQuestionIndexText(0);

            for (int i = 0; i < correctAnswers.length; ++i)
                correctAnswers[i] = false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getCorrectAnswersCount() {
        int count = 0;
        for (boolean b : correctAnswers) if (b) ++count;
        return count;
    }

    private void setQuestionIndexText(int position) {
        pageIndexText.setText(position + 1 + "/" + questionAdapter.getCount());
    }

    @Override
    public void onAnswerChange(boolean isCorrect) {
        correctAnswers[questionPager.getCurrentItem()] = isCorrect;
    }

    private static final class QuestionPagerAdapter extends FragmentStatePagerAdapter {
        private String[] questions, answers;
        private int count;

        QuestionPagerAdapter(FragmentManager fm, String[] questions, String[] answers) {
            super(fm);
            this.questions = questions;
            this.answers = answers;
            count = questions.length;
        }

        @Override
        public Fragment getItem(int position) {
            return QuestionFragment.newInstance(questions[position], answers[position]);
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
