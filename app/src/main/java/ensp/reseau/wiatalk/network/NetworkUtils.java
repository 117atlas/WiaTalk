package ensp.reseau.wiatalk.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Future;

import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.files.MessageFilesUtils;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtils {

    public interface IFileDownload{
        void onFileDownloaded(boolean error, String path);
    }

    public static void downloadPp(Context context, String id, String ppurl, final IFileDownload iFileDownload){
        Log.d("PPURL", ppurl==null?"null":ppurl);
        if (ppurl != null && !ppurl.equals("0") && !ppurl.isEmpty()){
            final File ppFile = FilesUtils.getOthersPpFile(id, context);
            final File tempDir = new File(context.getFilesDir().getAbsolutePath()+File.separator+"temp");
            if (!tempDir.exists()) tempDir.mkdirs();
            final File tempFile = new File(tempDir.getAbsolutePath()+File.separator+ Calendar.getInstance().getTimeInMillis());
            Ion.with(context)
                    .load(NetworkAPI.SERVER_URL+ppurl.replace("\\", "/"))
                    .write(tempFile)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            // download done...
                            // do stuff with the File or error
                            if (e!=null) {
                                e.printStackTrace();
                                return;
                            }
                            try {
                                copy(file, ppFile);
                                //File ppfileSave = new File(ppfile.getAbsolutePath());
                                iFileDownload.onFileDownloaded(false, ppFile.getPath());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }
        iFileDownload.onFileDownloaded(true,null);
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static void downloadMessageFile(Context context, MessageFile messageFile, final ProgressBar progressBar, final IFileDownload iFileDownload){
        if (messageFile!=null){
            final File mfFile = MessageFilesUtils.getMessageFile(messageFile);
            Ion.with(context)
                    .load(NetworkAPI.SERVER_URL+messageFile.getUrl().replace("\\", "/"))
                    .progressBar(progressBar)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long l, long l1) {
                            progressBar.setMax(100);
                            progressBar.setProgress((int)(((double)l/(double)l1)*100));
                        }
                    })
                    .write(mfFile)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            if (e!=null) {
                                e.printStackTrace();
                                return;
                            }
                            iFileDownload.onFileDownloaded(false, mfFile.getAbsolutePath());
                        }
                    });
        }
        else iFileDownload.onFileDownloaded(true, null);
    }

    public static void uploadMessageFile(final Context context, MessageFile messageFile, final ProgressBar progressBar, final IFileDownload iFileDownload){
        File f = new File(messageFile.getLocalPath());
        String[] types = {"image", "video", "audio", "document"};
        Future uploading = Ion.with(context)
                .load(NetworkAPI.BASE_URL+"messages/uploadfile/"+messageFile.get_id()+"/"+messageFile.getType())
                .progressBar(progressBar)
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long l, long l1) {
                        progressBar.setMax(100);
                        int progress = (int)Math.round((((double)l)/((double)l1))*100);
                        progressBar.setProgress(progress);
                    }
                })
                .setMultipartFile(types[messageFile.getType()-1], f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                        try {
                            JSONObject jobj = new JSONObject(result.getResult());
                            boolean error = jobj.getBoolean("error");
                            String message = jobj.getString("message");
                            String path = jobj.getString("path");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            iFileDownload.onFileDownloaded(error, path);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public static void markAs(int status, ArrayList<Message> messages, Context context){
        if (messages!=null && messages.size()>0){
            MessageInterface messageInterface = NetworkAPI.getClient().create(MessageInterface.class);

            MessageInterface.MarkAsBody markAsBody = new MessageInterface.MarkAsBody();
            markAsBody.setMessages(new ArrayList<String>());
            for (Message message: messages) markAsBody.getMessages().add(message.get_id());

            Call<BaseResponse> markAs =  messageInterface.markas(markAsBody, status, WiaTalkApp.getMe(context).get_Id());
            markAs.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body()==null) {
                        //Log.e("Mark As", "Empty response body");
                        return;
                    }
                    if (response.body().isError()){
                        //Log.e("Mark As", "Response error " + response.body().getMessage());
                        return;
                    }
                    //Log.i("Mark As", "Succesful");
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    //t.printStackTrace();
                }
            });
        }
    }
}
