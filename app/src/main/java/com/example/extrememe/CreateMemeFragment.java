package com.example.extrememe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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

import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;
import com.example.extrememe.services.LoginService;
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

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_meme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.storageReference = FirebaseStorage.getInstance().getReference();
        initializeUI();
    }

    private void initializeUI() {
        btnChoose = this.getActivity().findViewById(R.id.choose_image);
        btnUpload = this.getActivity().findViewById(R.id.upload_button);
        imageView = this.getActivity().findViewById(R.id.meme_to_upload);
        memeDescription = this.getActivity().findViewById(R.id.editTextTextPersonName);

        btnChoose.setOnClickListener(v -> chooseImage());
        btnUpload.setOnClickListener(v -> uploadImage());
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
        uploadedMeme.setCategories(this.getSelectedCategories()); //TODO - really use categories
        uploadedMeme.setDescription(this.memeDescription.getText().toString());
        uploadedMeme.setImageUrl(memeURL);
        uploadedMeme.setUsersLikes(new ArrayList<>());
        System.out.println(memeURL);

        return uploadedMeme;
    }

    private List<String> getSelectedCategories() {
        return null;
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
}