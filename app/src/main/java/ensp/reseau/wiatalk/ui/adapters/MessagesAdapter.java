package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Message;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Message> messages;

    private ArrayList<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();

    public MessagesAdapter(Context context){
        this.context = context;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message){
        if (messages==null) messages = new ArrayList<>();
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder;
        if (viewType==Message.TYPE_RECEIVED) holder = new MessagesReceivedViewHolder(inflater.inflate(R.layout.message_received_itemview, parent, false));
        else if (viewType==Message.TYPE_SENT) holder = new MessagesSentViewHolder(inflater.inflate(R.layout.message_sent_itemview, parent, false));
        else holder = new MessagesSignalViewHolder(inflater.inflate(R.layout.signal_message_itemview, parent, false));

        if (holder instanceof MessagesReceivedViewHolder) ((MessagesReceivedViewHolder)holder).setMessageClickHandler((IMessageClickHandler)context);
        else if (holder instanceof MessagesSentViewHolder) ((MessagesSentViewHolder)holder).setMessageClickHandler((IMessageClickHandler)context);

        if (holder instanceof  MessagesReceivedViewHolder || holder instanceof MessagesSentViewHolder) viewHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        Message previous = position==0?null:messages.get(position-1);
        if (holder instanceof MessagesReceivedViewHolder) ((MessagesReceivedViewHolder)holder).bind(message, position, previous);
        else if (holder instanceof MessagesSentViewHolder) ((MessagesSentViewHolder)holder).bind(message, position, previous);
        else ((MessagesSignalViewHolder)holder).bind(message);
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return messages==null?0:messages.size();
    }

    public void selectItems(ArrayList<Integer> selectedItems){
        for (RecyclerView.ViewHolder holder : viewHolders){
            if (holder instanceof MessagesSentViewHolder || holder instanceof MessagesReceivedViewHolder){
                if ( selectedItems.contains(((MessagesSentViewHolder)holder).getCurrentPosition()) ) ((MessagesSentViewHolder)holder).select(context);
                else ((MessagesSentViewHolder)holder).deselect();
            }
        }
    }
}
