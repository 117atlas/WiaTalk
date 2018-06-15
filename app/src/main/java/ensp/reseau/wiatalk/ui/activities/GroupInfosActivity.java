package ensp.reseau.wiatalk.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Future;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.model.AdminsGroups;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.model.UsersGroups;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.GroupInterface;
import ensp.reseau.wiatalk.network.GroupInterface.GetGroupResponse;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.ui.IntentExtra;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.fragment.AdminsOptionsBottomSheetFragment;
import ensp.reseau.wiatalk.ui.fragment.ChoosePpFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class GroupInfosActivity extends AppCompatActivity implements ChoosePpFragment.IChoosePp{

    private static final int ADD_MEMBERS_CODE = 488;
    private ImageView pp;
    private Toolbar toolbar;
    private EmojiTextView groupName;
    private ImageButton editGroupName;
    private TextView groupCreationInfos;
    private AppCompatButton sharedFiles;
    private RecyclerView groupMembers;
    private AppCompatButton quitGroup;
    private AppCompatButton addMembers;

    private ContactsAdapter contactsAdapter;

    private Group group;

    private Uri imageFileUri;
    private String ppGroup;
    private String oldPpGroup;

    private User me;

    private String ppGroupBackup;
    private String groupNameBackup;

    private void setMe(){
        if (me==null){
            try{
                me = WiaTalkApp.getMe(this);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_infos);
        //group = (Group) getIntent().getSerializableExtra(Group.class.getSimpleName());
        getGroup();
        initializeWidgets();
        bind();
    }

    private void initializeWidgets(){
        pp = findViewById(R.id.pp);
        toolbar = findViewById(R.id.toolbar);
        groupName = findViewById(R.id.group_name);
        editGroupName = findViewById(R.id.edit_group_name);
        groupCreationInfos = findViewById(R.id.group_creation_date);
        sharedFiles = findViewById(R.id.group_files);
        quitGroup = findViewById(R.id.quit);
        groupMembers = findViewById(R.id.group_members);
        addMembers = findViewById(R.id.add_members);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        quitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfosActivity.this, "QUIT GROUP", Toast.LENGTH_SHORT).show();
            }
        });
        sharedFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfosActivity.this, "SHARED FILES", Toast.LENGTH_SHORT).show();
            }
        });
        editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfosActivity.this, ChangeGroupNameActivity.class);
                intent.putExtra(ChangeGroupNameActivity.class.getSimpleName(), group.getName());
                startActivityForResult(intent, ChangeGroupNameActivity.CHANGE_GROUP_NAME_CODE);
            }
        });
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentExtra intentExtra = new IntentExtra(Group.class.getSimpleName(), group);
                Intent intent = new Intent(GroupInfosActivity.this, AddMembersActivity.class);
                intent.putExtra(Group.class.getSimpleName(), group);
                startActivityForResult(intent, ADD_MEMBERS_CODE);
                //UiUtils.switchActivity(GroupInfosActivity.this, AddMembersActivity.class, false, intentExtra);
            }
        });

        contactsAdapter = new ContactsAdapter(this, ContactsAdapter.TYPE_LIST_CONTACTS_USERS_IN_GROUP, null);
        groupMembers.setLayoutManager(new LinearLayoutManager(this));
        groupMembers.setAdapter(contactsAdapter);
    }

    private void getGroup(){
        group = LocalStorageDiscussions.getGroupById("5b0e49cbf65c8b2db81eac04", this);
        LocalStorageDiscussions.populateGroup(group, this);
    }

    private void bind(){
        getSupportActionBar().setTitle(group.getName());
        groupName.setText(group.getName());

        if (false /*(group.getOld_pp_change_timestamp()<group.getPp_change_timestamp())*/){
            downloadPp();
        }
        else{
            File file = new File(group.getPpPath());
            if (file.exists()) UiUtils.showImage(this, pp, group.getPpPath());
            else{
                downloadPp();
            }
        }

        ArrayList<User> members = new ArrayList<>();
        if (group.getMembers()!=null){
            for (UsersGroups usersGroups: group.getMembers()) if (usersGroups.isIs_in_group()) members.add(usersGroups.getMember());
        }
        ArrayList<User> admins = new ArrayList<>();
        if (group.getAdmins()!=null){
            for (AdminsGroups adminsGroups: group.getAdmins()) admins.add(adminsGroups.getAdmin());
        }
        contactsAdapter.setAdmins(admins);
        contactsAdapter.setGroupInfosActivity(this);
        contactsAdapter.setUsers(members);
        groupCreationInfos.setText(getString(R.string.group_creation_date).replace("?????", U.formatTimestamp(group.getCreation_date()))
                .replace("????", "~"+group.getCreator().getPseudo()));
    }

    private void downloadPp(){
        NetworkUtils.downloadPp(this, group.get_id(), group.getPp(), new NetworkUtils.IFileDownload() {
            @Override
            public void onFileDownloaded(boolean error, String path) {
                if (!error) {
                    group.setPpPath(path);
                    group.setOld_pp_change_timestamp(group.getPp_change_timestamp());
                    LocalStorageDiscussions.storeGroup(group, GroupInfosActivity.this);
                    UiUtils.showImage(GroupInfosActivity.this, pp, path);
                    Log.d("DL PP GRP", "Group Small Adapter - Complete");
                }
                else{
                    UiUtils.showImage(GroupInfosActivity.this, pp, group.getPp(), true);
                    Log.d("DL PP GRP", "Group Small Adapter - Complete");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_infos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_pp:{
                ChoosePpFragment choosePpFragment = ChoosePpFragment.newInstance(this);
                choosePpFragment.show(getSupportFragmentManager(), ChoosePpFragment.class.getSimpleName());
            } break;
            case R.id.remove_pp:{
                ppGroupBackup = group.getPp();
                group.setPp("0");
                updateGroup();
            } break;
            case R.id.edit_group_name:{
                Intent intent = new Intent(GroupInfosActivity.this, ChangeGroupNameActivity.class);
                intent.putExtra(ChangeGroupNameActivity.class.getSimpleName(), group.getName());
                startActivityForResult(intent, ChangeGroupNameActivity.CHANGE_GROUP_NAME_CODE);
            } break;
            case R.id.group_files:{

            } break;
            case R.id.quit:{

            } break;
        }
        return super.onOptionsItemSelected(item);
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
            imageFileUri = FilesUtils.getOthersPpsUri("temp", this);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(intent, ChoosePpFragment.CHOICE_CAMERA);
        }
    }

    private boolean checkPermissions(String[] permissions, final ArrayList<String> permissionsDenied){
        if (Build.VERSION.SDK_INT >= 23) {
            boolean res = true;
            for (int i=0; i<permissions.length; i++){
                if (checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED){
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

        if (requestCode == ChangeGroupNameActivity.CHANGE_GROUP_NAME_CODE){
            //Edit name
            if (data!=null){
                String newName = data.getStringExtra(ChangeGroupNameActivity.class.getSimpleName());
                if (!group.getName().equals(newName)){
                    groupNameBackup = group.getName();
                    group.setName(newName);
                    updateGroup();
                }
            }

        }
        else if (requestCode == ADD_MEMBERS_CODE){
            if (data!=null){
                Group addMemberResult = (Group) data.getSerializableExtra(Group.class.getSimpleName());
                LocalStorageDiscussions.storeGroupInfos(addMemberResult, this);
                getGroup();
                bind();
            }
        }

        else{
            if (requestCode == ChoosePpFragment.CHOICE_GALLERY){
                if (resultCode == RESULT_OK && data!=null && data.getData()!=null){
                    imageFileUri = data.getData();
                    try{
                        String imageFilePath =  FilesUtils.copyToOthersPp(FilesUtils.UriToPath(imageFileUri, this),
                                "temp", this);
                        ppGroup = imageFilePath;
                        //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                        ppGroupBackup = group.getPp();
                        updateGroupWithPp();
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
                    ppGroupBackup = group.getPp();
                    updateGroupWithPp();
                    showPp();
                }
                else{

                }
            }
            //((CreateGroupActivity)getActivity()).setGroupPp(ppGroup);
            System.out.println(ppGroup);
        }

    }

    private void showPp(){
        pp.setImageURI(imageFileUri);
    }

    private void updateGroup(){
        Group _group = new Group();
        _group.set_id(group.get_id());
        _group.setName(group.getName());
        _group.setPp(group.getPp()+"|"+!group.getPp().equals(ppGroupBackup));

        setMe();

        if (me==null){
            Log.e("CREATE GROUP", "ME NULL");
            return;
        }

        final GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<BaseResponse> updateGroup = groupInterface.updateGroup(_group, me.get_Id());

        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();

        updateGroup.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("UPDATE GROUP", "Response body is null");
                    group.setPp(ppGroupBackup);
                    group.setName(groupNameBackup);
                    return;
                }
                Log.i("UPDATE GROUP", "Response error " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("UPDATE GROUP", "Response error " + response.body().getMessage());
                    group.setPp(ppGroupBackup);
                    group.setName(groupNameBackup);
                    return;
                }
                //NEXT
                Toast.makeText(GroupInfosActivity.this, "Groupe updated successfully", Toast.LENGTH_SHORT).show();
                //finish();
                if (group.getPp()==null || group.getPp().isEmpty() || group.getPp().equals("0")) group.setPpPath("0");
                LocalStorageDiscussions.updateGroup(group, GroupInfosActivity.this);
                bind();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("UPDATE GROUP", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void updateGroupWithPp(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        File f = new File(ppGroup);

        String newPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + group.get_id() + ".jpg";
        if (f.renameTo(new File(newPath))) f = new File(newPath);

        System.out.println("RENAME FILE " + f.getAbsolutePath());

        System.out.println(NetworkAPI.BASE_URL+"groups/uploadpp/"+group.get_id());
        Future uploading = Ion.with(this)
                .load(NetworkAPI.BASE_URL+"groups/uploadpp/"+group.get_id())
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
                            group.setPp(path);
                            Toast.makeText(GroupInfosActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (!error) updateGroup();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private void removeMember(String memberId){
        GroupInterface groupInterface  = NetworkAPI.getClient().create(GroupInterface.class);

        setMe();
        if (me==null){
            Log.e("CREATE GROUP", "ME NULL");
            return;
        }

        Call<GetGroupResponse> removeMember = groupInterface.removeMemberFromGroup(group.get_id(), memberId, me.get_Id());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();
        removeMember.enqueue(new Callback<GetGroupResponse>() {
            @Override
            public void onResponse(Call<GetGroupResponse> call, Response<GetGroupResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("REMOVE MEMBER", "Response body is null");
                    return;
                }
                Log.i("REMOVE MEMBER", "Response " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("REMOVE MEMBER", "Response error " + response.body().getMessage());
                    return;
                }
                LocalStorageDiscussions.storeGroupInfos(response.body().getGroup(), GroupInfosActivity.this);
                getGroup();
                bind();
            }

            @Override
            public void onFailure(Call<GetGroupResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                //Log.e("REMOVE MEMBER", t.getMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    private void removeAdmin(String adminId){
        GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<GetGroupResponse> removeAdmin = groupInterface.removeAdminForGroup(group.get_id(), adminId);
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();
        removeAdmin.enqueue(new Callback<GetGroupResponse>() {
            @Override
            public void onResponse(Call<GetGroupResponse> call, Response<GetGroupResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("REMOVE ADMIN", "Response body is null");
                    return;
                }
                Log.i("REMOVE ADMIN", "Response " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("REMOVE ADMIN", "Response error " + response.body().getMessage());
                    return;
                }
                LocalStorageDiscussions.storeGroupInfos(response.body().getGroup(), GroupInfosActivity.this);
                group = LocalStorageDiscussions.getGroupById(group.get_id(), GroupInfosActivity.this);
                bind();
            }

            @Override
            public void onFailure(Call<GetGroupResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("ADD ADMIN", t.getMessage());
                return;
            }
        });
    }

    private void addAdmin(String adminId){
        GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<GetGroupResponse> removeAdmin = groupInterface.addAdminForGroup(group.get_id(), adminId);
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();
        removeAdmin.enqueue(new Callback<GetGroupResponse>() {
            @Override
            public void onResponse(Call<GetGroupResponse> call, Response<GetGroupResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("ADD ADMIN", "Response body is null");
                    return;
                }
                Log.i("ADD ADMIN", "Response " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("ADD ADMIN", "Response error " + response.body().getMessage());
                    return;
                }
                LocalStorageDiscussions.storeGroupInfos(response.body().getGroup(), GroupInfosActivity.this);
                getGroup();
                bind();
            }

            @Override
            public void onFailure(Call<GetGroupResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("ADD ADMIN", t.getMessage());
                return;
            }
        });
    }

    public void groupMembersOptionChoosen(User user, int option){
        switch (option){
            case AdminsOptionsBottomSheetFragment.OPTION_MESSAGE:{
                //UiUtils.switchActivity((AppCompatActivity)context, DiscussionActivity.class, true, null);
            } break;
            case AdminsOptionsBottomSheetFragment.OPTION_NOMINATE_ADMIN:{
                addAdmin(user.get_Id());
            } break;
            case AdminsOptionsBottomSheetFragment.OPTION_REMOVE_MEMBER:{
                removeMember(user.get_Id());
            } break;
            case AdminsOptionsBottomSheetFragment.OPTION_VIEW_PROFILE:{
                //VIEW PROFILE
            } break;
        }
    }

}
