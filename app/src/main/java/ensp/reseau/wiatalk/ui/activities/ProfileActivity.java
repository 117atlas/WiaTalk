package ensp.reseau.wiatalk.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.ui.fragment.ChoosePpFragment;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupSetInfosFragment;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity implements ChoosePpFragment.IChoosePp{

    private Toolbar toolbar;
    private CircleImageView pp;
    private FloatingActionButton changePp;
    private EmojiTextView userName;
    private ImageButton editUserName;
    private TextView userMobile;
    private ImageButton editUserMobile;

    private User user;

    private Uri imageFileUri;
    private String myPp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        randomUser();
        initializeWidgets();
        bind();
    }

    private void initializeWidgets(){
        toolbar = findViewById(R.id.toolbar);
        pp = findViewById(R.id.pp);
        changePp = findViewById(R.id.change_pp);
        userName = findViewById(R.id.username);
        editUserName = findViewById(R.id.edit_username);
        userMobile = findViewById(R.id.usermobile);
        editUserMobile = findViewById(R.id.edit_usermobile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.myprofile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changePp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePpFragment choosePpFragment = ChoosePpFragment.newInstance(ProfileActivity.this);
                choosePpFragment.show(getSupportFragmentManager(), ChoosePpFragment.class.getSimpleName());
            }
        });
        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "EDIT USERNAME", Toast.LENGTH_SHORT).show();
            }
        });
        editUserMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "EDIT USERMOBILE", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void randomUser(){
        user = new User();
        user.setId("0"); user.setMobile("697266488");
        user.setPseudo("samar"); user.setContactName("Samaritain Sims");
        user.setPp("pp2.jpg");
    }

    private void bind(){
        userName.setText(user.getPseudo());
        userMobile.setText(user.getMobile());
        U.showImageAsset(this, user.getPp(), pp);
    }

    @Override
    public void choiceForPp(int choice) {
        switch (choice){
            case ChoosePpFragment.CHOICE_GALLERY: {
                galleryChoice();
            } break;
            case ChoosePpFragment.CHOICE_CAMERA: {
                cameraChoice();
            } break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void galleryChoice(){
        ArrayList<String> permsDenied = new ArrayList<>();
        boolean perms = checkPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsDenied);
        if (!perms){
            ActivityCompat.requestPermissions(this, U.toArray(permsDenied), ChoosePpFragment.CHOICE_GALLERY);
            return;
        }
        else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, this.getString(R.string.profile_img_chooser_gallery_title)), ChoosePpFragment.CHOICE_GALLERY);;
        }
    }

    private void cameraChoice(){
        ArrayList<String> permsDenied = new ArrayList<>();
        boolean perms = checkPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsDenied);
        if (!perms){
            ActivityCompat.requestPermissions(this, U.toArray(permsDenied), ChoosePpFragment.CHOICE_CAMERA);
            return;
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFileUri = FilesUtils.getOutputImageFileUriForGroupCreated(0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(intent, ChoosePpFragment.CHOICE_CAMERA);
        }
    }

    private boolean checkPermissions(String[] permissions, final ArrayList<String> permissionsDenied){
        if (Build.VERSION.SDK_INT >= 23) {
            boolean res = true;
            for (int i=0; i<permissions.length; i++){
                if (this.checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED){
                    res = false;
                    permissionsDenied.add(permissions[i]);
                }
            }
            return res;
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ChoosePpFragment.CHOICE_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryChoice();
                } else {
                    Toast.makeText(this, getString(R.string.gallery_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case ChoosePpFragment.CHOICE_CAMERA : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraChoice();
                } else {
                    Toast.makeText(this, getString(R.string.camera_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ChoosePpFragment.CHOICE_GALLERY){
            if (resultCode == RESULT_OK && data!=null && data.getData()!=null){
                imageFileUri = data.getData();
                try{
                    String imageFilePath =  FilesUtils.copyToWiaTalkImagesDirectory(FilesUtils.UriToPath(imageFileUri, this), 0);
                    myPp = imageFilePath;
                    //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                    showPp();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{

            }
        }
        else if (requestCode == ChoosePpFragment.CHOICE_CAMERA){
            if (resultCode == RESULT_OK){
                String imageFilePath = imageFileUri.getPath();
                myPp = imageFilePath;
                //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                showPp();
            }
            else{

            }
        }
        System.out.println(myPp);
    }

    private void showPp(){
        pp.setImageURI(imageFileUri);
    }
    
}
