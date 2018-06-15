package ensp.reseau.wiatalk.app;

import android.app.Service;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.localstorage.LocalStorageMessages;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.MessageInterface;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.activities.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WiaTalkService extends Service {

    private final IBinder binder = new MyBinder();

    public static final String keyVal_arg0="ARG0";
    public static final String keyVal_arg1="ARG1";

    private Timer timer;
    private int test=0;
    double arg0=0;
    String arg1= "";

    private String meId;

    private IUpdates mainActivity;

    public interface IUpdates{
        void onUpdate();
    }

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            test++;
            //Log.d("Wiatalk service", "Timer task doing work " + test + " arg0: " + arg0);
            //getUpdates();
            //sendPendingMessages();
            /*UserInterface
            Call<>*/
            //Toast.makeText(WiaTalkService.this, "Timer task doing work " + test + " arg0: " + arg0, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            arg0=intent.getDoubleExtra(keyVal_arg0, 0.002);
            arg1=intent.getStringExtra(keyVal_arg1);
            timer = new Timer("UpdateTimer");
            timer.schedule(updateTask, 1000L, 3 * 1000L);
            //Log.d("OREN", "ServiceStarted" + test);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("OREN", "OnBind" + test);
        return binder;
    }

    public void setArg0(double d){
        arg0=d;
    }

    public String getMeId() {
        return meId;
    }

    public void setMeId(String meId) {
        this.meId = meId;
    }

    public class MyBinder extends Binder {
        public WiaTalkService getService() {
            return WiaTalkService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.d("WIATalk Service", "OnDestroy" + test);
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("WIATalk Service", "OnUnBind" + test);
        return super.onUnbind(intent);
    }

    private void onUpdate(ArrayList<Group> groups){
        Intent intent = new Intent("Update");
        Bundle bundle = new Bundle();
        bundle.putBoolean("update", true);
        bundle.putSerializable("Groups", groups);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void getUpdates(){
        System.out.println(meId);
        if (meId!=null && !meId.isEmpty()) {
            UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
            Log.d("Wiatalk service", "Timer task doing work " + test + " arg0: " + arg0 + "user " + meId);
            if (true) {
                Call<UserInterface.GetUserResponse> call = userInterface.updates(meId);
                call.enqueue(new Callback<UserInterface.GetUserResponse>() {
                    @Override
                    public void onResponse(Call<UserInterface.GetUserResponse> call, Response<UserInterface.GetUserResponse> response) {
                        if (response.body()==null) {
                            Log.e("GET UPDATES", "Response body is null");
                            return;
                        }
                        if (response.body().isError()){
                            Log.e("GET UPDATES", response.body().getMessage());
                        }
                        else{
                            Log.d("GET UPDATES", "Success");
                            //onUpdate((ArrayList<Group>) response.body().getUser().getGroups());
                            final Response<UserInterface.GetUserResponse> fResponse = response;
                            new AsyncTask<Void, Void, ArrayList<Group>>(){
                                @Override
                                protected ArrayList<Group> doInBackground(Void... voids) {
                                    return saveUpdates(true, (ArrayList<Group>)fResponse.body().getUser().getGroups());
                                }
                                @Override
                                protected void onPostExecute(ArrayList<Group> groups) {
                                    super.onPostExecute(groups);
                                    onUpdate(groups);
                                }
                            }.execute();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInterface.GetUserResponse> call, Throwable t) {
                        Log.e("GET UPDATE FAILURE", t.getMessage()+"");
                        //t.printStackTrace();
                    }
                });
            }
        }
    }

    public ArrayList<Group> saveUpdates(boolean update, ArrayList<Group> groups){
        if (update && groups!=null){
            for (Group group: groups){
                LocalStorageDiscussions.storeGroup(group, getApplicationContext());
                LocalStorageDiscussions.storeGroupMessages(group, getApplicationContext());
            }

            //Mark new Messages As read
            ArrayList<Message> newMessages = LocalStorageDiscussions.getAllNewMessages(getApplicationContext());
            NetworkUtils.markAs(Message.TYPE_RECEIVED, newMessages, getApplicationContext());

            //Get updated groups
            ArrayList<Group> updatedGroups = LocalStorageDiscussions.getAllDiscussions(getApplicationContext());
            for (Group group: updatedGroups) if (group.getType()==Group.TYPE_IB) LocalStorageDiscussions.populateGroup(group, getApplicationContext());
            return updatedGroups;
        }
        return null;
    }

    private void sendPendingMessages(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Message> pendingMessages = LocalStorageMessages.getPendingMessages(getApplicationContext());
                if (pendingMessages!=null && pendingMessages.size()>0) for (Message message: pendingMessages) sendPendingMessage(message);
                return null;
            }
        }.execute();
    }
    private void sendPendingMessage(final Message message){
        MessageInterface messageInterface = NetworkAPI.getClient().create(MessageInterface.class);
        Call<MessageInterface.GetMessageResponse> sendMessage = messageInterface.sendMessage(message);
        sendMessage.enqueue(new Callback<MessageInterface.GetMessageResponse>() {
            @Override
            public void onResponse(Call<MessageInterface.GetMessageResponse> call, Response<MessageInterface.GetMessageResponse> response) {
                if (response.body()==null){
                    //Log.e("Send Message", "Response body null");
                    return;
                }
                if (response.body().isError()){
                    //Log.e("Send Message", "Response error " + response.body().getMessage());
                    return;
                }
                //Log.d("Send Message", "Send message successfull");

                final Message nMessage = response.body().get_message();
                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        nMessage.setSignalisationMessage(false);
                        nMessage.setMyReceptionTimestamp(message.getMyReceptionTimestamp());
                        nMessage.arrangeForLocalStorage();

                        LocalStorageMessages.deleteMessageByMyTimestamp(message.getMyReceptionTimestamp(), getApplicationContext());
                        LocalStorageMessages.storeMessages(nMessage, getApplicationContext());

                        return null;
                    }
                }.execute();

            }
            @Override
            public void onFailure(Call<MessageInterface.GetMessageResponse> call, Throwable t) {
                Log.e("SEND PENDING MESSAGES", t.getMessage()+"");
                //t.printStackTrace();
            }
        });
    }
}
