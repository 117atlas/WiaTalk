package ensp.reseau.wiatalk.ui.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.File;

import ensp.reseau.wiatalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoViewerFragment extends AppCompatDialogFragment {

    private VideoView video;
    private ProgressBar progress;
    private MediaController mediaController;

    private String videoPath;
    private int position = 0;

    public VideoViewerFragment() {
        // Required empty public constructor
    }

    public static VideoViewerFragment newInstance(String videoPath){
        VideoViewerFragment fragment = new VideoViewerFragment();
        Bundle data = new Bundle();
        data.putString("PATH", videoPath);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_video_viewer, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        video = view.findViewById(R.id.video);
        progress = view.findViewById(R.id.progress);
        if (mediaController==null) mediaController = new MediaController(getContext());
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);

        //video.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);

        try{
            video.setVideoPath(getArguments().getString("PATH"));
            System.out.println(getArguments().getString("PATH"));
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
                if (position == 0) {
                    video.start();
                }
                else {
                    video.pause();
                }

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController.setAnchorView(video);
                    }
                });

            }
        });
    }
}
