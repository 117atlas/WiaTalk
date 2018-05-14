package ensp.reseau.wiatalk.ui.activities;

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

import java.util.ArrayList;
import java.util.List;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.UnSwipeableViewPager;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupAddMembersFragment;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupSetInfosFragment;
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

    public void setGroupeName(String groupeName) {
        this.groupeName = groupeName;
    }

    public void setGroupPp(String groupPp) {
        this.groupPp = groupPp;
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
                    String toasts = "Groupe : " + groupeName + " Avec " + selectedUsers.size() + " membres\n";
                    toasts = toasts + "PP: " + ((groupPp==null)?"Aucune":groupPp) + "\n\n";
                    for (User user:selectedUsers){
                        toasts = toasts + user.getMobile() + " - " +user.getContactName() + "\n";
                    }
                    Toast.makeText(CreateGroupActivity.this, toasts, Toast.LENGTH_LONG).show();
                    Log.i("TOASTS", toasts);
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
}
