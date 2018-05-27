package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.ContactsActivity;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.fragment.AdminsOptionsBottomSheetFragment;
import ensp.reseau.wiatalk.ui.fragment.ContactsOptionsBottomSheetFragment;

/**
 * Created by Sim'S on 12/05/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private int type;

    private IPhoneContactsCharged iPhoneContactsCharged;

    private ArrayList<User> selectedUsers = new ArrayList<>();

    private int purpose;

    public static final int TYPE_LIST_CONTACTS_USERS_FOR_ADD_IN_GROUP = 0;
    public static final int TYPE_LIST_CONTACTS_USERS = 1;
    public static final int TYPE_LIST_CONTACTS_USERS_IN_GROUP = 2;

    public ContactsAdapter(Context context, int type, IPhoneContactsCharged iPhoneContactsCharged, int purpose){this.context = context; this.type = type; this.iPhoneContactsCharged = iPhoneContactsCharged; this.purpose = purpose;}

    public ContactsAdapter(Context context, int type, IPhoneContactsCharged iPhoneContactsCharged){this.context = context; this.type = type; this.iPhoneContactsCharged = iPhoneContactsCharged; this.purpose = 0;}

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(type==TYPE_LIST_CONTACTS_USERS_IN_GROUP?R.layout.contact_small_itemview:R.layout.contact_itemview,
                parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return users==null?0:users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView pp;
        protected EmojiTextView username;
        protected TextView initiales;
        protected TextView mobile;
        protected CheckBox select;

        private int currentPosition;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp);
            username = itemView.findViewById(R.id.username);
            initiales = itemView.findViewById(R.id.contact_initiales);
            mobile = itemView.findViewById(R.id.usermobile);
            select = itemView.findViewById(R.id.select);

            if (type==TYPE_LIST_CONTACTS_USERS_FOR_ADD_IN_GROUP){
                select.setChecked(false); select.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select.setChecked(!select.isChecked());
                        selectUser();
                    }
                });
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectUser();
                    }
                });
            }
            else{
                if (select!=null) select.setVisibility(View.GONE);
            }

            if (type==TYPE_LIST_CONTACTS_USERS_IN_GROUP){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Si c'est un admin, on ouvre le bottomsheet de l'admin
                        if (true){
                            AdminsOptionsBottomSheetFragment adminsOptionsBottomSheetFragment =
                                    AdminsOptionsBottomSheetFragment.newInstance(currentPosition, users.get(currentPosition), new AdminsOptionsBottomSheetFragment.IAdminOptions() {
                                        @Override
                                        public void onOptionChoosen(int option) {
                                            switch (option){
                                                case AdminsOptionsBottomSheetFragment.OPTION_MESSAGE:{
                                                    UiUtils.switchActivity((AppCompatActivity)context, DiscussionActivity.class, true, null);
                                                } break;
                                            }
                                        }
                                    });
                            adminsOptionsBottomSheetFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), AdminsOptionsBottomSheetFragment.class.getSimpleName());
                        }
                        else{

                        }
                    }
                });
            }

            if (type==TYPE_LIST_CONTACTS_USERS){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (purpose== ContactsActivity.NO_PURPOSE){
                            //
                            //Ouvrir le BottomSheetFragment des options
                            ContactsOptionsBottomSheetFragment contactsOptionsBottomSheetFragment =
                                    ContactsOptionsBottomSheetFragment.newInstance(currentPosition, ((ContactsOptionsBottomSheetFragment.IOptionsChoosen)context), users.get(currentPosition));
                            contactsOptionsBottomSheetFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),
                                    ContactsOptionsBottomSheetFragment.class.getSimpleName());
                        }

                        else if (purpose == ContactsActivity.PURPOSE_MESSAGE) {
                            //La liste de contacts a ete ouverte a partir du floating action button du fragment des discussions
                            UiUtils.switchActivity(((AppCompatActivity)context), DiscussionActivity.class, true, null);
                        }

                        else if (purpose == ContactsActivity.PURPOSE_VOCAL_CALL) {
                            //La liste de contacts a ete ouverte a partir du floating action button APPEL VOCAL du fragment de l'historique des appels
                        }

                        else if (purpose == ContactsActivity.PURPOSE_VIDEO_CALL) {
                            //La liste de contacts a ete ouverte a partir du floating action button APPEL VIDEO du fragment de l'historique des appels
                        }
                    }
                });
            }
        }

        public void bind(int position){
            currentPosition = position;
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
            mobile.setText(user.getMobile());
            String usernameText = user.getContactName()==null?"~"+user.getPseudo():user.getContactName()+" ~"+user.getPseudo();
            Spannable spannable = new SpannableString(usernameText);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), usernameText.lastIndexOf("~"), usernameText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            username.setText(spannable);
        }

        private void selectUser(){
            User user = users.get(currentPosition);
            if (select.isChecked() && !selectedUsers.contains(user)) selectedUsers.add(user);
            if (!select.isChecked() && selectedUsers.contains(user)) selectedUsers.remove(user);
            if (iPhoneContactsCharged!=null) iPhoneContactsCharged.onCharged(selectedUsers);
        }

    }
}
