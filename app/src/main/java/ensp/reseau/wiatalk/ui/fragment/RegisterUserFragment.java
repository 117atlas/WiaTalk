package ensp.reseau.wiatalk.ui.fragment;


import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Future;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.SimulateProcessing;
import ensp.reseau.wiatalk.ui.activities.RegisterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUserFragment extends Fragment implements ChoosePpFragment.IChoosePp{

    private AppCompatButton next;
    private AppCompatEditText username;
    private FloatingActionButton changepp;
    private CircleImageView pp;

    private Uri imageFileUri;
    private String ppPath;
    private String ppUrl;
    private ProgressDialog progressDialog;

    public RegisterUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    public void initializeView(View view){
        next = view.findViewById(R.id.next);
        username = view.findViewById(R.id.username);
        changepp = view.findViewById(R.id.change_pp);
        pp = view.findViewById(R.id.pp);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pleasewait));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()){
                    username.setError(getString(R.string.empty_username));
                    return;
                }
                //NEXT
                //simulateProcessing();

                if (ppPath==null) registerUser();
                else registerUserWithPp(((RegisterActivity)getActivity()).getFinalUser().get_Id());

                //registerUser();
            }
        });

        changepp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePpFragment choosePpFragment = ChoosePpFragment.newInstance(RegisterUserFragment.this);
                choosePpFragment.show(getFragmentManager(), ChoosePpFragment.class.getSimpleName());
            }
        });
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
            imageFileUri = FilesUtils.getMyPpUri(((RegisterActivity)getActivity()).getFinalUser().get_Id(), getContext());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(intent, ChoosePpFragment.CHOICE_CAMERA);
        }
    }

    private boolean checkPermissions(String[] permissions, final ArrayList<String> permissionsDenied){
        if (Build.VERSION.SDK_INT >= 23) {
            boolean res = true;
            for (int i=0; i<permissions.length; i++){
                if (getContext().checkSelfPermission(permissions[i])!=PackageManager.PERMISSION_GRANTED){
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
                    String imageFilePath =  FilesUtils.copyToMyPp(FilesUtils.UriToPath(imageFileUri, getContext()),
                            ((RegisterActivity)getActivity()).getFinalUser().get_Id(), getContext());
                    //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                    ppPath = imageFilePath;
                    ((RegisterActivity)getActivity()).getFinalUser().setPpPath(ppPath);
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
                //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                ppPath = imageFilePath;
                ((RegisterActivity)getActivity()).getFinalUser().setPpPath(ppPath);
                showPp();
            }
            else{

            }
        }

    }

    private void showPp(){
        pp.setImageURI(imageFileUri);
    }

    public void next(){
        ((RegisterActivity)getActivity()).swipe(4);
    }

    private void simulateProcessing(){
        new SimulateProcessing(getContext(), this).execute();
    }

    private void registerUserWithPp(String id){
        File f = new File(ppPath);
        Future uploading = Ion.with(getContext())
                .load(NetworkAPI.BASE_URL+"users/uploadpp/"+id)
                .progressDialog(progressDialog)
                .setMultipartFile("image", f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                        try {
                            if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                            if (e!=null) throw e;

                            JSONObject jobj = new JSONObject(result.getResult());
                            boolean error = jobj.getBoolean("error");
                            String message = jobj.getString("message");
                            String path = jobj.getString("path");
                            ppUrl = path;
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            if (!error) registerUser();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private void registerUser(){
        User user = new User();
        user.setMobile(((RegisterActivity)getActivity()).getMobile());
        user.setPseudo(username.getText().toString());
        user.setPp(ppUrl==null?"0":ppUrl);

        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.GetUserResponse> registerUserCall = userInterface.registerUser(user);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        registerUserCall.enqueue(new Callback<UserInterface.GetUserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.GetUserResponse> call, Response<UserInterface.GetUserResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()!=null){
                    Log.d(RegisterMobileFragment.class.getSimpleName(), response.body().getMessage() + " --- " + response.body().isError());
                    if (response.body().isError()){
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ((RegisterActivity)getActivity()).setFinalUser(response.body().getUser());
                        ((RegisterActivity)getActivity()).getFinalUser().setPpPath(ppPath);
                        Log.d(RegisterUserFragment.class.getSimpleName(), response.body().getUser().toString());
                        next();
                    }
                }
                else Log.d(RegisterMobileFragment.class.getSimpleName(), "Response is empty");
            }

            @Override
            public void onFailure(Call<UserInterface.GetUserResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.d(RegisterMobileFragment.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void downloadPp(){
        String ppurl = ((RegisterActivity)getActivity()).getFinalUser().getPp();
        Log.d("PPURL", ppurl==null?"null":ppurl);
        if (ppurl != null && !ppurl.equals("0") && !ppurl.isEmpty()){
            final File ppfile = FilesUtils.getMyPpFile(((RegisterActivity)getActivity()).getFinalUser().get_Id(), getContext());
            Ion.with(getContext())
                .load(NetworkAPI.SERVER_URL+ppurl.replace("\\", "/"))
                .write(ppfile)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        // do stuff with the File or error
                        if (e!=null) {
                            e.printStackTrace();
                            return;
                        }
                        imageFileUri = Uri.fromFile(ppfile);
                        ppUrl = ((RegisterActivity)getActivity()).getFinalUser().getPp();
                        ppPath = ppfile.getAbsolutePath();
                        ((RegisterActivity)getActivity()).getFinalUser().setPpPath(ppfile.getAbsolutePath());
                        showPp();
                    }
                });
        }

    }

    public void _bind(){
        downloadPp();
        username.setText(((RegisterActivity)getActivity()).getFinalUser().getPseudo());
    }
}
