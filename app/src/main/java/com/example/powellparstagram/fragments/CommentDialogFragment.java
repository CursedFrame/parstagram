package com.example.powellparstagram.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.powellparstagram.R;
import com.example.powellparstagram.objects.Comment;
import com.example.powellparstagram.objects.Post;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentDialogFragment extends DialogFragment {

    public static final String TAG = "CommentDialogFragment";

    private Button btnComment;
    private EditText etComment;
    private Post post;

    public CommentDialogFragment() {
        // Required empty public constructor
    }

    public static CommentDialogFragment newInstance(String title) {
        CommentDialogFragment frag = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        post = bundle.getParcelable("post");

        btnComment = view.findViewById(R.id.btnComment);
        etComment = view.findViewById(R.id.etComment);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Comment comment = new Comment();
                comment.setCommentPost(post);
                comment.setCommentText(etComment.getText().toString());
                comment.setCommentUser(ParseUser.getCurrentUser());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error making comment", e);
                            return;
                        }
                        ParseRelation<Comment> parseRelation = post.getRelation("comments");
                        parseRelation.add(comment);
                        post.saveInBackground();
                    }
                });
                dismiss();
            }
        });


    }
}