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
    /**
     * This activity starts another activity and would like to get data back from it.
     * A request code identifier helps with that. See below when starting the {@link ResultActivity}
     * and inside the method onActivityResult.
     */
    private static final int REQUEST_CODE_RESULT = 1;

    /**
     * This is the view which holds {@link QuestionFragment}s as pages.
     */
    private ViewPager questionPager;

    /**
     * An adapter for our questionPager. Manages the content and initiates {@link Fragment}s when needed.
     */
    private QuestionPagerAdapter questionAdapter;

    /**
     * The {@link TextView} displayed at the bottom of this activity (not inside the questionPager)
     * which indicates the question/page's index.
     */
    private TextView pageIndexText;

    private String[] correctAnswers, userAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // The bottom toolbar has to be visible above the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setTitle("My Trivia");

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
        /**
         * A {@link FragmentStatePagerAdapter} utilizes a {@link FragmentManager}
         * to add fragments to the screen.
         */
        questionAdapter = new QuestionPagerAdapter(getSupportFragmentManager(),
                res.getStringArray(R.array.questions));
        questionPager.setAdapter(questionAdapter);
        setQuestionIndexText(0);
        correctAnswers = res.getStringArray(R.array.answers);
        userAnswers = new String[questionAdapter.getCount()];

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /**
         * Both previous & next buttons manifest a cyclic behavior, hence modulo.
         */
        switch (view.getId()) {
            case R.id.previous_button:
                int index = questionPager.getCurrentItem() - 1;
                questionPager.setCurrentItem(index >= 0 ? index : questionAdapter.getCount() - 1);
                break;

            case R.id.next_button:
                questionPager.setCurrentItem((questionPager.getCurrentItem() + 1)
                        % questionAdapter.getCount());
                break;
        }
    }

    /**
     * The action buttons at the top of the screen (on the {@link android.support.v7.app.ActionBar})
     * have to be inflated here.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trivia, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_check_answers) {
            /**
             * The menu button at the top was selected, so the {@link ResultActivity} must start.
             * We pass the amount of correct answers and also the total amount of questions.
             *
             * The activity is started but not with the regular startActivity method.
             * As the user is finished with {@link ResultActivity}, we want {@link TriviaActivity}
             * to know whether the user pressed on the "Try Again" button.
             * So we use startActivityForResult and our REQUEST_CODE_RESULT will be later used
             * in the onActivityResult method down below.
             */
            Intent intent = new Intent(TriviaActivity.this, ResultActivity.class)
                    .putExtra(ResultActivity.EXTRA_CORRECT_ANS_COUNT, getCorrectAnswersCount())
                    .putExtra(ResultActivity.EXTRA_TOTAL_ANS_COUNT, userAnswers.length);
            startActivityForResult(intent, REQUEST_CODE_RESULT);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * The {@link ResultActivity} sets the resultCode to be RESULT_OK if the user wants
         * to reset the trivia. RESULT_CANCELED is the default value.
         */
        if (requestCode == REQUEST_CODE_RESULT && resultCode == RESULT_OK) { // Resets trivia
            questionPager.setAdapter(new QuestionPagerAdapter(getSupportFragmentManager(),
                    questionAdapter.questions));
            questionPager.setCurrentItem(0);
            setQuestionIndexText(0);

            for (int i = 0; i < userAnswers.length; ++i)
                userAnswers[i] = null;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Counts the amount of correct answers.
     */
    private int getCorrectAnswersCount() {
        int count = 0;

        for (int i = 0; i < userAnswers.length; ++i) {
            String userAns = userAnswers[i];

            if (userAns != null && !userAns.isEmpty())
                if (userAns.equalsIgnoreCase(correctAnswers[i]))
                    ++count;
        }

        return count;
    }

    private void setQuestionIndexText(int position) {
        pageIndexText.setText(position + 1 + "/" + questionAdapter.getCount());
    }

    /**
     * This method is called from a {@link Fragment} whenever the user modifies an answer.
     *
     * @param answer The answer that the user has typed.
     */
    @Override
    public void onAnswerChange(int position, String answer) {
        userAnswers[position] = answer;
    }

    /**
     * This can be created in a separated file if you wish.
     * <p>
     * P.S. There's a difference between {@link FragmentStatePagerAdapter} and
     * {@link android.support.v4.app.FragmentPagerAdapter}. Google it or talk to me if you'd like to know.
     */
    private static final class QuestionPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * Both of the string arrays have the same length. You are free to consider
         * a different approach. A 2D array or a dedicated class, perhaps?
         */
        private String[] questions;
        private int count;

        QuestionPagerAdapter(FragmentManager fm, String[] questions) {
            super(fm);
            this.questions = questions;
            count = questions.length;
        }

        @Override
        public Fragment getItem(int position) {
            return QuestionFragment.newInstance(position, questions[position]);
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
