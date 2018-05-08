package ensp.reseau.wiatalk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Call;
import ensp.reseau.wiatalk.models.Discussion;
import ensp.reseau.wiatalk.ui.adapters.CallsAdapter;
import ensp.reseau.wiatalk.ui.adapters.DiscussionsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {

    private NestedScrollView scroll;
    private RecyclerView callsList;
    private RelativeLayout noCallsContainer;
    private FloatingActionButton newCall;

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calls, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scroll = view.findViewById(R.id.scroll);
        callsList = view.findViewById(R.id.calls_list);
        noCallsContainer = view.findViewById(R.id.no_calls_container);
        newCall = view.findViewById(R.id.newcall);

        newCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "NEW CALL", Toast.LENGTH_SHORT).show();
            }
        });
        thereAreCalls();
        mlistCalls();
    }

    private void noCalls(){
        scroll.setVisibility(View.INVISIBLE);
        noCallsContainer.setVisibility(View.VISIBLE);
    }

    private void thereAreCalls(){
        scroll.setVisibility(View.VISIBLE);
        noCallsContainer.setVisibility(View.INVISIBLE);
    }

    private void mlistCalls(){
        CallsAdapter adapter = new CallsAdapter(getContext());
        callsList.setLayoutManager(new LinearLayoutManager(getContext()));
        callsList.setAdapter(adapter);
        adapter.setList(Call.random(5));
    }

}
