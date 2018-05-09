package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Message;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class MessagesSentViewHolder extends RecyclerView.ViewHolder {

    protected View root;

    protected ConstraintLayout replyContainer;
    protected TextView replyMessageSender;
    protected TextView replyMessageMessage;
    protected FrameLayout replyLeftBar;
    protected TextView message;
    protected TextView time;
    protected ImageView status;

    protected int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    protected IMessageClickHandler messageClickHandler;

    public MessagesSentViewHolder(View itemView) {
        super(itemView);
        root = itemView;
        replyContainer = itemView.findViewById(R.id.reply_container);
        replyMessageSender = itemView.findViewById(R.id.reply_message_sender);
        replyMessageMessage = itemView.findViewById(R.id.reply_message_message);
        replyLeftBar = itemView.findViewById(R.id.reply_color);
        message = itemView.findViewById(R.id.message);
        time = itemView.findViewById(R.id.time);
        status = itemView.findViewById(R.id.message_status);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                messageClickHandler.longClick(currentPosition);
                return true;
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageClickHandler.click(currentPosition);
            }
        });
    }

    public void setMessageClickHandler(IMessageClickHandler messageClickHandler) {
        this.messageClickHandler = messageClickHandler;
    }

    public void bind(Message message, int position, Message previous){
        currentPosition = position;
        int color = Color.argb(255, (int)Math.round(Math.random()*256), (int)Math.round(Math.random()*256), (int)Math.round(Math.random()*256));
        replyMessageSender.setText(message.getSender());
        replyMessageSender.setTextColor(color);
        replyLeftBar.setBackgroundColor(color);
        if (message.isReply()){
            replyContainer.setVisibility(View.VISIBLE);
        }
        else replyContainer.setVisibility(View.GONE);
    }

    public void select(Context context){
        root.setBackgroundResource(R.drawable.selected_bg);
    }

    public void deselect(){
        root.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }
}
