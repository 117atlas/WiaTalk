package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.localstorage.LocalStorageMessages;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.ui.UiUtils;

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

    protected RelativeLayout fileContainer;
    protected RelativeLayout mediaContainer;
    protected LinearLayout audioContainer;
    protected LinearLayout documentContainer;
    protected ImageView media;
    protected LinearLayout downloadMediaContainer;
    protected ImageButton downloadMedia;
    protected ProgressBar downloadMediaProgress;
    protected TextView mediaSize;
    protected ImageView videoIndicator;
    protected ImageButton downloadAudio;
    protected ProgressBar downloadAudioProgress;
    protected ImageButton playAudio;
    protected TextView audioName;
    protected ProgressBar audioProgress;
    protected TextView audioSize;
    protected TextView audioLength;
    protected TextView documentInitiales;
    protected TextView documentName;
    protected TextView documentSize;
    protected TextView documentType;
    protected RelativeLayout downloadDocumentContainer;
    protected ImageView downloadDocument;
    protected ProgressBar downloadDocumentProgress;

    protected int currentPosition;
    protected Message currentMessage;
    protected Context context;

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

        initializeFileContainer(itemView);
    }

    public void setMessageClickHandler(IMessageClickHandler messageClickHandler) {
        this.messageClickHandler = messageClickHandler;
    }

    public void bind(Group group, Message message, int position, Message previous){
        currentPosition = position;
        currentMessage = message;

        if (message.getReply()!=null){
            replyContainer.setVisibility(View.VISIBLE);
            replyMessageSender.setTextColor(group.getMembersColors().get(group.indexOfMember(message.getReply().getSender())));
            replyLeftBar.setBackgroundColor(group.getMembersColors().get(group.indexOfMember(message.getReply().getSender())));
            replyMessageSender.setText(message.getReply().getSender().getPseudo());
            replyMessageMessage.setText(UiUtils.shortMessageView(message.getReply()));;
        }
        else replyContainer.setVisibility(View.GONE);

        this.message.setText(message.getText());
        time.setText(U.formatTimestamp(message.getMyReceptionTimestamp()));

        bindMessageStatus(message);
        bindFile(message.getFile());
    }

    protected void bindMessageStatus(Message message){
        switch (message.getStatus()){
            case Message.TYPE_PENDING:{
                status.setImageResource(R.drawable.ic_pending);
            } break;
            case Message.TPPE_SENT:{
                status.setImageResource(R.drawable.ic_tick);
            } break;
            case Message.TYPE_RECEIVED:{
                status.setImageResource(R.drawable.ic_double_tick);
            } break;
            case Message.TYPE_READ:{
                status.setImageResource(R.drawable.ic_double_tick_green);
            } break;
        }
    }

    public void select(Context context){
        root.setBackgroundResource(R.drawable.selected_bg);
    }

    public void deselect(){
        root.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }

    private void initializeFileContainer(View itemView){
        fileContainer = itemView.findViewById(R.id.message_file_container);
        mediaContainer = itemView.findViewById(R.id.media_container);
        audioContainer = itemView.findViewById(R.id.audio_container);
        documentContainer = itemView.findViewById(R.id.document_container);

        media = itemView.findViewById(R.id.media);
        downloadMediaContainer = itemView.findViewById(R.id.download_med_container);
        downloadMedia = itemView.findViewById(R.id.download_med);
        downloadMediaProgress = itemView.findViewById(R.id.download_med_progress);
        mediaSize = itemView.findViewById(R.id.media_size);
        videoIndicator = itemView.findViewById(R.id.video_indicator);

        downloadAudio = itemView.findViewById(R.id.download_aud);
        downloadAudioProgress = itemView.findViewById(R.id.download_aud_progress);
        playAudio = itemView.findViewById(R.id.play_aud);
        audioProgress = itemView.findViewById(R.id.audio_progress);
        audioName = itemView.findViewById(R.id.audio_name);
        audioSize = itemView.findViewById(R.id.audio_size);
        audioLength = itemView.findViewById(R.id.audio_length);

        documentInitiales = itemView.findViewById(R.id.document_initiales);
        documentName = itemView.findViewById(R.id.document_name);
        documentSize = itemView.findViewById(R.id.document_size);
        documentType = itemView.findViewById(R.id.document_type);
        downloadDocumentContainer = itemView.findViewById(R.id.download_doc_container);
        downloadDocumentProgress = itemView.findViewById(R.id.download_doc_progress);
        downloadDocument = itemView.findViewById(R.id.download_doc);

        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        documentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initUpDownListener();
    }

    protected void initUpDownListener(){
        View.OnClickListener downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        };
        downloadMedia.setOnClickListener(downloadListener);
        downloadAudio.setOnClickListener(downloadListener);
        downloadDocument.setOnClickListener(downloadListener);
    }

    protected String normalizeSize(double size){
        if (size/1024<0) return String.valueOf(size)+" o";
        else if (size/(1024*1024)<0) {
            String _size = String.valueOf(size/1024);
            return _size.substring(0, _size.indexOf(".")>0?_size.indexOf(".")+2:_size.length()) + " Ko";
        }
        else {
            String _size = String.valueOf(size/(1024*1024));
            return _size.substring(0, _size.indexOf(".")>0?_size.indexOf(".")+2:_size.length()) + " Mo";
        }
    }

    protected String normalizeLength(double length){
        long _length = ((int)length)/1000;
        String min = String.valueOf(((int)_length)/60);
        if (min.length()==1) min = "0"+min;
        String sec = String.valueOf(((int)_length)%60);
        if (sec.length()==1) sec = "0"+sec;
        return min+":"+sec;
    }

    protected void bindFile(MessageFile messageFile){
        if (messageFile==null) fileContainer.setVisibility(View.GONE);
        else {
            fileContainer.setVisibility(View.VISIBLE);
            switch (messageFile.getType()){
                case MessageFile.TYPE_PHOTO:{
                    mediaContainer.setVisibility(View.VISIBLE);
                    audioContainer.setVisibility(View.GONE);
                    documentContainer.setVisibility(View.GONE);
                    videoIndicator.setVisibility(View.GONE);
                    if (messageFile.getLocalPath()==null || messageFile.getLocalPath().isEmpty() || messageFile.getLocalPath().equals("0")) {
                        downloadMediaContainer.setVisibility(View.VISIBLE);
                        //show thumbnail
                    }
                    else {
                        downloadMediaContainer.setVisibility(View.GONE);
                        //show thumbnail from local photo
                        Glide.with((Context)messageClickHandler)
                                .load(Uri.fromFile(new File(messageFile.getLocalPath())))
                                .thumbnail(0.1f)
                                .centerCrop()
                                .into(media);
                    }
                } break;
                case MessageFile.TYPE_VIDEO:{
                    mediaContainer.setVisibility(View.VISIBLE);
                    audioContainer.setVisibility(View.GONE);
                    documentContainer.setVisibility(View.GONE);
                    videoIndicator.setVisibility(View.VISIBLE);
                    if (messageFile.getLocalPath()==null || messageFile.getLocalPath().isEmpty() || messageFile.getLocalPath().equals("0")) {
                        downloadMediaContainer.setVisibility(View.VISIBLE);
                        //show thumbnail
                    }
                    else {
                        downloadMediaContainer.setVisibility(View.GONE);
                        //show thumbnail from local video
                        Glide.with((Context)messageClickHandler)
                                .load(Uri.fromFile(new File(messageFile.getLocalPath())))
                                .thumbnail(0.1f)
                                .centerCrop()
                                .into(media);
                    }
                } break;
                case MessageFile.TYPE_AUDIO:{
                    mediaContainer.setVisibility(View.GONE);
                    audioContainer.setVisibility(View.VISIBLE);
                    documentContainer.setVisibility(View.GONE);
                    audioName.setText(messageFile.getOriginalName());
                    audioSize.setText(normalizeSize(messageFile.getSize()));
                    audioLength.setText(normalizeLength(messageFile.getLength()));
                    if (messageFile.getLocalPath()==null || messageFile.getLocalPath().isEmpty() || messageFile.getLocalPath().equals("0")) {
                        downloadAudio.setVisibility(View.VISIBLE);
                        playAudio.setVisibility(View.GONE);
                        downloadAudioProgress.setVisibility(View.GONE);
                    }
                    else {
                        downloadAudio.setVisibility(View.GONE);
                        playAudio.setVisibility(View.VISIBLE);
                        downloadAudioProgress.setVisibility(View.GONE);
                        //show thumbnail from local video
                    }
                } break;
                case MessageFile.TYPE_DOCUMENT:{
                    mediaContainer.setVisibility(View.GONE);
                    audioContainer.setVisibility(View.GONE);
                    documentContainer.setVisibility(View.VISIBLE);
                    documentName.setText(messageFile.getOriginalName());
                    documentSize.setText(normalizeSize(messageFile.getSize()));
                    if (messageFile.getOriginalName()!=null) documentType.setText(messageFile.getOriginalName().
                            substring(messageFile.getOriginalName().lastIndexOf(".")+1, messageFile.getOriginalName().length()));
                    if (messageFile.getLocalPath()==null || messageFile.getLocalPath().isEmpty() || messageFile.getLocalPath().equals("0")) {
                        downloadDocumentContainer.setVisibility(View.VISIBLE);
                        downloadDocument.setVisibility(View.VISIBLE);
                        downloadDocumentProgress.setVisibility(View.GONE);
                    }
                    else {
                        downloadDocumentContainer.setVisibility(View.GONE);
                        downloadDocument.setVisibility(View.VISIBLE);
                        downloadDocumentProgress.setVisibility(View.GONE);
                    }

                } break;
            }
        }
    }

    public interface IPlayAudio{
        void play(String path);
    }

    private void upload(){
        final MessageFile messageFile = currentMessage.getFile();
        ProgressBar progressBar = null;
        if (messageFile!=null){
            beginDownload();
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
            NetworkUtils.uploadMessageFile(context, messageFile, progressBar, new NetworkUtils.IFileDownload() {
                @Override
                public void onFileDownloaded(boolean error, String path) {
                    downloadFinished(error);
                    if (!error){
                        messageFile.setUrl(path);
                        //Save message file in server
                    }
                    else beginDownload();
                }
            });
        }
    }

    protected void beginDownload(){
        if (currentMessage.getFile()!=null){
            switch (currentMessage.getFile().getType()){
                case MessageFile.TYPE_PHOTO:{
                    downloadMediaContainer.setVisibility(View.VISIBLE);
                    downloadMedia.setVisibility(View.GONE);
                    downloadMediaProgress.setVisibility(View.VISIBLE);
                    downloadMediaProgress.setIndeterminate(true);
                } break;
                case MessageFile.TYPE_VIDEO:{
                    downloadMediaContainer.setVisibility(View.VISIBLE);
                    downloadMedia.setVisibility(View.GONE);
                    downloadMediaProgress.setVisibility(View.VISIBLE);
                    downloadMediaProgress.setIndeterminate(true);
                } break;
                case MessageFile.TYPE_AUDIO:{
                    downloadAudioProgress.setVisibility(View.VISIBLE);
                    downloadAudioProgress.setIndeterminate(true);
                    downloadAudio.setVisibility(View.GONE);
                    playAudio.setVisibility(View.GONE);
                } break;
                case MessageFile.TYPE_DOCUMENT:{
                    downloadDocumentContainer.setVisibility(View.VISIBLE);
                    downloadDocument.setVisibility(View.GONE);
                    downloadDocumentProgress.setVisibility(View.VISIBLE);
                    downloadDocumentProgress.setIndeterminate(true);
                } break;
            }
        }
    }

    protected void downloadFinished(boolean error){
        if (currentMessage.getFile()!=null && !error){
            switch (currentMessage.getFile().getType()){
                case MessageFile.TYPE_PHOTO:{
                    downloadMediaContainer.setVisibility(View.GONE);
                } break;
                case MessageFile.TYPE_VIDEO:{
                    downloadMediaContainer.setVisibility(View.GONE);
                } break;
                case MessageFile.TYPE_AUDIO:{
                    downloadAudioProgress.setVisibility(View.GONE);
                    downloadAudio.setVisibility(View.GONE);
                    playAudio.setVisibility(View.VISIBLE);
                } break;
                case MessageFile.TYPE_DOCUMENT:{
                    downloadDocumentContainer.setVisibility(View.GONE);
                } break;
            }
        }
    }
}
