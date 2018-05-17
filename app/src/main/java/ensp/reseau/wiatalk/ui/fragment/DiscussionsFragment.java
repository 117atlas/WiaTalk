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
import ensp.reseau.wiatalk.models.Discussion;
import ensp.reseau.wiatalk.ui.IntentExtra;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.ContactsActivity;
import ensp.reseau.wiatalk.ui.adapters.DiscussionsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussionsFragment extends Fragment {

    private NestedScrollView scroll;
    private RecyclerView messageList;
    private RelativeLayout noMessageContainer;
    private FloatingActionButton newMessage;
    
    public DiscussionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discussions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scroll = view.findViewById(R.id.scroll);
        messageList = view.findViewById(R.id.messages_list);
        noMessageContainer = view.findViewById(R.id.no_message_container);
        newMessage = view.findViewById(R.id.newmessage);
        
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "NEW MESSAGE", Toast.LENGTH_SHORT).show();
                UiUtils.switchActivity(getActivity(), ContactsActivity.class, false, new IntentExtra("PURPOSE", ContactsActivity.PURPOSE_MESSAGE));
            }
        });
        thereAreMessages();
        mlistMessages();
    }

    private void noMessages(){
        scroll.setVisibility(View.INVISIBLE);
        noMessageContainer.setVisibility(View.VISIBLE);
    }

    private void thereAreMessages(){
        scroll.setVisibility(View.VISIBLE);
        noMessageContainer.setVisibility(View.INVISIBLE);
    }

    private void mlistMessages(){
        DiscussionsAdapter adapter = new DiscussionsAdapter(getContext());
        messageList.setLayoutManager(new LinearLayoutManager(getContext()));
        messageList.setAdapter(adapter);
        adapter.setList(Discussion.random(13));
    }
}
