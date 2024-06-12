package com.example.appmovie.Frag;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.appmovie.Dto.UserManager;
import com.example.appmovie.Model.FavourFilm;
import com.example.appmovie.Model.User;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.MovieAdapter;
import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.SignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001;
    private static final int REQUEST_CODE_IMAGE_PICK = 1002;
    private TextView txtUserName, txtUserEmail;
    ListView listView;
    User user = UserManager.getInstance().getCurrentUser();
    private ImageView imgUser;
    private ImageView dialogImageView;

    private Uri selectedImageUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageButton logoutButton = view.findViewById(R.id.logout_button);
        Button editProfileButton = view.findViewById(R.id.edit_profile_button);

        txtUserName = view.findViewById(R.id.user_name);
        txtUserEmail = view.findViewById(R.id.user_email);
        imgUser = view.findViewById(R.id.profile_image);

        initView(view);

        if (user != null) {

            txtUserName.setText(user.Name);
            txtUserEmail.setText(user.Email);
            Glide.with(this).load(user.Image).into(imgUser);
            for(FavourFilm fv: user.Favour_film){
                Log.d("FILM", fv.origin_name);
            }
            favourMovie(user.Favour_film);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfilePopup(user);
            }
        });
        return view;
    }

    private void favourMovie(ArrayList<FavourFilm> films) {
        if (getContext() == null) {
            Log.e("ProfileFragment", "Context is null, cannot set adapter.");
            return;
        }
        ArrayList<FavourFilm> newFilms = new ArrayList<>(films);
        MovieAdapter adapter = new MovieAdapter(getContext(), R.layout.layout_item_fav, newFilms);

        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavourFilm film) {
                onClickGoToDetail(film);
            }
        });

        listView.setAdapter(adapter);
    }



    private void initView(View view) {
        listView = view.findViewById(R.id.listViewMovies);
    }

    private void onClickGoToDetail(FavourFilm film) {
        String slug = film.slug;
        Intent it = new Intent(getActivity(), MovieDetail.class);
        Bundle bd = new Bundle();
        bd.putString("slug",slug);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }

    private void showEditProfilePopup(User user) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.edit_profile_popup);
        EditText editTextUsername = dialog.findViewById(R.id.editTextUsername);
        EditText editTextEmail = dialog.findViewById(R.id.editTextEmail);
        Button saveButton = dialog.findViewById(R.id.save_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        dialogImageView = dialog.findViewById(R.id.imageView);

        editTextUsername.setText(user.Name);
        editTextEmail.setText(user.Email);

        Glide.with(this)
                .load(user.Image)
                .apply(new RequestOptions().placeholder(R.drawable.circle_avatar))
                .into(dialogImageView);

        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextUsername.getText().toString();
                String newEmail = editTextEmail.getText().toString();

                if (Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Map<String, Object> updatedUserData = new HashMap<>();
                    updatedUserData.put("Name", newUsername);
                    updatedUserData.put("Email", newEmail);

                    if (selectedImageUri != null) {
                        uploadImageToFirebase(selectedImageUri, user, updatedUserData);
                    } else {
                        updateUserData(user, updatedUserData);
                    }
                } else {
                    Toast.makeText(ProfileFragment.this.getActivity(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Update the ImageView with the selected image
                Glide.with(this).load(selectedImageUri).into(dialogImageView);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, User user, Map<String, Object> updatedUserData) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("profile_images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String newImageUrl = uri.toString();
                        updatedUserData.put("Image", newImageUrl);
                        updateUserData(user, updatedUserData);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(User user, Map<String, Object> updatedUserData) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("Users").document(userId)
                    .update(updatedUserData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.Name = (String) updatedUserData.get("Name");
                            user.Email = (String) updatedUserData.get("Email");
                            if (updatedUserData.containsKey("Image")) {
                                user.Image = (String) updatedUserData.get("Image");
                            }
                            UserManager.getInstance().setCurrentUser(user);
//                            dialog.dismiss();
                            Toast.makeText(ProfileFragment.this.getActivity(), "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();

                            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                            ft.replace(R.id.container, new ProfileFragment()).commit();
                            ProfileFragment profileFragment = (ProfileFragment) getParentFragmentManager().findFragmentByTag("ProfileFragment");
                            if (profileFragment != null) {
                                profileFragment.onResume();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileFragment.this.getActivity(), "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}