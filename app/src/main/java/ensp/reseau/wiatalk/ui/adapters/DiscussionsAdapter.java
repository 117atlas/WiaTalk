package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.models.Discussion;
import ensp.reseau.wiatalk.ui.fragment.DiscussionOptionsFragment;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class DiscussionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DiscussionOptionsFragment.IDiscussionOptions{

    private ArrayList<Discussion> list;
    private Context context;

    private static final int TYPE_ITEMVIEW = 1;
    private static final int TYPE_NUMBERITEMS = 0;

    public DiscussionsAdapter(Context context){
        this.context = context;
    }

    public void setList(ArrayList<Discussion> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(Discussion discussion){
        if (list==null) list = new ArrayList<>();
        list.add(discussion);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType==TYPE_ITEMVIEW) return new DiscussionViewHolder(inflater.inflate(R.layout.discussion_listitem_view, parent, false));
        else return new NumberOfItemsViewHolder(inflater.inflate(R.layout.numberitem_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NumberOfItemsViewHolder) ((NumberOfItemsViewHolder)holder).bind(position, context);
        else ((DiscussionViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return getListSize()+1;
    }

    private int getListSize(){
        return list==null?0:list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position==getListSize()?TYPE_NUMBERITEMS:TYPE_ITEMVIEW;
    }

    //Discussion Options BottomSheet
    @Override
    public void done(int choice, int position) {
        switch (choice){
            case DiscussionOptionsFragment.CHOICE_PIN :
                Toast.makeText(context, "PIN " + position, Toast.LENGTH_SHORT).show(); break;
            case DiscussionOptionsFragment.CHOICE_CLEAR :
                Toast.makeText(context, "CLEAR " + position, Toast.LENGTH_SHORT).show(); break;
            case DiscussionOptionsFragment.CHOICE_DELETE :
                Toast.makeText(context, "DELETE " + position, Toast.LENGTH_SHORT).show(); break;
        }
    }

    class DiscussionViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView pp;
        private TextView initiales;
        private TextView discName;
        private ImageView mute;
        private TextView lastMessage;
        private ImageView status;
        private TextView date;
        private TextView unreadMessages;

        private int currentPosition;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp);
            initiales = itemView.findViewById(R.id.discussion_initiales);
            discName = itemView.findViewById(R.id.discussion_name);
            mute = itemView.findViewById(R.id.mute);
            lastMessage = itemView.findViewById(R.id.last_message);
            status = itemView.findViewById(R.id.message_status);
            date = itemView.findViewById(R.id.date);
            unreadMessages = itemView.findViewById(R.id.unread_messages);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "CLICK", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DiscussionOptionsFragment fragment = DiscussionOptionsFragment.newInstance(DiscussionsAdapter.this, currentPosition);
                    fragment.show(((AppCompatActivity)context).getSupportFragmentManager(), DiscussionOptionsFragment.class.getSimpleName());
                    return false;
                }
            });
        }

        public void bind(int position){
            currentPosition = position;
            Discussion discussion = list.get(position);

            if (discussion.getPp()==null){
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(discussion.getType()==Discussion.TYPE_CONTACT?discussion.getContact():discussion.getGroup()));
            }
            else{
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //pp.setImageURI(Uri.fromFile(new File(new URI())));
                U.loadImage(context, pp, discussion.getPp());
            }

            discName.setText(discussion.getType()==Discussion.TYPE_CONTACT?discussion.getContact():discussion.getGroup());

            if (discussion.isMute()) mute.setVisibility(View.VISIBLE);
            else mute.setVisibility(View.INVISIBLE);

            date.setText(U.NormalizeDate(discussion.getLastMessageDate(), context));
            if (discussion.getUnreadMessages()==0) unreadMessages.setVisibility(View.GONE);
            else unreadMessages.setVisibility(View.VISIBLE);
            unreadMessages.setText(String.valueOf(discussion.getUnreadMessages()));

            switch (discussion.getLastMessageStatus()){
                case Discussion.STATUS_NULL : {
                    status.setVisibility(View.GONE);
                    if (discussion.getType()==Discussion.TYPE_GROUP){
                        String sender = "Member:";
                        Spannable spannable = new SpannableString(sender+" "+discussion.getLastMessageString());
                        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        lastMessage.setText(spannable, TextView.BufferType.SPANNABLE);
                    }
                    else{
                        lastMessage.setText(discussion.getLastMessageString());
                    }
                } break;
                case Discussion.STATUS_SENT:{
                    status.setVisibility(View.VISIBLE);
                    status.setImageResource(R.drawable.ic_tick);
                    lastMessage.setText(discussion.getLastMessageString());
                } break;
                case Discussion.STATUS_RECEIVED: {
                    status.setVisibility(View.VISIBLE);
                    status.setImageResource(R.drawable.ic_double_tick);
                    lastMessage.setText(discussion.getLastMessageString());
                } break;
                case Discussion.STATUS_READ: {
                    status.setVisibility(View.VISIBLE);
                    status.setImageResource(R.drawable.ic_double_tick_green);
                    lastMessage.setText(discussion.getLastMessageString());
                }
            }

        }
    }

}
