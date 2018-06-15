package ensp.reseau.wiatalk.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.Group;

public class UpdateReceiver extends BroadcastReceiver {

    public interface OnUpdateReceived{
        void onUpdateReceived(ArrayList<Group> groups);
    }

    private OnUpdateReceived onUpdateReceived;

    public UpdateReceiver(OnUpdateReceived onUpdateReceived) {
        this.onUpdateReceived = onUpdateReceived;
    }

    public UpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        boolean update = b.getBoolean("update");
        ArrayList<Group> groups = (ArrayList<Group>) b.getSerializable("Groups");
        if (update && groups!=null){
            //messagesFragment.update(groups);
            if (onUpdateReceived!=null) onUpdateReceived.onUpdateReceived(groups);
        }

    }
}
