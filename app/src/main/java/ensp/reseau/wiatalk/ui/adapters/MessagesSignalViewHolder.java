package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.Message;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesSignalViewHolder extends RecyclerView.ViewHolder {
    private TextView message;
    private Context context;
    
    public MessagesSignalViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void bind(Message message){
        this.message.setText(setSignalMessageString(message));
    }

    private Spannable setSignalMessageString(Message message){
        if (message.isSignalisationMessage()){
            String text = message.getText();
            if (text.contains("|++GC++|")){
                message.setText(context.getString(R.string.group_creation_message).replace("?????", message.getGroup().getName()).replace("????", message.getSender().getPseudo()));
            }
            else if (text.contains("ADD_MEMBER")){
                String addedMember = LocalStorageUser.getUserById(U.Split(text, '|').get(1), context).getPseudo();
                message.setText(context.getString(R.string.add_member_message).replace("?????", message.getSender().getPseudo()).replace("????", addedMember));
            }
            else if (text.contains("REMOVE_MEMBER")){
                String addedMember = LocalStorageUser.getUserById(U.Split(text, '|').get(1), context).getPseudo();
                message.setText(context.getString(R.string.remove_member_message).replace("?????", message.getSender().getPseudo()).replace("????", addedMember));
            }
            else if (text.contains("ADD_ADMIN")){
                message.setText(context.getString(R.string.add_admin_message).replace("?????", message.getSender().getPseudo()));
            }
            else if (text.contains("CHANGENAME")){
                ArrayList<String> names = U.Split(U.Split(text, '|').get(1), '+');
                message.setText(context.getString(R.string.change_group_name_message).replace("??????", message.getSender().getPseudo()).replace("?????", names.get(0)).replace("????", names.size()==1?"":names.get(1)));
            }
            else if (text.contains("CHANGEPICTURE")){
                message.setText(context.getString(R.string.change_pp_message).replace("?????", message.getSender().getPseudo()));
            }
            return new SpannableString(message.getText());
        }
        return new SpannableString("");
    }
}
