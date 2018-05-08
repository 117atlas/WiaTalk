package ensp.reseau.wiatalk.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.ui.fragment.RegisterMobileFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterOtpFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterRestoreFragment;
import ensp.reseau.wiatalk.ui.fragment.RegisterUserFragment;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class SimulateProcessing extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progressDialog;
    private Context context;
    private Object parent;

    interface After{
        void simulateDone();
    }

    private After after;

    public SimulateProcessing(Context context, Object parent) {
        this.context = context;
        this.parent = parent;
    }

    public SimulateProcessing(Context context, After after) {
        this.context = context;
        this.after = after;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.processing));
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
        if (parent instanceof RegisterMobileFragment){
            ((RegisterMobileFragment)parent).next();
        }
        if (parent instanceof RegisterOtpFragment){
            ((RegisterOtpFragment)parent).next();
        }
        if (parent instanceof RegisterUserFragment){
            ((RegisterUserFragment)parent).next();
        }
        if (parent instanceof RegisterRestoreFragment){
            ((RegisterRestoreFragment)parent).next();
        }
        if (after!=null) after.simulateDone();
    }
}
