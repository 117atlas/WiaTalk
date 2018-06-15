package ensp.reseau.wiatalk.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageInterface {

    @POST("messages/sendmessagefile")
    Call<GetMessageFileResponse> sendMessageFile(@Body MessageFile messageFile);

    @POST("messages/updatemessagefile")
    Call<GetMessageFileResponse> updateMessageFile(@Body MessageFile messageFile);

    @POST("messages/send")
    Call<GetMessageResponse> sendMessage(@Body Message message);

    @GET("message/updatemessage/vn/{message}/{vn}")
    Call<GetMessageResponse> updateMessageVn(@Path("message") String messageId, @Path("vn") String vn);

    @POST("message/markas/{status}/{user_id}")
    Call<BaseResponse> markas(@Body MarkAsBody markAsBody, @Path("status") int status, @Path("user_id") String userId);


    class GetMessageFileResponse extends BaseResponse{
        @SerializedName("message_file") @Expose private MessageFile messageFile;

        public MessageFile getMessageFile() {
            return messageFile;
        }

        public void setMessageFile(MessageFile messageFile) {
            this.messageFile = messageFile;
        }
    }

    class GetMessageResponse extends BaseResponse{
        @SerializedName("_message") @Expose private Message _message;

        public Message get_message() {
            return _message;
        }

        public void set_message(Message message) {
            _message = message;
        }
    }

    class MarkAsBody{
        @SerializedName("messages") @Expose
        List<String> messages;

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }
    }
}
