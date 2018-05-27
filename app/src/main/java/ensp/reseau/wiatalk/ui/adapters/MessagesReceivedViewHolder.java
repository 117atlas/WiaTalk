package ensp.reseau.wiatalk.ui.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.tmodels.Message;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesReceivedViewHolder extends MessagesSentViewHolder {
    protected TextView sender;
    public MessagesReceivedViewHolder(View itemView) {
        super(itemView);
        sender = itemView.findViewById(R.id.sender);
    }

    @Override
    public void bind(Message message, int position, Message previous) {
        super.bind(message, position, previous);
        if (previous!=null && previous.getSender()!=null && previous.getSender().equals(message.getSender())) sender.setVisibility(View.GONE);
        else sender.setVisibility(View.VISIBLE);
        sender.setText(message.getSender());
        sender.setTextColor(Color.argb(255, (int)Math.round(Math.random()), (int)Math.round(Math.random()), (int)Math.round(Math.random())));
    }
}
