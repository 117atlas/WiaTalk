package ensp.reseau.wiatalk.ui.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.ui.UiUtils;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        User me = WiaTalkApp.getMe(SplashActivity.this);
        if (me!=null) {
            UiUtils.switchActivity(SplashActivity.this, InitAppActivity.class, true);
            finish();
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                UiUtils.switchActivity(SplashActivity.this, RegisterActivity.class, true);
                }
            }, SPLASH_DELAY);
        }

    }
}
