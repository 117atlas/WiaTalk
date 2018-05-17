package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.ui.SimulateProcessing;
import ensp.reseau.wiatalk.ui.activities.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterOtpFragment extends Fragment {

    private AppCompatEditText otp;
    private AppCompatButton previous;
    private AppCompatButton next;
    private AppCompatButton requestSms;

    private long time = 65;
    private Thread timeThread = null;
    private boolean interrupt;

    public RegisterOtpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    @Override
    public void onStop() {
        interrupt = true;
        super.onStop();
    }

    @Override
    public void onStart() {
        interrupt = false;
        initTimeThread();
        super.onStart();
    }

    public void initializeView(View view){
        otp = view.findViewById(R.id.otp);
        previous = view.findViewById(R.id.back);
        next = view.findViewById(R.id.next);
        requestSms = view.findViewById(R.id.request_sms);

        requestSms.setText(normalizeTime());
        requestSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimeThread();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().isEmpty()){
                    otp.setError(getString(R.string.empty_code_otp));
                    return;
                }
                //VERIFYOTP ET NEXT
                simulateProcessing();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BACK
                ((RegisterActivity)getActivity()).swipe(0);
            }
        });

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean add = (i2==1 && i1==0);
                if (i==2 && add) {
                    otp.setText(otp.getText().toString()+"-");
                    otp.setSelection(otp.getText().length());
                }
                if (i==3 && add){
                    otp.setText(otp.getText().toString().substring(0, otp.getText().length()-1)+"-"+otp.getText().toString().substring(otp.getText().length()-1));
                    otp.setSelection(otp.getText().length());
                }
                if (i==4 && !add){
                    otp.setText(otp.getText().toString().substring(0, otp.getText().length()-1));
                    otp.setSelection(otp.getText().length());
                }
                if (i==3 && !add){
                    otp.setText(otp.getText().toString()+"-");
                    otp.setText(otp.getText().toString().substring(0, otp.getText().length()-2));
                    otp.setSelection(otp.getText().length());
                }
                if (i>=7) {
                    otp.getText().replace(i, i+i2, "");
                    //otp.setText(code.getText().toString().substring(0, 7));
                }
                //if (charSequence.length()>=7) next.setEnabled(true);
                //else next.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private String normalizeTime(){
        String res = getActivity().getString(R.string.wait_sms)+" ";
        long min = time/60;
        long sec = time%60;
        if (min!=0) res = res + min + " min ";
        res = res + sec + " secondes";
        return res;
    }

    private void initTimeThread(){
        requestSms.setClickable(false);
        timeThread = new Thread(new Runnable() {
            private long oldTime = time;
            @Override
            public void run() {
                while (!interrupt && time>=1){
                    if (getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    requestSms.setText(normalizeTime());
                                } catch (Exception e){e.printStackTrace();}
                            }
                        });
                        time--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else return;
                }
                time = oldTime + 30;
                if (getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestSms.setClickable(true);
                            requestSms.setText(getString(R.string.request_sms));
                        }
                    });
                }
                else return;
            }
        });
        timeThread.start();
    }

    public void simulateProcessing(){
        new SimulateProcessing(getContext(), this).execute();
    }

    public void next(){
        ((RegisterActivity)getActivity()).swipe(2);
    }
}
