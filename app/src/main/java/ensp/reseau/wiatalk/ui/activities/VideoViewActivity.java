package ensp.reseau.wiatalk.ui.activities;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import ensp.reseau.wiatalk.R;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView video;
    private ProgressBar progress;
    private MediaController mediaController;

    private String videoPath;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_viewer);

        video = findViewById(R.id.video);
        progress = findViewById(R.id.progress);
        if (mediaController==null) mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);

        //video.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);

        try{
            video.setVideoPath(getIntent().getStringExtra("PATH"));
            System.out.println(getIntent().getStringExtra("PATH"));
        } catch (Exception e){
            e.printStackTrace();
        }
        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //video.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);

                video.seekTo(position);
                video.start();
                /*if (position == 0) {
                    video.start();
                }
                else {
                    video.pause();
                }*/

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController.setAnchorView(video);
                    }
                });

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Store current position.
        savedInstanceState.putInt("CurrentPosition", video.getCurrentPosition());
        video.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        video.seekTo(position);
    }
}
