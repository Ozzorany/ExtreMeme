package com.example.extrememe.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.R;
import com.example.extrememe.model.Meme;


public class MyMemesViewHolder extends RecyclerView.ViewHolder{
    public MyMemesAdapter.OnItemClickListener listener;
    TextView memeDescription;
    TextView memeLikes;
    ImageView memeImage;
    int position;

    public MyMemesViewHolder(@NonNull View itemView) {
        super(itemView);
        memeDescription = itemView.findViewById(R.id.listrow_text_v);
        memeImage = itemView.findViewById(R.id.listrow_image_v);
        memeLikes = itemView.findViewById(R.id.likes_text_v);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(Meme meme, int position) {
        memeDescription.setText(meme.getDescription());
        memeLikes.setText(meme.getUsersLikes().length + "");
        this.position = position;
    }
}
