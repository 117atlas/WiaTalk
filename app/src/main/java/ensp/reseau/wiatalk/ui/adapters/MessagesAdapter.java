package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.User;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int MESSAGE_SENT = 298;
    private static final int MESSAGE_RECEIVED = 299;
    private static final int SIGNAL_MESSAGE = -54;
    private Context context;
    private ArrayList<Message> messages;

    private ArrayList<Message> selectedMessages;

    private Group group;

    private ArrayList<RecyclerView.ViewHolder> holders;


    public ArrayList<RecyclerView.ViewHolder> getHolders() {
        return holders;
    }

    public void setHolders(ArrayList<RecyclerView.ViewHolder> holders) {
        this.holders = holders;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ArrayList<Message> getSelectedMessages() {
        return selectedMessages;
    }

    public void setSelectedMessages(ArrayList<Message> selectedMessages) {
        this.selectedMessages = selectedMessages;
    }

    private User me;

    private ArrayList<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();

    public MessagesAdapter(Context context){
        this.context = context;
        me = WiaTalkApp.getMe(context);
        selectedMessages = new ArrayList<>();
        holders = new ArrayList<>();
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
        if (viewType==MESSAGE_RECEIVED) holder = new MessagesReceivedViewHolder(inflater.inflate(R.layout.message_received_itemview, parent, false));
        else if (viewType==MESSAGE_SENT) holder = new MessagesSentViewHolder(inflater.inflate(R.layout.message_sent_itemview, parent, false));
        else holder = new MessagesSignalViewHolder(inflater.inflate(R.layout.signal_message_itemview, parent, false));

        if (holder instanceof MessagesReceivedViewHolder) ((MessagesReceivedViewHolder)holder).setMessageClickHandler((IMessageClickHandler)context);
        else if (holder instanceof MessagesSentViewHolder) ((MessagesSentViewHolder)holder).setMessageClickHandler((IMessageClickHandler)context);
        else ((MessagesSignalViewHolder)holder).setContext(context);

        if (holder instanceof  MessagesReceivedViewHolder || holder instanceof MessagesSentViewHolder) viewHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        Message previous = position==0?null:messages.get(position-1);
        if (holder instanceof MessagesReceivedViewHolder) ((MessagesReceivedViewHolder)holder).bind(group, message, position, previous);
        else if (holder instanceof MessagesSentViewHolder) ((MessagesSentViewHolder)holder).bind(group, message, position, previous);
        else ((MessagesSignalViewHolder)holder).bind(message);

        if (holders!=null && !holders.contains(holder)) holders.add(holder);
        //else ((MessagesSignalViewHolder)holder).bind(message);
    }

    @Override
    public int getItemViewType(int position) {
        if (me==null) return MESSAGE_RECEIVED;
        if (messages.get(position).isSignalisationMessage()) return SIGNAL_MESSAGE;
        return (messages.get(position).getSenderId()!=null && messages.get(position).getSenderId().equals(me.get_Id()))?MESSAGE_SENT:MESSAGE_RECEIVED;
    }

    @Override
    public int getItemCount() {
        return messages==null?0:messages.size();
    }

    public void selectItems(ArrayList<Integer> selectedItems){
        for (RecyclerView.ViewHolder holder : viewHolders){
            if (holder instanceof MessagesSentViewHolder || holder instanceof MessagesReceivedViewHolder){
                if ( selectedItems.contains(((MessagesSentViewHolder)holder).getCurrentPosition()) ) {
                    ((MessagesSentViewHolder)holder).select(context);
                    selectedMessages.add(messages.get(((MessagesSentViewHolder)holder).getCurrentPosition()));
                }
                else {
                    ((MessagesSentViewHolder)holder).deselect();
                    selectedMessages.remove(new Integer(((MessagesSentViewHolder)holder).getCurrentPosition()));
                }
            }
        }
    }
}
