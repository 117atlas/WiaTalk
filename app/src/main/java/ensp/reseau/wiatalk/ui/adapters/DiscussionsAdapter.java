package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.fragment.DiscussionOptionsFragment;
import ensp.reseau.wiatalk.ui.fragment.ViewPhotoFragment;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class DiscussionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DiscussionOptionsFragment.IDiscussionOptions{

    private ArrayList<Group> list;
    private Context context;

    private static final int TYPE_ITEMVIEW = 1;
    private static final int TYPE_NUMBERITEMS = 0;

    private User me;

    public DiscussionsAdapter(Context context){
        this.context = context;
        me = WiaTalkApp.getMe(context);
    }

    public void setList(ArrayList<Group> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(Group group){
        if (list==null) list = new ArrayList<>();
        list.add(group);
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

    private User getIbUser(Group group){
        return group.getMembers().get(0).getMember().equals(me)?group.getMembers().get(1).getMember():group.getMembers().get(0).getMember();
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
                    Intent intent = new Intent(context, DiscussionActivity.class);
                    intent.putExtra(Group.class.getSimpleName(), list.get(currentPosition));
                    context.startActivity(intent);
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

            pp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Group group = list.get(currentPosition);
                    ViewPhotoFragment viewPhotoFragment = ViewPhotoFragment.newInstance(group, group.getType()==Group.TYPE_IB?getIbUser(group):null);
                    viewPhotoFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), ViewPhotoFragment.class.getSimpleName());
                }
            });
        }

        private Spannable setLastMessageString(Message lastMessage, boolean isFromMe){
            if (lastMessage.isSignalisationMessage()){
                String text = lastMessage.getText();
                if (text.contains("|++GC++|")){
                    lastMessage.setText(context.getString(R.string.group_creation_message).replace("?????", lastMessage.getGroup().getName()).replace("????", lastMessage.getSender().getPseudo()));
                }
                else if (text.contains("ADD_MEMBER")){
                    String addedMember = LocalStorageUser.getUserById(U.Split(text, '|').get(1), context).getPseudo();
                    lastMessage.setText(context.getString(R.string.add_member_message).replace("?????", lastMessage.getSender().getPseudo()).replace("????", addedMember));
                }
                else if (text.contains("REMOVE_MEMBER")){
                    String addedMember = LocalStorageUser.getUserById(U.Split(text, '|').get(1), context).getPseudo();
                    lastMessage.setText(context.getString(R.string.remove_member_message).replace("?????", lastMessage.getSender().getPseudo()).replace("????", addedMember));
                }
                else if (text.contains("ADD_ADMIN")){
                    lastMessage.setText(context.getString(R.string.add_admin_message).replace("?????", lastMessage.getSender().getPseudo()));
                }
                else if (text.contains("CHANGENAME")){
                    ArrayList<String> names = U.Split(U.Split(text, '|').get(1), '+');
                    lastMessage.setText(context.getString(R.string.change_group_name_message).replace("??????", lastMessage.getSender().getPseudo()).replace("?????", names.get(0)).replace("????", names.get(1)));
                }
                else if (text.contains("CHANGEPICTURE")){
                    lastMessage.setText(context.getString(R.string.change_pp_message).replace("?????", lastMessage.getSender().getPseudo()));
                }
                return new SpannableString(lastMessage.getText());
            }
            else{
                String sender = "", file = "";
                String[] types = {"Photo", "Video", "Audio", "Document"};
                if (!isFromMe) sender = "~"+(lastMessage.getSender()!=null?lastMessage.getSender().getPseudo():"Sender")+": ";
                if (lastMessage.getFile()!=null) file = "["+types[lastMessage.getFile().getType()-1]+"] ";
                Spannable spannable = new SpannableString(sender+file+lastMessage.getText());
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), spannable.toString().indexOf(sender), spannable.toString().indexOf(sender)+sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), spannable.toString().indexOf(file), spannable.toString().indexOf(file)+file.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannable;
            }
        }

        public void bind(int position){
            currentPosition = position;
            Group group = list.get(position);

            if (group.getPp()==null){
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(group.getType()==Group.TYPE_IB?getIbUser(group).getPseudo():group.getName()));
            }
            else{
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //pp.setImageURI(Uri.fromFile(new File(new URI())));
                UiUtils.showImage(context, pp, group.getType()==Group.TYPE_IB?getIbUser(group).getPpPath():group.getPpPath());
            }

            discName.setText(group.getType()==Group.TYPE_IB?getIbUser(group).getPseudo():group.getName());

            //if (discussion.isMute()) mute.setVisibility(View.VISIBLE);
            //else mute.setVisibility(View.INVISIBLE);

            date.setText(U.NormalizeDate(group.getLastMessage().getMyReceptionTimestamp(), context));
            if (group.getNewMessages()==0) unreadMessages.setVisibility(View.GONE);
            else unreadMessages.setVisibility(View.VISIBLE);
            unreadMessages.setText(String.valueOf(group.getNewMessages()));

            Message groupLastMessage = group.getLastMessage();
            if (groupLastMessage.getSender()!=null && groupLastMessage.getSender().equals(me)){
                switch (group.getLastMessage().getStatus()){
                    case Message.TYPE_PENDING:{
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_tick);
                        lastMessage.setText(setLastMessageString(groupLastMessage, true));
                    } break;
                    case Message.TPPE_SENT:{
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_tick);
                        lastMessage.setText(setLastMessageString(groupLastMessage, true));
                    } break;
                    case Message.TYPE_RECEIVED: {
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_double_tick);
                        lastMessage.setText(setLastMessageString(groupLastMessage, true));
                    } break;
                    case Message.TYPE_READ: {
                        status.setVisibility(View.VISIBLE);
                        status.setImageResource(R.drawable.ic_double_tick_green);
                        lastMessage.setText(setLastMessageString(groupLastMessage, true));
                    }
                }
            }
            else{
                status.setVisibility(View.GONE);
                lastMessage.setText(setLastMessageString(groupLastMessage, false));
            }

        }
    }

}
