package com.example.extrememe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MemesAdapter;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;
import com.example.extrememe.services.LoginService;

import java.util.Collections;
import java.util.List;

public class MyMemesFragment extends Fragment {
    MemesViewModel memesViewModel;
    MemesAdapter adapter;
    TextView emptyMemesText;
    ImageView emptyMemesImage;
    ProgressBar progressBar;

    private MenuItem signOutButton;
    private MenuItem signInButton;
    private MenuItem loggedInUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_memes, container, false);
        RecyclerView memesRv = view.findViewById(R.id.mymemes_rv);
        memesRv.setHasFixedSize(true);
        setHasOptionsMenu(true);
        memesViewModel = new ViewModelProvider(this).get(MemesViewModel.class);
        progressBar = view.findViewById(R.id.pBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(view.getContext());
        emptyMemesText = view.findViewById(R.id.mymemes_empty_tv);
        emptyMemesImage = view.findViewById(R.id.mymemes_empty_img);

        adapter = new MemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);
        adapter.isEditAvailable = true;

        if (LoginService.getInstance(this.getContext()).getFirebaseUser() != null) {
            memesViewModel.getMemesByUserId(LoginService.getInstance(this.getContext()).getFirebaseUser().getUid()).observe(getViewLifecycleOwner(), memes -> {
                handleEmptyMemes(memes);
                Collections.sort(memes, (firstMeme, secondMeme) -> secondMeme.getLastUpdated().compareTo(firstMeme.getLastUpdated()));
                adapter.data = memes;
                adapter.notifyDataSetChanged();
                adapter.setOnMemeLikeListener((meme) -> {
                    if (LoginService.getInstance(MyMemesFragment.super.getContext()).isLoggedIn()) {
                        likeMeme(meme);
                    } else {
                        alBuilder.setTitle("FAILED").setMessage("Please log in to like memes :)");
                        alBuilder.show();
                    }

                    return true;
                });

                adapter.setOnRemoveListener(meme -> {
                    alBuilder.setTitle("INFO").setMessage("Are you sure you want to delete the meme?").setPositiveButton("yes", (dialogInterface, i) -> MemeModel.instance.removeMeme(meme, result1 -> {
                        adapter.data.remove(meme);
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                        handleEmptyMemes(adapter.data);
                    })).setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());

                    alBuilder.show();
                });
            });
        }


        emptyMemesImage.setOnClickListener(view1 -> {
            NavDirections action = MyMemesFragmentDirections.actionMyMemesToCreateMeme();
            Navigation.findNavController(view1).navigate(action);
        });

        reloadData(LoginService.getInstance(this.getContext()).getFirebaseUser().getUid());

        return view;
    }

    private void likeMeme(Meme meme) {
        String userId = LoginService.getInstance(this.getContext()).getFirebaseUser().getUid();
        ImageView imageView = getView().findViewById(R.id.like_button);

        if (!meme.getUsersLikes().contains(userId)) {
            meme.getUsersLikes().add(userId);
            imageView.setImageResource(R.drawable.ic_baseline_full_favorite_24);
        } else {
            meme.getUsersLikes().remove(userId);
            imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        MemeModel.instance.updateMeme(meme, result -> adapter.notifyDataSetChanged());
    }

    private void reloadData(String userId){
        MemeModel.instance.refreshAllMyMemes(userId, () -> {
            progressBar.setVisibility(View.GONE);
        });
    }

    private void handleEmptyMemes(List<Meme> memes) {
        if (memes.isEmpty()) {
            emptyMemesImage.setVisibility(View.VISIBLE);
            emptyMemesText.setVisibility(View.VISIBLE);
        }
        else {
            emptyMemesImage.setVisibility(View.INVISIBLE);
            emptyMemesText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_top_menu, menu);
        loggedInUsername = menu.findItem(R.id.logged_in_display_name);
        signInButton = menu.findItem(R.id.google_sign_in_button);
        signOutButton = menu.findItem(R.id.google_sign_out_button);

        if (LoginService.getInstance(this.getContext()).isLoggedIn()) {
            setSignedInView(LoginService.getInstance(this.getContext()).getGoogleAccount().getDisplayName());
        } else {
            Navigation.findNavController(this.getActivity(), R.id.mainactivity_navhost).navigateUp();
        }
    }

    private void setSignedInView(String displayName) {
        this.loggedInUsername.setTitle(displayName);
        this.signInButton.setVisible(false);
        this.signOutButton.setVisible(true);
        this.signOutButton.setOnMenuItemClickListener((listener) -> {
            LoginService.getInstance(this.getContext()).signOut();
            Navigation.findNavController(this.getActivity(), R.id.mainactivity_navhost).navigateUp();

            return false;
        });
    }
}