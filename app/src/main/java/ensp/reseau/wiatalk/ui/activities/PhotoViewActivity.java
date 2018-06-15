package ensp.reseau.wiatalk.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.ui.UiUtils;

public class PhotoViewActivity extends AppCompatActivity {
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        String path = getIntent().getStringExtra(PhotoViewActivity.class.getSimpleName());
        if (path!=null) UiUtils.showImage(this, image, path);
    }
}
