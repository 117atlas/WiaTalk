package ensp.reseau.wiatalk.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.ui.activities.CreateGroupActivity;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupSetInfosFragment extends Fragment implements ChoosePpFragment.IChoosePp{

    private LinearLayout root;
    private CircleImageView pp;
    private FloatingActionButton changepp;
    private EmojiEditText groupName;
    private ImageView emoji;
    private RecyclerView members;
    private ContactsAdapter contactsAdapter;

    private EmojiPopup emojiPopup;

    private String groupNameText;
    private String ppGroup = "";

    private Uri imageFileUri;

    public void setSelectedUsers(ArrayList<User> users){
        contactsAdapter.setUsers(users);
    }

    public CreateGroupSetInfosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group_set_infos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);
        pp = view.findViewById(R.id.pp);
        changepp = view.findViewById(R.id.change_pp);
        groupName = view.findViewById(R.id.group_name);
        emoji = view.findViewById(R.id.emoji);
        members = view.findViewById(R.id.added_members);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(groupName);

        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) emojiPopup.dismiss();
                else emojiPopup.toggle();
            }
        });

        changepp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePpFragment choosePpFragment = ChoosePpFragment.newInstance(CreateGroupSetInfosFragment.this);
                choosePpFragment.show(getFragmentManager(), ChoosePpFragment.class.getSimpleName());
            }
        });

        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                groupNameText = charSequence.toString();
                ((CreateGroupActivity)getActivity()).groupNameTextWatcher(charSequence.length()>0 && charSequence.length()<=20);
                ((CreateGroupActivity)getActivity()).setGroupeName(groupNameText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        members.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsAdapter = new ContactsAdapter(getContext(), ContactsAdapter.TYPE_LIST_CONTACTS_USERS, null);
        members.setAdapter(contactsAdapter);
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
            ActivityCompat.requestPermissions(getActivity(), U.toArray(permsDenied), ChoosePpFragment.CHOICE_GALLERY);
            return;
        }
        else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getContext().getString(R.string.profile_img_chooser_gallery_title)), ChoosePpFragment.CHOICE_GALLERY);;
        }
    }

    private void cameraChoice(){
        ArrayList<String> permsDenied = new ArrayList<>();
        boolean perms = checkPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsDenied);
        if (!perms){
            ActivityCompat.requestPermissions(getActivity(), U.toArray(permsDenied), ChoosePpFragment.CHOICE_CAMERA);
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
                if (getContext().checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED){
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
                    Toast.makeText(getContext(), getString(R.string.gallery_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case ChoosePpFragment.CHOICE_CAMERA : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraChoice();
                } else {
                    Toast.makeText(getContext(), getString(R.string.camera_permissions_denied), Toast.LENGTH_SHORT).show();
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
                    String imageFilePath =  FilesUtils.copyToWiaTalkImagesDirectory(FilesUtils.UriToPath(imageFileUri, getContext()), 0);
                    ppGroup = imageFilePath;
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
                ppGroup = imageFilePath;
                //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                showPp();
            }
            else{

            }
        }
        ((CreateGroupActivity)getActivity()).setGroupPp(ppGroup);
        System.out.println(ppGroup);
    }

    private void showPp(){
        pp.setImageURI(imageFileUri);
    }

    public boolean enableNext(){
        return groupName.getText().toString().length()>0 && groupName.getText().toString().length()<=20;
    }

}
