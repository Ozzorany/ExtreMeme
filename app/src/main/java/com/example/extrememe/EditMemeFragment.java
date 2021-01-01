package com.example.extrememe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;

public class EditMemeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_meme, container, false);
        EditText memeDescription = view.findViewById(R.id.editmeme_text_input);
        ImageView memeImage = view.findViewById(R.id.editmeme_image_v);
        Button saveBtn = view.findViewById(R.id.editmeme_btn);
        Meme meme = EditMemeFragmentArgs.fromBundle(getArguments()).getMeme();
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(view.getContext());

        memeDescription.setText(meme.getDescription());

        memeDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                meme.setDescription(String.valueOf(editable));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memeDescription.clearFocus();

                MemeModel.instance.updateMeme(meme, new MemeModel.UpdateMemeListener() {
                    @Override
                    public void onComplete(Void result) {
                        alBuilder.setTitle("SUCCESS").setMessage("Meme changes were saved");
                        alBuilder.show();
                    }
                });
            }
        });

        return view;
    }
}