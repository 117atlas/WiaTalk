package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.activities.EditMobileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditMobileFragment extends Fragment {

    private AppCompatEditText oldMobile;
    private AppCompatEditText newMobile;
    private AppCompatButton next;

    public EditMobileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_mobile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldMobile = view.findViewById(R.id.old_mobile);
        newMobile = view.findViewById(R.id.new_mobile);
        next = view.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldMobile.getText().toString().isEmpty()){
                    oldMobile.setError(getString(R.string.empty_old_mobile));
                    return;
                }
                Pattern pattern = Pattern.compile("^6[2356789][0-9]{7}$");
                Matcher matcher = pattern.matcher(oldMobile.getText().toString().replace("-", ""));
                if (!matcher.matches()){
                    oldMobile.setError(getString(R.string.wrong_number));
                    return;
                }
                if (newMobile.getText().toString().isEmpty()){
                    newMobile.setError(getString(R.string.empty_new_mobile));
                    return;
                }
                matcher = pattern.matcher(newMobile.getText().toString().replace("-", ""));
                if (!matcher.matches()){
                    newMobile.setError(getString(R.string.wrong_number));
                    return;
                }

                User user = ((EditMobileActivity)getActivity()).getUser();
                if (!oldMobile.getText().toString().equals(user.getMobile())){
                    oldMobile.setError(getString(R.string.incorrect_mobile_num));
                    return;
                }

                Toast.makeText(getContext(), "EDIT PHONE", Toast.LENGTH_SHORT).show();

                //NEXT
                next();
            }
        });
    }

    private void next(){
        ((EditMobileActivity)getActivity()).swipe(1, null);
    }
}
