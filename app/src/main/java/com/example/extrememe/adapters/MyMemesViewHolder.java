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
import com.example.extrememe.services.LoginService;
import com.squareup.picasso.Picasso;


public class MyMemesViewHolder extends RecyclerView.ViewHolder {
    public MemesAdapter.OnItemClickListener onItemClickListener;
    public MemesAdapter.OnMemeRemoveListener onMemeRemoveListener;
    public MemesAdapter.OnMemeLikeListener onMemeLikeListener;
    private boolean isEditAvailable;
    TextView memeDescription;
    TextView memeLikes;
    TextView editMeme;
    ImageView likeMeme;
    ImageView memeImage;
    ImageView removeMeme;
    Meme currentMeme;
    int position;

    public MyMemesViewHolder(@NonNull View itemView, boolean isEditable) {
        super(itemView);
        memeDescription = itemView.findViewById(R.id.listrow_text_v);
        memeImage = itemView.findViewById(R.id.listrow_image_v);
        likeMeme = itemView.findViewById(R.id.like_button);
        memeLikes = itemView.findViewById(R.id.likes_text_v);
        editMeme = itemView.findViewById(R.id.listmeme_edit_text_v);
        removeMeme = itemView.findViewById(R.id.listrow_remove_meme);
        this.isEditAvailable = isEditable;

        if (this.isEditAvailable) {
            editMeme.setVisibility(View.VISIBLE);
            removeMeme.setVisibility(View.VISIBLE);
        }

        editMeme.setOnClickListener(view -> {
            MyMemesFragmentDirections.ActionMyMemesToEditMeme action = MyMemesFragmentDirections.actionMyMemesToEditMeme(currentMeme);
            Navigation.findNavController(view).navigate(action);
        });

        itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
        removeMeme.setOnClickListener(view -> onMemeRemoveListener.onItemRemove(currentMeme));
        memeImage.setOnLongClickListener(view -> onMemeLikeListener.onLikeMeme(currentMeme));
        likeMeme.setOnClickListener(view -> onMemeLikeListener.onLikeMeme(currentMeme));
    }

    public void bindData(Meme meme, int position) {
        currentMeme = meme;
        memeDescription.setText(meme.getDescription());
        memeLikes.setText(meme.getUsersLikes() != null ? meme.getUsersLikes().size() + "" : "");
        memeImage.setImageResource(R.drawable.ic_baseline_image_24);
        this.position = position;

        if (meme.getImageUrl() != null) {
            Picasso.get().load(meme.getImageUrl()).fit().centerInside().placeholder(R.drawable.ic_baseline_image_24).into(memeImage);
        }

        setLikeMemeIcon(meme);
    }

    private void setLikeMemeIcon(Meme meme) {
        if (LoginService.getInstance(itemView.getContext()).getFirebaseUser() != null) {
            if (meme.getUsersLikes().contains(LoginService.getInstance(itemView.getContext()).getFirebaseUser().getUid())) {
                likeMeme.setImageResource(R.drawable.ic_baseline_full_favorite_24);
            } else {
                likeMeme.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
        } else {
            likeMeme.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }
}
