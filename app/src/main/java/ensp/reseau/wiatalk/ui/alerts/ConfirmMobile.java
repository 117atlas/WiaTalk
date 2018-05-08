package ensp.reseau.wiatalk.ui.alerts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import ensp.reseau.wiatalk.R;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class ConfirmMobile extends AppCompatDialogFragment{
    private IConfirmMobile iConfirmMobile;
    private String message;
    public static String TAG = ConfirmMobile.class.getSimpleName();

    public interface IConfirmMobile{
        void confirmMobile(boolean confirm);
    }

    public ConfirmMobile() {
    }

    public static ConfirmMobile newInstance(IConfirmMobile iConfirmMobile, String message){
        ConfirmMobile confirmMobile = new ConfirmMobile();
        confirmMobile.iConfirmMobile = iConfirmMobile;
        confirmMobile.message = message;
        return confirmMobile;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        iConfirmMobile.confirmMobile(true);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        iConfirmMobile.confirmMobile(false);
                    }
                })
                .create();
        dialog.setCancelable(false);
        return dialog;
    }
}
