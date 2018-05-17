package ensp.reseau.wiatalk.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminsOptionsBottomSheetFragment extends BottomSheetDialogFragment {

    public static final int OPTION_MESSAGE = 1;
    public static final int OPTION_VIEW_PROFILE = 2;
    public static final int OPTION_NOMINATE_ADMIN = 3;
    public static final int OPTION_REMOVE_MEMBER = 4;

    private AppCompatButton message;
    private AppCompatButton viewProfile;
    private AppCompatButton nominateAdmin;
    private AppCompatButton removeMember;

    private int position;
    private User user;
    private IAdminOptions iAdminOptions;

    public interface IAdminOptions{
        public void onOptionChoosen(int option);
    }

    public AdminsOptionsBottomSheetFragment() {
        // Required empty public constructor
    }

    public static AdminsOptionsBottomSheetFragment newInstance(int position, User user, IAdminOptions iAdminOptions){
        AdminsOptionsBottomSheetFragment fragment = new AdminsOptionsBottomSheetFragment();
        fragment.position = position;
        fragment.user = user;
        fragment.iAdminOptions = iAdminOptions;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admins_options_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message = view.findViewById(R.id.message);
        viewProfile = view.findViewById(R.id.view_profile);
        nominateAdmin = view.findViewById(R.id.nominate_admin);
        removeMember = view.findViewById(R.id.remove_member);


        String textUser = user.getContactName()==null?user.getMobile():user.getContactName();

        String viewProfileText = getString(R.string.view_profile);
        int indiceRepViewProfile = viewProfileText.lastIndexOf("????");
        Spannable spannable = new SpannableString(viewProfileText.replace("????", textUser));
        spannable.setSpan(new ForegroundColorSpan(Color.GRAY), indiceRepViewProfile, indiceRepViewProfile+textUser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewProfile.setText(spannable);

        String nominateAdminText = getString(R.string.nominate_admin);
        int indiceRepNominateAdmin = nominateAdminText.lastIndexOf("????");
        Spannable spannable1 = new SpannableString(nominateAdminText.replace("????", textUser));
        spannable1.setSpan(new ForegroundColorSpan(Color.GRAY), indiceRepNominateAdmin, indiceRepNominateAdmin+textUser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nominateAdmin.setText(spannable1);

        String removeMemberText = getString(R.string.remove_member);
        int indiceRepRemoveMember = removeMemberText.lastIndexOf("????");
        Spannable spannable2 = new SpannableString(removeMemberText.replace("????", textUser));
        spannable2.setSpan(new ForegroundColorSpan(Color.GRAY), indiceRepRemoveMember, indiceRepRemoveMember+textUser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        removeMember.setText(spannable2);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.message:{
                        iAdminOptions.onOptionChoosen(OPTION_MESSAGE);
                    } break;
                    case R.id.view_profile:{
                        iAdminOptions.onOptionChoosen(OPTION_VIEW_PROFILE);
                    } break;
                    case R.id.nominate_admin:{
                        iAdminOptions.onOptionChoosen(OPTION_NOMINATE_ADMIN);
                    } break;
                    case R.id.remove_member:{
                        iAdminOptions.onOptionChoosen(OPTION_REMOVE_MEMBER);
                    } break;
                }
            }
        };
        message.setOnClickListener(listener);
        viewProfile.setOnClickListener(listener);
        nominateAdmin.setOnClickListener(listener);
        removeMember.setOnClickListener(listener);
    }
}
