package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.UsersGroups;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.activities.GroupInfosActivity;

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
            final Group group = groups.get(position);
            if (group.getPp()==null) {
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(group.getName()));
            }
            else {
                File file = new File("");
                boolean fileexists = false;
                if (group.getPpPath()!=null){
                    file = new File(group.getPpPath());
                    fileexists = file.exists();
                }
                if (group.getPpPath()==null || !fileexists) {
                    downloadPp(group);
                }
                else
                    UiUtils.showImage(context, pp, group.getPpPath());

                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
            }
            groupName.setText(group.getName());

            String members = "";
            for (UsersGroups usersGroups: group.getMembers()) members = members + usersGroups.getMember().getPseudo() + ", ";
            members = members.substring(0, members.length()-2);
            this.members.setText(members);
        }

        private void downloadPp(final Group group){
            NetworkUtils.downloadPp(context, group.get_id(), group.getPp(), new NetworkUtils.IFileDownload() {
                @Override
                public void onFileDownloaded(boolean error, String path) {
                    if (!error) {
                        group.setPpPath(path);
                        group.setOld_pp_change_timestamp(group.getPp_change_timestamp());
                        LocalStorageDiscussions.storeGroup(group, context);
                        UiUtils.showImage(context, pp, path);
                        Log.d("DL PP GRP", "Group Small Adapter - Complete");
                    }
                    else{
                        UiUtils.showImage(context, pp, group.getPp(), true);
                        Log.e("DL PP GRP", "Group Small Adapter - Error");
                    }
                }
            });
        }
    }
}
