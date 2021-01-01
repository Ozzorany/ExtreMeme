package com.example.extrememe.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.MyMemesFragmentDirections;
import com.example.extrememe.R;
import com.example.extrememe.model.Meme;


public class MyMemesViewHolder extends RecyclerView.ViewHolder {
    public MyMemesAdapter.OnItemClickListener listener;
    private boolean isEditAvailable;
    TextView memeDescription;
    TextView memeLikes;
    TextView editMeme;
    ImageView memeImage;
    Meme currentMeme;
    int position;

    public MyMemesViewHolder(@NonNull View itemView, boolean isEditable) {
        super(itemView);
        memeDescription = itemView.findViewById(R.id.listrow_text_v);
        memeImage = itemView.findViewById(R.id.listrow_image_v);
        memeLikes = itemView.findViewById(R.id.likes_text_v);
        editMeme = itemView.findViewById(R.id.listmeme_edit_text_v);
        this.isEditAvailable = isEditable;

        if (this.isEditAvailable) {
            editMeme.setVisibility(View.VISIBLE);
        }

        editMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMemesFragmentDirections.ActionMyMemesToEditMeme action = MyMemesFragmentDirections.actionMyMemesToEditMeme(currentMeme);
                Navigation.findNavController(view).navigate(action);
            }
        });

        itemView.setOnClickListener(view -> listener.onItemClick(position));
    }

    public void bindData(Meme meme, int position) {
        currentMeme = meme;
        memeDescription.setText(meme.getDescription());
        memeLikes.setText(meme.getUsersLikes() != null ? meme.getUsersLikes().size() + "" : "");
        this.position = position;
    }
}
