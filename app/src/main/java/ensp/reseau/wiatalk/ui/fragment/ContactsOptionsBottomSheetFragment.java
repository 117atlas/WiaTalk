package ensp.reseau.wiatalk.ui.fragment;


import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ensp.reseau.wiatalk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsOptionsBottomSheetFragment extends Fragment {

    private AppCompatButton message;
    private AppCompatButton vocalCall;
    private AppCompatButton videoCall;

    public static final int OPTION_MESSAGE = 0;
    public static final int OPTION_VOCAL_CALL = 1;
    public static final int OPTION_VIDEO_CALL = 2;

    private int position;
    private IOptionsChoosen iOptionsChoosen;

    public ContactsOptionsBottomSheetFragment() {
        // Required empty public constructor
    }

    public interface IOptionsChoosen{
        void onOptionChoosen(int option);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts_options_bottom_sheet, container, false);
    }

    public static ContactsOptionsBottomSheetFragment newInstance(int position, IOptionsChoosen iOptionsChoosen){
        ContactsOptionsBottomSheetFragment fragment = new ContactsOptionsBottomSheetFragment();
        fragment.position = position;
        fragment.iOptionsChoosen = iOptionsChoosen;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message = view.findViewById(R.id.message);
        vocalCall = view.findViewById(R.id.vocal_call);
        videoCall = view.findViewById(R.id.video_call);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.message: {
                        iOptionsChoosen.onOptionChoosen(OPTION_MESSAGE);
                    } break;
                    case R.id.vocal_call: {
                        iOptionsChoosen.onOptionChoosen(OPTION_VOCAL_CALL);
                    } break;
                    case R.id.video_call: {
                        iOptionsChoosen.onOptionChoosen(OPTION_VIDEO_CALL);
                    } break;
                }
            }
        };
        message.setOnClickListener(listener);
        vocalCall.setOnClickListener(listener);
        videoCall.setOnClickListener(listener);
    }
}
