package com.example.mr_starktastic.ex3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionFragment extends Fragment implements TextWatcher {
    private static final String ARG_POSITION = "position";
    private static final String ARG_QUESTION = "question";

    private int position;
    private String question;

    private OnAnswerChangeListener answerListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question Question string.
     * @param position This fragment's position in the ViewPager.
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(int position, String question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_QUESTION, question);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Unlike an activity's onCreate method, here it's common to get the arguments and not deal with views.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            position = args.getInt(ARG_POSITION);
            question = args.getString(ARG_QUESTION);
        }
    }

    /**
     * This is where the {@link Fragment}'s view is inflated (created).
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        TextView questionText = (TextView) v.findViewById(R.id.question_text);
        questionText.setText(question);

        EditText answerEdit = (EditText) v.findViewById(R.id.answer_edit);
        answerEdit.addTextChangedListener(this);

        return v;
    }

    /**
     * This method is called when this {@link Fragment} is attached to its parent activity.
     *
     * @param context The parent activity's context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /**
         * This is an example of how one can force a parent activity to implement a listener.
         * Thus, we don't even need a setter method for the listener
         * unlike buttons and lots of other widgets (Remember setOnClickListener?).
         */
        if (context instanceof OnAnswerChangeListener)
            answerListener = (OnAnswerChangeListener) context;
        else throw new RuntimeException(context.toString() +
                " must implement OnAnswerChangeListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        answerListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * When the answer {@link EditText} is changed, the {@link OnAnswerChangeListener} is called.
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (answerListener != null)
            answerListener.onAnswerChange(position, charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * This is a functional interface. I believe it's crucial to know how listeners are made
     * and here lies the foundation for that.
     */
    public interface OnAnswerChangeListener {
        void onAnswerChange(int position, String answer);
    }
}
