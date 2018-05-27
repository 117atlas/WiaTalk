package ensp.reseau.wiatalk.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ensp.reseau.wiatalk.network.UserInterface;
import retrofit2.Call;

public class WiaTalkService extends Service {

    private final IBinder binder = new MyBinder();
    public static final String keyVal_arg0="ARG0";
    public static final String keyVal_arg1="ARG1";
    private Timer timer;
    private int test=0;
    double arg0=0;
    String arg1= "";

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            test++;
            Log.d("Wiatalk service", "Timer task doing work " + test + " arg0: " + arg0);
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
            Log.d("OREN", "ServiceStarted" + test);
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
}
