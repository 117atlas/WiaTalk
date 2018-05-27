package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.Group;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.ContactInfosActivity;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.activities.GroupInfosActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPhotoFragment extends DialogFragment {

    private ImageView pp;
    private ImageButton viewProfile;
    private ImageButton message;
    private ImageButton vocalCall;
    private ImageButton videoCall;
    private TextView discussionName;

    private Group group;
    private User user;

    public ViewPhotoFragment() {
        // Required empty public constructor
    }

    public static ViewPhotoFragment newInstance(Group group, User user){
        ViewPhotoFragment viewPhotoFragment = new ViewPhotoFragment();
        viewPhotoFragment.group = group;
        viewPhotoFragment.user = user;
        return viewPhotoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_view_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pp = view.findViewById(R.id.pp);
        viewProfile = view.findViewById(R.id.view_profile);
        message = view.findViewById(R.id.message);
        vocalCall = view.findViewById(R.id.vocal_call);
        videoCall = view.findViewById(R.id.video_call);
        discussionName = view.findViewById(R.id.discussion_name);

        discussionName.setText(user==null?group.getNom():(user.getContactName()==null?user.getMobile():user.getContactName()));
        U.loadImage(getContext(), pp, user==null?group.getPp():user.getPp());

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                UiUtils.switchActivity(getActivity(), user==null?GroupInfosActivity.class: ContactInfosActivity.class, false, null);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                UiUtils.switchActivity(getActivity(), DiscussionActivity.class, false, null);
            }
        });
        vocalCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(getContext(), "Vocal Call", Toast.LENGTH_SHORT).show();
            }
        });
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(getContext(), "Video Call", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
