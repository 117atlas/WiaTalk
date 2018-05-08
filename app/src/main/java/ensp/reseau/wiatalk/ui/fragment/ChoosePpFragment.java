package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoosePpFragment extends DialogFragment {

    private IChoosePp iChoosePp;

    private TextView gallery;
    private TextView camera;

    public static final int CHOICE_GALLERY = 1;
    public static final int CHOICE_CAMERA = 2;

    public interface IChoosePp {
        public void choiceForPp(int choice);
    }

    public ChoosePpFragment() {
        // Required empty public constructor
    }

    public static ChoosePpFragment newInstance(IChoosePp iChoosePp){
        ChoosePpFragment choosePpFragment = new ChoosePpFragment();
        choosePpFragment.iChoosePp = iChoosePp;
        return choosePpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_choose_pp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gallery = view.findViewById(R.id.gallery_choice);
        camera = view.findViewById(R.id.camera_choice);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                iChoosePp.choiceForPp(CHOICE_GALLERY);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                iChoosePp.choiceForPp(CHOICE_CAMERA);
            }
        });
    }
}
