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
    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";

    private String question, answer;

    private OnAnswerChangeListener answerListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question Question string.
     * @param answer   Correct answer string.
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(String question, String answer) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_ANSWER, answer);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            answer = getArguments().getString(ARG_ANSWER);
        }
    }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (answerListener != null)
            if (charSequence.length() != 0)
                answerListener.onAnswerChange(answer.equalsIgnoreCase(charSequence.toString()));
            else answerListener.onAnswerChange(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface OnAnswerChangeListener {
        void onAnswerChange(boolean isCorrect);
    }
}
