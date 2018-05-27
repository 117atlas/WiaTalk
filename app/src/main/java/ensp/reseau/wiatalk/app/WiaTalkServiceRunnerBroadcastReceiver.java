package ensp.reseau.wiatalk.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class WiaTalkServiceRunnerBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_SET_UpdateService = "ACTION_ALARM";

    public static final String keyVal_arg0="ARG0";
    public static final String keyVal_arg1="ARG1";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SET_UpdateService)){
            launchUpdateService(context, intent.getDoubleExtra(keyVal_arg0, 0.02), intent.getStringExtra(keyVal_arg1));
        }
    }

    private void launchUpdateService(final Context context, double arg0, String arg1){
        final Intent intent = new Intent(context, WiaTalkService.class);
        intent.setAction(ACTION_SET_UpdateService);
        intent.putExtra(keyVal_arg0, arg0);
        intent.putExtra(keyVal_arg1, arg1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startService(intent);
                Log.d("WIATALK Service","ServiceRunner");
            }
        }, 6000);
        /*synchronized (this){
            try {
                this.wait(6000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
    }
}
