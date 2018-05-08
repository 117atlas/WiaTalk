package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussionOptionsFragment extends BottomSheetDialogFragment {

    private LinearLayout pin;
    private LinearLayout clear;
    private LinearLayout delete;
    private int position;

    public static final int CHOICE_PIN = 1;
    public static final int CHOICE_CLEAR = 2;
    public static final int CHOICE_DELETE = 3;

    private IDiscussionOptions iDiscussionOptions;

    public interface  IDiscussionOptions{
        public void done(int choice, int position);
    }

    public DiscussionOptionsFragment() {
        // Required empty public constructor
    }

    public  static DiscussionOptionsFragment newInstance(IDiscussionOptions iDiscussionOptions, int position){
        DiscussionOptionsFragment fragment = new DiscussionOptionsFragment();
        fragment.iDiscussionOptions = iDiscussionOptions;
        fragment.position = position;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussion_options_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pin = view.findViewById(R.id.pin);
        clear = view.findViewById(R.id.clear);
        delete = view.findViewById(R.id.delete);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.pin: iDiscussionOptions.done(CHOICE_PIN, position); break;
                    case R.id.clear: iDiscussionOptions.done(CHOICE_CLEAR, position); break;
                    case R.id.delete: iDiscussionOptions.done(CHOICE_DELETE, position); break;
                }
                dismiss();
            }
        };
        pin.setOnClickListener(listener);
        clear.setOnClickListener(listener);
        delete.setOnClickListener(listener);
    }
}
