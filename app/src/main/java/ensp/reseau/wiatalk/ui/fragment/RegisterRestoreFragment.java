package ensp.reseau.wiatalk.ui.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.ui.SimulateProcessing;
import ensp.reseau.wiatalk.ui.activities.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterRestoreFragment extends Fragment {

    private AppCompatButton yes;
    private AppCompatButton no;
    private ProgressBar restoreProgress;
    private TextView restoreResponse;
    private AppCompatButton restartRestore;
    private AppCompatButton next;
    private LinearLayout restoreQuestionContainer;
    private LinearLayout restoreResponseContainer;

    public RegisterRestoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_restore, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidgets(view);
    }

    private void initializeWidgets(View view){
        yes = view.findViewById(R.id.yes);
        no = view.findViewById(R.id.no);
        restoreProgress = view.findViewById(R.id.restore_progress);
        restoreResponse = view.findViewById(R.id.restore_response);
        restartRestore = view.findViewById(R.id.restart);
        next = view.findViewById(R.id.next);
        restoreQuestionContainer = view.findViewById(R.id.answer_container);
        restoreResponseContainer = view.findViewById(R.id.response_container);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NEXT
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreDatas();
            }
        });

        restartRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreDatas();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    private void restoreDatas(){
        new SimulRestore().execute();
    }

    class SimulRestore extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int time = 5000;
            int _time = 0;
            while (_time<=time){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                _time += 50;
                onProgressUpdate((int)(_time*100)/(time));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            restoreQuestionContainer.setVisibility(View.GONE);
            restoreProgress.setVisibility(View.VISIBLE);
            restoreResponseContainer.setVisibility(View.GONE);

            restoreProgress.setMax(100);
            restoreProgress.setProgress(0);
            restoreProgress.setSecondaryProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            restoreQuestionContainer.setVisibility(View.GONE);
            restoreProgress.setVisibility(View.GONE);
            restoreResponseContainer.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            restoreProgress.setProgress(values[0]);
            restoreProgress.setSecondaryProgress(values[0]+13>100?100:values[0]+13);
        }
    }

    public void next(){
        ((RegisterActivity)getActivity()).swipe(4);
    }

    private void simulateProcessing(){
        new SimulateProcessing(getContext(), this).execute();
    }
}
