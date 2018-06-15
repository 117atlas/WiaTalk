package ensp.reseau.wiatalk.ui.activities;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.GroupInterface;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.UnSwipeableViewPager;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupAddMembersFragment;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupSetInfosFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView numberAddedMembers;
    private ImageButton next;
    private UnSwipeableViewPager viewPager;

    private CreateGroupAddMembersFragment addMembersFragment;
    private CreateGroupSetInfosFragment setInfosFragment;
    private int currentPage = 0;

    private ArrayList<User> selectedUsers;
    private String groupeName;
    private String groupPp;
    private String groupPpUrl;
    private String groupId = "";

    private User me;

    private void setMe(){
        if (me==null){
            try{
                me = WiaTalkApp.getMe(this);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void setGroupeName(String groupeName) {
        this.groupeName = groupeName;
    }

    public void setGroupPp(String groupPp) {
        this.groupPp = groupPp;
        System.out.println("Activity create group "  + groupPp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        numberAddedMembers = findViewById(R.id.number_added);
        next = findViewById(R.id.next);
        viewPager = findViewById(R.id.viewpager);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage==0) swipe(1);
                else {
                    /*String toasts = "Groupe : " + groupeName + " Avec " + selectedUsers.size() + " membres\n";
                    toasts = toasts + "PP: " + ((groupPp==null)?"Aucune":groupPp) + "\n\n";
                    for (User user:selectedUsers){
                        toasts = toasts + user.getMobile() + " - " +user.getContactName() + "\n";
                    }
                    Toast.makeText(CreateGroupActivity.this, toasts, Toast.LENGTH_LONG).show();
                    Log.i("TOASTS", toasts);*/

                    createGroup();
                }
            }
        });

        setupViewPager();
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        addMembersFragment = new CreateGroupAddMembersFragment();
        setInfosFragment = new CreateGroupSetInfosFragment();
        viewPagerAdapter.addFragment(addMembersFragment, "STEP 1");
        viewPagerAdapter.addFragment(setInfosFragment, "STEP 2");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void swipe(int dest){
        if (dest==1) next.setEnabled(setInfosFragment.enableNext());
        else next.setEnabled(selectedUsers.size()!=0);

        if (dest<2) viewPager.setCurrentItem(dest);
        else UiUtils.switchActivity(this, MainActivity.class, true, null);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (currentPage==1) swipe(0);
        else {
            super.onBackPressed();
            finish();
        }
    }

    public void setSelectedUsers(ArrayList<User> selectedUsers){
        this.selectedUsers = selectedUsers;
        updateToolbarInfos();
        this.setInfosFragment.setSelectedUsers(selectedUsers);
    }

    private void updateToolbarInfos(){
        if (currentPage==0){
            if (selectedUsers!=null && selectedUsers.size()>0){
                numberAddedMembers.setText(getString(R.string.number_added).replace("???", String.valueOf(selectedUsers.size())));
                next.setEnabled(true);
            }
            else{
                numberAddedMembers.setText(getString(R.string.number_added).replace("???", "0"));
                next.setEnabled(false);
            }
        }
    }

    public void groupNameTextWatcher(boolean enableNextButton){
        next.setEnabled(enableNextButton);
    }

    private void updateGroup(){
        Group group = new Group();
        group.set_id(groupId);
        group.setName(groupeName);
        group.setPp(groupPpUrl);

        setMe();

        if (me==null){
            Log.e("CREATE GROUP", "ME NULL");
            return;
        }

        GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<BaseResponse> updateGroup = groupInterface.updateGroup(group, me.get_Id());

        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();

        updateGroup.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("UPDATE GROUP", "Response body is null");
                    return;
                }
                Log.i("UPDATE GROUP", "Response error " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("UPDATE GROUP", "Response error " + response.body().getMessage());
                    return;
                }
                //NEXT
                Toast.makeText(CreateGroupActivity.this, "Groupe created successfully", Toast.LENGTH_SHORT).show();
                //finish();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("UPDATE GROUP", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void uploadGroupPp(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        File f = new File(groupPp);

        String newPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + groupId + ".jpg";
        if (f.renameTo(new File(newPath))) f = new File(newPath);

        System.out.println("RENAME FILE " + f.getAbsolutePath());

        System.out.println(NetworkAPI.BASE_URL+"groups/uploadpp/"+groupId);
        Future uploading = Ion.with(this)
                .load(NetworkAPI.BASE_URL+"groups/uploadpp/"+groupId)
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
                            groupPpUrl = path;
                            Toast.makeText(CreateGroupActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (!error) updateGroup();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private void createGroup(){
        final Group group = new Group();
        group.setName(groupeName);
        group.setPp("");
        String members = "";

        setMe();

        if (me==null){
            Log.e("CREATE GROUP", "Error getting me");
            return;
        }

        members = me.get_Id();
        for (User u: selectedUsers) members = members + "|" + u.get_id();

        GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<BaseResponse> createGroup = groupInterface.createGroup(group, members);

        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();

        createGroup.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("CREATE GROUP", "Response body is null");
                    return;
                }
                Log.i("CREATE GROUP", "Response error " + response.body().getMessage());
                if (response.body().isError()){
                    Log.e("CREATE GROUP", "Response error " + response.body().getMessage());
                    return;
                }
                groupId = response.body().getMessage();
                if (groupPp!=null && !groupPp.isEmpty() && !groupPp.equals("0")) uploadGroupPp();
                else{
                    Toast.makeText(CreateGroupActivity.this, "Groupe created successfully", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("UPDATE GROUP", t.getMessage()+"");
                t.printStackTrace();
            }
        });
    }
}
