package ensp.reseau.wiatalk.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.SimulateProcessing;
import ensp.reseau.wiatalk.ui.activities.RegisterActivity;
import ensp.reseau.wiatalk.ui.alerts.ConfirmMobile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterMobileFragment extends Fragment implements ConfirmMobile.IConfirmMobile{

    private AppCompatEditText mobile;
    private AppCompatButton next;

    public RegisterMobileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_mobile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mobile = view.findViewById(R.id.mobile);
        next = view.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.getText().toString().isEmpty()){
                    mobile.setError(getString(R.string.empty_mobile));
                    return;
                }
                Pattern pattern = Pattern.compile("^6[2356789][0-9]{7}$");
                Matcher matcher = pattern.matcher(mobile.getText().toString().replace("-", ""));
                if (!matcher.matches()){
                    mobile.setError(getString(R.string.invalide_mobile));
                    return;
                }

                ConfirmMobile confirmMobile = ConfirmMobile.newInstance(RegisterMobileFragment.this,
                        getString(R.string.confirm_mobile).replace("???", mobile.getText().toString().replace("-", "")));
                confirmMobile.show(getFragmentManager(), ConfirmMobile.TAG);
            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean add = (i2==1 && i1==0);
                if (i==2 && add) {
                    mobile.setText(mobile.getText().toString()+"-");
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==3 && add){
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-1)+"-"+mobile.getText().toString().substring(mobile.getText().length()-1));
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==4 && !add){
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-1));
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==3 && !add){
                    mobile.setText(mobile.getText().toString()+"-");
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-2));
                    mobile.setSelection(mobile.getText().length());
                }

                if (i==6 && add) {
                    mobile.setText(mobile.getText().toString()+"-");
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==7 && add){
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-1)+"-"+mobile.getText().toString().substring(mobile.getText().length()-1));
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==8 && !add){
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-1));
                    mobile.setSelection(mobile.getText().length());
                }
                if (i==7 && !add){
                    mobile.setText(mobile.getText().toString()+"-");
                    mobile.setText(mobile.getText().toString().substring(0, mobile.getText().length()-2));
                    mobile.setSelection(mobile.getText().length());
                }

                if (i>=11) {
                    mobile.getText().replace(i, i+i2, "");
                    //mobile.setText(code.getText().toString().substring(0, 7));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void confirmMobile(boolean confirm) {
        if (confirm) /*simulateProcessing();*/ registerMobile();
    }

    public void next(){
        ((RegisterActivity)getActivity()).swipe(1);
    }

    private void simulateProcessing(){
        new SimulateProcessing(getContext(), this).execute();
    }

    private void registerMobile(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.GetUserResponse> registerMobileCall = userInterface.registerMobile(mobile.getText().toString().replace("-", ""));
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        registerMobileCall.enqueue(new Callback<UserInterface.GetUserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.GetUserResponse> call, Response<UserInterface.GetUserResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()!=null){
                    Log.d(RegisterMobileFragment.class.getSimpleName(), response.body().getMessage() + " --- " + response.body().isError());
                    if (response.body().isError()){
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ((RegisterActivity)getActivity()).setMobile(mobile.getText().toString().replace("-", ""));
                        ((RegisterActivity)getActivity()).setFinalUser(response.body().getUser());
                        next();
                    }
                }
                else Log.d(RegisterMobileFragment.class.getSimpleName(), "Response is empty");
            }

            @Override
            public void onFailure(Call<UserInterface.GetUserResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.d(RegisterMobileFragment.class.getSimpleName(), t.getMessage()+"");
            }
        });
    }
}
