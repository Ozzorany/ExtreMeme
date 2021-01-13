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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MemesAdapter;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;
import com.example.extrememe.services.LoginService;

import java.util.ArrayList;
import java.util.List;

public class MyMemesFragment extends Fragment {
    List<Meme> data = new ArrayList<>();
    MemesAdapter adapter;
    TextView emptyMemesText;
    ImageView emptyMemesImage;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(view.getContext());
        emptyMemesText = view.findViewById(R.id.mymemes_empty_tv);
        emptyMemesImage = view.findViewById(R.id.mymemes_empty_img);

        adapter = new MemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);
        adapter.isEditAvailable = true;

        if (LoginService.getInstance(this.getContext()).getFirebaseUser() != null) {
            MemeModel.instance.getMemesByUserId(LoginService.getInstance(this.getContext()).getFirebaseUser().getUid(), result -> {
                handleEmptyMemes(result);
                data = result;
                adapter.data = data;
                adapter.notifyDataSetChanged();

                adapter.setOnClickListener(position -> {
                });

                adapter.setOnMemeLikeListener((meme) -> true);

                adapter.setOnRemoveListener(meme -> {
                    alBuilder.setTitle("INFO").setMessage("Are you sure you want to delete the meme?").setPositiveButton("yes", (dialogInterface, i) -> MemeModel.instance.removeMeme(meme.getId(), result1 -> {
                        adapter.data.remove(meme);
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                        handleEmptyMemes(adapter.data);
                    })).setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());

                    alBuilder.show();
                });
            });
        }


        emptyMemesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = MyMemesFragmentDirections.actionMyMemesToCreateMeme();
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    private void handleEmptyMemes(List<Meme> memes) {
        if (memes.isEmpty()) {
            emptyMemesImage.setVisibility(View.VISIBLE);
            emptyMemesText.setVisibility(View.VISIBLE);
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