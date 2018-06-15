package ensp.reseau.wiatalk.ui.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.localstorage.LocalStorageMessages;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.network.NetworkUtils;

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
    public void bind(Group group, Message message, int position, Message previous) {
        super.bind(group, message, position, previous);
        if (previous!=null && previous.getSender()!=null && previous.getSender().equals(message.getSender())) sender.setVisibility(View.GONE);
        else sender.setVisibility(View.VISIBLE);
        //sender.setText(message.getSender());
        sender.setTextColor(group.getMembersColors().get(group.indexOfMember(message.getSender())));
    }

    @Override
    protected void bindMessageStatus(Message message) {

    }

    private void download(){
        final MessageFile messageFile = currentMessage.getFile();
        if (messageFile!=null){
            beginDownload();
            ProgressBar progressBar = null;
            switch (messageFile.getType()){
                case MessageFile.TYPE_PHOTO:{
                    progressBar = downloadMediaProgress;
                } break;
                case MessageFile.TYPE_VIDEO:{
                    progressBar = downloadMediaProgress;
                } break;
                case MessageFile.TYPE_AUDIO:{
                    progressBar = downloadAudioProgress;
                } break;
                case MessageFile.TYPE_DOCUMENT:{
                    progressBar = downloadDocumentProgress;
                } break;
            }
            NetworkUtils.downloadMessageFile(context, messageFile, progressBar, new NetworkUtils.IFileDownload() {
                @Override
                public void onFileDownloaded(boolean error, String path) {
                    downloadFinished(error);
                    if (!error){
                        messageFile.setLocalPath(path);
                        LocalStorageMessages.storeMessageFile(messageFile, context);
                    }
                    else{
                        beginDownload();
                    }
                }
            });
        }
    }

    @Override
    protected void initUpDownListener(){
        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        };
        downloadMedia.setOnClickListener(downloadListener);
        downloadAudio.setOnClickListener(downloadListener);
        downloadDocument.setOnClickListener(downloadListener);
    }
}
