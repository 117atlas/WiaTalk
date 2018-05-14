package ensp.reseau.wiatalk.ui.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.UnSwipeableViewPager;
import ensp.reseau.wiatalk.ui.adapters.ViewPagerAdapter;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupAddMembersFragment;
import ensp.reseau.wiatalk.ui.fragment.CreateGroupSetInfosFragment;
import ensp.reseau.wiatalk.ui.fragment.EditMobileFragment;
import ensp.reseau.wiatalk.ui.fragment.EditMobileOtpFragment;

public class EditMobileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private UnSwipeableViewPager viewPager;

    private EditMobileFragment editMobileFragment;
    private EditMobileOtpFragment editMobileOtpFragment;

    private User user;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mobile);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_mobile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setupViewPager();
    }

    public void swipe(int dest, String... datas){
        if (dest==0) viewPager.setCurrentItem(0);
        if (dest==1) {
            //Change mobile and send otp
            viewPager.setCurrentItem(1);
        }
        if (dest==2) {
            Toast.makeText(this, "NEXT ACTIVITY, EDIT MOBILE DONE", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        editMobileFragment = new EditMobileFragment();
        editMobileOtpFragment = new EditMobileOtpFragment();
        viewPagerAdapter.addFragment(editMobileFragment, "STEP 1");
        viewPagerAdapter.addFragment(editMobileOtpFragment, "STEP 2");
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
}