package com.example.extrememe;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MemesAdapter;
import com.example.extrememe.model.Category;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;
import com.example.extrememe.services.CategoryService;
import com.example.extrememe.services.LoginService;
import com.example.extrememe.utils.LayoutUnitUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

public class MainFeedFragment extends Fragment {
    private static final String TAG = "MainFeedFragment";
    private final int SELECTED_CATEGORY = Color.MAGENTA;
    private final int UNSELECTED_CATEGORY = Color.GRAY;
    private List<Meme> allMemes = new ArrayList<>();
    private List<Meme> filteredMemes = new ArrayList<>();
    private List<String> selectedCategories = new ArrayList<>();
    private MemesAdapter adapter;
    private MenuItem signOutButton;
    private MenuItem signInButton;
    private MenuItem loggedInUsername;
    private BottomNavigationView bottomNavigationView;
    private TextView noMemesFilteredText;
    private ImageView noMemesFilteredImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);

        setHasOptionsMenu(true);

        RecyclerView memesRv = view.findViewById(R.id.allMemes_rv);
        memesRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);

        adapter = new MemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);

        this.noMemesFilteredImage = view.findViewById(R.id.mainfeed_iv_not_found);
        this.noMemesFilteredText = view.findViewById(R.id.mainfeed_tv_not_found);

        initBottomNavigationView();

        return view;
    }

    private void initBottomNavigationView() {
        bottomNavigationView = ((MainActivity) this.getContext()).findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this.getContext());

        new CategoryService().getMemeCategories(categories -> {
            for (Category category : categories) {
                this.addCategoryButtonToView(category);
            }
            this.initRandomButton();
        });

        MemeModel.instance.getAllMemes(result -> {
            allMemes = result;
            filterMemes();

            adapter.setOnClickListener((position) -> {
            });

            adapter.setOnMemeLikeListener((meme) -> {
                if (LoginService.getInstance(MainFeedFragment.super.getContext()).isLoggedIn()) {
                    likeMeme(meme);
                } else {
                    alBuilder.setTitle("FAILED").setMessage("Please log in to like memes :)");
                    alBuilder.show();
                }

                return true;
            });

            adapter.setOnRemoveListener(meme -> {
            });
        });

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

        MemeModel.instance.updateMeme(meme, new MemeModel.UpdateMemeListener() {
            @Override
            public void onComplete(Void result) {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_top_menu, menu);
        loggedInUsername = menu.findItem(R.id.logged_in_display_name);
        signInButton = menu.findItem(R.id.google_sign_in_button);
        signOutButton = menu.findItem(R.id.google_sign_out_button);

        if (LoginService.getInstance(this.getContext()).getGoogleAccount() != null && !LoginService.getInstance(this.getContext()).isLoggedIn()) {
            LoginService.getInstance(this.getContext()).signOut();
        }

        setSignedInView(LoginService.getInstance(this.getContext()).getUserDisplayName(),
                LoginService.getInstance(this.getContext()).isLoggedIn());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.google_sign_in_button:
                signIn();
                return true;
            case R.id.google_sign_out_button:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRandomButton() {
        getView().findViewById(R.id.random_button).setOnClickListener(randomButton -> {
            for (String selectedCategoryId : selectedCategories) {
                getView().findViewWithTag(selectedCategoryId).setBackgroundColor(UNSELECTED_CATEGORY);
            }
            this.selectCategory((Button) randomButton);
            this.filterMemes();
        });
    }

    private void filterMemes() {
        this.filteredMemes.clear();
        boolean isMemeDisplayed;

        if (selectedCategories != null && selectedCategories.size() > 0) {
            for (Meme meme : allMemes) {
                isMemeDisplayed = false;

                if (meme.getCategories() != null) {
                    for (String categoryId : meme.getCategories()) {
                        for (String selectedCategoryId : selectedCategories) {
                            if (selectedCategoryId.equals(categoryId)) {
                                isMemeDisplayed = true;
                            }
                        }
                    }
                }

                if (isMemeDisplayed) {
                    this.filteredMemes.add(meme);
                }
            }
        } else {
            this.filteredMemes = (List<Meme>) ((ArrayList<Meme>) allMemes).clone();
        }

        this.adapter.data = filteredMemes;
        this.adapter.notifyDataSetChanged();
        handleNoFilteredMemes(filteredMemes);
    }

    private void handleNoFilteredMemes(List<Meme> filteredMemes) {
        if (filteredMemes.isEmpty()) {
            this.noMemesFilteredText.setVisibility(View.VISIBLE);
            this.noMemesFilteredImage.setVisibility(View.VISIBLE);
        } else {
            this.noMemesFilteredText.setVisibility(View.INVISIBLE);
            this.noMemesFilteredImage.setVisibility(View.INVISIBLE);
        }
    }

    private void addCategoryButtonToView(Category category) {
        final int categoryButtonWidth = 110;
        final int categoryButtonHeight = 50;
        final int margin = 5;

        Button myButton = new Button(this.getContext());
        myButton.setText(category.getName());
        myButton.setWidth(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonWidth, getResources().getDisplayMetrics()));
        myButton.setHeight(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonHeight, getResources().getDisplayMetrics()));

        LinearLayout ll = getView().findViewById(R.id.categoriesPanel);
        int marginInDP = LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(margin, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(marginInDP, marginInDP, marginInDP, marginInDP);

        myButton.setOnClickListener(onClickCategory());

        myButton.setTag(category.getId());
        ll.addView(myButton, lp);
    }

    private View.OnClickListener onClickCategory() {
        return v -> {
            this.selectCategory((Button) v);
            this.filterMemes();
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoginService.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                LoginService.getInstance(this.getContext()).setGoogleAccount(account);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getDisplayName());
                Task<AuthResult> authResultTask = LoginService.getInstance(this.getContext()).firebaseAuthWithGoogle(account.getIdToken(), this.getActivity());

                authResultTask.addOnCompleteListener(this.getActivity(), firebaseLoginRes -> {
                    if (firebaseLoginRes.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        setSignedInView(LoginService.getInstance(this.getContext()).getGoogleAccount().getDisplayName(), true);
                        adapter.notifyDataSetChanged();
                        LoginService.getInstance(this.getContext()).createNewUser(authResultTask);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", firebaseLoginRes.getException());
                    }
                });

                setSignedInView(account.getDisplayName(), true);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            } catch (NullPointerException e) {
                Log.w(TAG, "Null pointer exception while login trial", e);
            }
        }
    }

    private void setSignedInView(String displayName, boolean isLoggedIn) {
        this.loggedInUsername.setTitle(displayName);
        this.signInButton.setVisible(!isLoggedIn);
        this.signOutButton.setVisible(isLoggedIn);
    }

    private void selectCategory(Button button) {
        if (button.getText().equals("Random")) {
            button.setBackgroundColor(SELECTED_CATEGORY);
            this.selectedCategories.clear();
            return;
        }

        for (String selectedCategoryId : selectedCategories) {
            if (button.getTag().equals(selectedCategoryId)) {
                button.setBackgroundColor(UNSELECTED_CATEGORY);
                selectedCategories.remove(selectedCategoryId);
                return;
            }
        }

        button.setBackgroundColor(SELECTED_CATEGORY);
        unselectRandomButton(UNSELECTED_CATEGORY);
        selectedCategories.add(button.getTag().toString());
    }

    private void unselectRandomButton(int unselectedButtonColor) {
        getView().findViewById(R.id.random_button).setBackgroundColor(unselectedButtonColor);
    }

    private void signIn() {
        Intent signInIntent = LoginService.getInstance(this.getContext()).getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, LoginService.RC_SIGN_IN);
    }

    private void signOut() {
        LoginService.getInstance(this.getContext()).signOut();
        setSignedInView(this.getString(R.string.default_sign_in_name_display), false);
        adapter.notifyDataSetChanged();
    }
}