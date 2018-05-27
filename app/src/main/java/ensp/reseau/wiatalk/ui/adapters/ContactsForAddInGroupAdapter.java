package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.User;

/**
 * Created by Sim'S on 12/05/2018.
 */

public class ContactsForAddInGroupAdapter extends RecyclerView.Adapter<ContactsForAddInGroupAdapter.ContactsForAddInGroupViewHolder> {
    private ArrayList<User> users;
    private Context context;

    public ContactsForAddInGroupAdapter(Context context){
        this.context = context;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ContactsForAddInGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_added_ingroup_itemview, parent, false);
        return new ContactsForAddInGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactsForAddInGroupViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return users==null?0:users.size();
    }

    class ContactsForAddInGroupViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView pp;
        private TextView initiales;
        private EmojiTextView username;
        public ContactsForAddInGroupViewHolder(View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp);
            username = itemView.findViewById(R.id.username);
            initiales = itemView.findViewById(R.id.contact_initiales);
        }

        public void bind(int position){
            User user = users.get(position);
            if (user.getPp()==null) {
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(user.getContactName()==null?user.getPseudo():user.getContactName()));
            }
            else {
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //Set pp
                U.loadImage(context, pp, user.getPp());
            }
            username.setText(users.get(position).getContactName());
            //Set pp
            //U.loadImage(context, pp, users.get(position).getPp());
        }
    }
}
