package ensp.reseau.wiatalk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Message;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesSignalViewHolder extends RecyclerView.ViewHolder {
    private TextView message;
    public MessagesSignalViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    public void bind(Message message){

    }
}
