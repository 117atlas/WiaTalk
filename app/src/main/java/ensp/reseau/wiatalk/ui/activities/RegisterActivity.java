package ensp.reseau.wiatalk.ui.activities;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.UnSwipeableViewPager;
import ensp.reseau.wiatalk.ui.fragment.RegisterMobileFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterOtpFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterRestoreFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterUserFragment;

public class RegisterActivity extends AppCompatActivity {

    private UnSwipeableViewPager viewPager;

    private RegisterMobileFragment mobileFragment;
    private RegisterOtpFragment otpFragment;
    private RegisterUserFragment userFragment;
    private RegisterRestoreFragment restoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager();
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mobileFragment = new RegisterMobileFragment();
        otpFragment = new RegisterOtpFragment();
        userFragment = new RegisterUserFragment();
        restoreFragment = new RegisterRestoreFragment();
        viewPagerAdapter.addFragment(mobileFragment, "STEP 1");
        viewPagerAdapter.addFragment(otpFragment, "STEP 2");
        viewPagerAdapter.addFragment(userFragment, "STEP 3");
        viewPagerAdapter.addFragment(restoreFragment, "STEP 4");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
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
        if (dest<4) viewPager.setCurrentItem(dest);
        else UiUtils.switchActivity(this, MainActivity.class, true, null);
    }
}
