package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ensp.reseau.wiatalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayAudioFragment extends Fragment {


    public PlayAudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_audio, container, false);
    }

}
