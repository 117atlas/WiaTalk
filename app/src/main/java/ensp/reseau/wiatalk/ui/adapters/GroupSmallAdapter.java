package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.models.Group;
import ensp.reseau.wiatalk.models.User;

/**
 * Created by Sim'S on 14/05/2018.
 */

public class GroupSmallAdapter extends RecyclerView.Adapter<GroupSmallAdapter.GroupSmallViewHolder> {
    private Context context;
    private ArrayList<Group> groups;

    public GroupSmallAdapter(Context context){
        this.context = context;
    }

    public void setGroups(ArrayList<Group> groups){
        this.groups = groups;
        notifyDataSetChanged();
    }

    public void add(Group group){
        if (groups==null) groups = new ArrayList<>();
        groups.add(group);
        notifyDataSetChanged();
    }

    @Override
    public GroupSmallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new GroupSmallViewHolder(inflater.inflate(R.layout.group_small_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupSmallViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return groups==null?0:groups.size();
    }

    class GroupSmallViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView pp;
        private TextView initiales;
        private EmojiTextView groupName;
        private EmojiTextView members;

        private int currentPosition;
        public GroupSmallViewHolder(View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp);
            initiales = itemView.findViewById(R.id.group_initiales);
            groupName = itemView.findViewById(R.id.group_name);
            members = itemView.findViewById(R.id.group_members);
        }

        public void bind(int position){
            currentPosition = position;
            Group group = groups.get(position);
            if (group.getPp()==null) {
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(group.getNom()));
            }
            else {
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //Set pp
                U.showImageAsset(context, group.getPp(), pp);
            }
            groupName.setText(group.getNom());
            members.setText("Utilisateur 1, Utilisateur 2, Utilisateur 3, Utilisateur 4");
        }
    }
}
