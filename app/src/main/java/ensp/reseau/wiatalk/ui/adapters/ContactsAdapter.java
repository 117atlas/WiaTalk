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
import android.widget.ImageView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.ContactsActivity;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.activities.GroupInfosActivity;
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

    private ArrayList<User> alreadyIn = new ArrayList<>();

    private ArrayList<User> admins;
    private GroupInfosActivity groupInfosActivity;



    public GroupInfosActivity getGroupInfosActivity() {
        return groupInfosActivity;
    }

    public void setGroupInfosActivity(GroupInfosActivity groupInfosActivity) {
        this.groupInfosActivity = groupInfosActivity;
    }

    public ArrayList<User> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<User> admins) {
        this.admins = admins;
    }

    public void addAlreadyIn(User user){
        if (alreadyIn==null) alreadyIn = new ArrayList<>();
        alreadyIn.add(user);
    }

    public ArrayList<User> getAlreadyIn() {
        return alreadyIn;
    }

    public void setAlreadyIn(ArrayList<User> alreadyIn) {
        this.alreadyIn = alreadyIn;
    }

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
        protected ImageView admin;

        private int currentPosition;

        private View root;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            pp = itemView.findViewById(R.id.pp);
            username = itemView.findViewById(R.id.username);
            initiales = itemView.findViewById(R.id.contact_initiales);
            mobile = itemView.findViewById(R.id.usermobile);
            select = itemView.findViewById(R.id.select);
            admin = itemView.findViewById(R.id.isadmin);

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
                                    AdminsOptionsBottomSheetFragment.newInstance(currentPosition, users.get(currentPosition), isAdmin(), amIAdmin(), new AdminsOptionsBottomSheetFragment.IAdminOptions() {
                                        @Override
                                        public void onOptionChoosen(int option) {
                                            /*switch (option){
                                                case AdminsOptionsBottomSheetFragment.OPTION_MESSAGE:{
                                                    //UiUtils.switchActivity((AppCompatActivity)context, DiscussionActivity.class, true, null);
                                                } break;
                                                case AdminsOptionsBottomSheetFragment.OPTION_NOMINATE_ADMIN:{
                                                    groupInfosActivity.groupMembersOptionChoosen();
                                                } break;
                                                case AdminsOptionsBottomSheetFragment.OPTION_REMOVE_MEMBER:{

                                                } break;
                                                case AdminsOptionsBottomSheetFragment.OPTION_VIEW_PROFILE:{

                                                } break;
                                            }*/
                                            groupInfosActivity.groupMembersOptionChoosen(users.get(currentPosition), option);
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
            final User user = users.get(position);
            if (user.getPp()==null || user.getPp().isEmpty() || user.getPp().equals("0")) {
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(user.getContactName()==null?user.getPseudo():user.getContactName()));
            }
            else {
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //Set pp
                if (user.getPp_change_timestamp()>user.getOld_pp_change_timestamp() || user.getPpPath()==null || user.getPpPath().equals("0") || user.getPpPath().isEmpty()){
                    NetworkUtils.downloadPp(context, user.get_Id(), user.getPp(), new NetworkUtils.IFileDownload() {
                        @Override
                        public void onFileDownloaded(boolean error, String path) {
                            if (!error) {
                                user.setOld_pp_change_timestamp(user.getPp_change_timestamp());
                                user.setPpPath(path);
                                LocalStorageUser.updateUser(user, context);
                                UiUtils.showImage(context, pp, user.getPpPath());
                            }
                            else if (user.getPpPath()==null || user.getPpPath().equals("0") || user.getPpPath().isEmpty())
                                UiUtils.showImage(context, pp, user.getPp(), true);
                            else UiUtils.showImage(context, pp, user.getPpPath());
                        }
                    });
                }
                else UiUtils.showImage(context, pp, user.getPpPath());
            }
            mobile.setText(user.getMobile());
            String usernameText = user.getContactName()==null?"~"+user.getPseudo():user.getContactName()+" ~"+user.getPseudo();
            Spannable spannable = new SpannableString(usernameText);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), usernameText.lastIndexOf("~"), usernameText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            username.setText(spannable);

            if (type==TYPE_LIST_CONTACTS_USERS_FOR_ADD_IN_GROUP){
                if (alreadyIn!=null && alreadyIn.contains(user)){
                    root.setClickable(false);
                    root.setEnabled(false);
                    select.setChecked(true);
                }
                else{
                    root.setClickable(true);
                    root.setEnabled(true);
                    select.setChecked(selectedUsers!=null && selectedUsers.contains(user));
                }
            }

            if (admin!=null) admin.setVisibility(isAdmin()?View.VISIBLE:View.INVISIBLE);

        }

        private void selectUser(){
            User user = users.get(currentPosition);
            if (select.isChecked() && !selectedUsers.contains(user)) selectedUsers.add(user);
            if (!select.isChecked() && selectedUsers.contains(user)) selectedUsers.remove(user);
            if (iPhoneContactsCharged!=null) iPhoneContactsCharged.onCharged(selectedUsers);
        }

        private boolean isAdmin(){
            return admins!=null && admins.contains(users.get(currentPosition));
        }

        private boolean amIAdmin(){
            User me = WiaTalkApp.getMe(context);
            if (me==null) return false;
            return admins!=null && admins.contains(me);
        }

    }
}
