package com.example.extrememe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.extrememe.model.Category;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;
import com.example.extrememe.services.CategoryService;
import com.example.extrememe.services.LoginService;
import com.example.extrememe.utils.CategoryViewUtils;
import com.example.extrememe.utils.ColorUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateMemeFragment extends Fragment {
    private Button btnChoose;
    private Button btnUpload;
    private ImageView imageView;
    private EditText memeDescription;
    StorageReference storageReference;
    private List<String> selectedCategories = new ArrayList<>();
    private Button randomButton;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;


    private MenuItem signOutButton;
    private MenuItem signInButton;
    private MenuItem loggedInUsername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_create_meme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.storageReference = FirebaseStorage.getInstance().getReference();
        initializeUI();
    }

    private void initializeUI() {
        this.initRandomButton();
        btnChoose = this.getActivity().findViewById(R.id.choose_image);
        btnUpload = this.getActivity().findViewById(R.id.upload_button);
        imageView = this.getActivity().findViewById(R.id.meme_to_upload);
        memeDescription = this.getActivity().findViewById(R.id.editTextTextPersonName);

        btnChoose.setOnClickListener(v -> chooseImage());
        btnUpload.setOnClickListener(v -> uploadImage());
    }

    @Override
    public void onResume() {
        super.onResume();
        new CategoryService().getMemeCategories(categories -> {
            for (Category category : categories) {
                this.addCategoryButtonToView(category);
            }
            this.initRandomButton();
        });
    }

    private void addCategoryButtonToView(Category category) {
        Button categoryButton = new CategoryViewUtils()
                .generateCategoryButton(category, this.getContext(), getResources(),
                        getView().findViewById(R.id.categories_panel_create_meme));

        categoryButton.setOnClickListener(onClickCategory());
        this.unselectButtonView(categoryButton);
    }

    private View.OnClickListener onClickCategory() {
        return v -> this.selectCategory((Button) v);
    }

    private void initRandomButton() {
        this.randomButton = getView().findViewById(R.id.random_button_create_meme);

        randomButton.setOnClickListener(button -> this.selectCategory((Button) button));

        if(this.selectedCategories != null && this.selectedCategories.size() == 0) {
            this.selectButtonView(this.randomButton);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("memes/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnCompleteListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        uploadMemeWithPublicURL(taskSnapshot);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(CreateMemeFragment.this.getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }

    }

    private Meme createMemeObject(String memeURL) {
        Meme uploadedMeme = new Meme();
        uploadedMeme.setId(UUID.randomUUID().toString());
        uploadedMeme.setUserId(LoginService.getInstance(this.getContext()).getFirebaseUser().getUid());
        uploadedMeme.setCategories(this.selectedCategories);
        uploadedMeme.setDescription(this.memeDescription.getText().toString());
        uploadedMeme.setImageUrl(memeURL);
        uploadedMeme.setUsersLikes(new ArrayList<>());

        return uploadedMeme;
    }

    private Task<UploadTask.TaskSnapshot> uploadMemeWithPublicURL(Task<UploadTask.TaskSnapshot> taskSnapshot) {
        return taskSnapshot.addOnCompleteListener((res) -> {
            res.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(memeURL -> {
                MemeModel.instance.insertMeme(createMemeObject(memeURL.getResult().toString()), (result) -> {
                    Toast.makeText(CreateMemeFragment.this.getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(this.getActivity(), R.id.mainactivity_navhost).navigateUp();
                });
            });
        });
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

    private void selectCategory(Button button) {
        if (button.getId() == R.id.random_button_create_meme) {
            this.selectButtonView(this.randomButton);
            this.unselectAllCategories();
            this.selectedCategories.clear();
            return;
        }

        for (String selectedCategoryId : selectedCategories) {
            if (button.getTag().equals(selectedCategoryId)) {
                this.unselectButtonView(button);
                selectedCategories.remove(selectedCategoryId);
                if (selectedCategories.size() == 0) {
                    this.selectButtonView(this.randomButton);
                }
                return;
            }
        }

        selectButtonView(button);
        unselectButtonView(this.randomButton);
        selectedCategories.add(button.getTag().toString());
    }

    private void unselectAllCategories() {
        for (String selectedCategoryId : selectedCategories) {
            this.unselectButtonView(getView().findViewWithTag(selectedCategoryId));
        }
    }

    private void unselectButtonView(Button button) {
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(ColorUtils.getInstance(getResources()).getColorByResourceId(R.color.unselected_category));
    }

    private void selectButtonView(Button button) {
        button.setBackgroundColor(ColorUtils.getInstance(getResources()).getColorByResourceId(R.color.purple_500));
        button.setTextColor(Color.WHITE);
    }
}