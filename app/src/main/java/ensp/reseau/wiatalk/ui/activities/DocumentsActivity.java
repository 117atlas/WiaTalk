package ensp.reseau.wiatalk.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.models.utils.Document;
import ensp.reseau.wiatalk.ui.adapters.DocumentsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IFileChoosenHandler;

public class DocumentsActivity extends AppCompatActivity implements IFileChoosenHandler {

    private Toolbar toolbar;
    private RecyclerView documentsList;
    private ImageButton close;
    private TextView numberItemsSelected;
    private ImageButton send;

    private ArrayList<String> selectedDocuments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        initializeComponents();
    }

    private void initializeComponents(){
        toolbar = findViewById(R.id.toolbar);
        documentsList = findViewById(R.id.document_list);
        close = findViewById(R.id.back);
        numberItemsSelected = findViewById(R.id.number_items);
        send = findViewById(R.id.send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.documents_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        numberItemsSelected.setText(String.valueOf(selectedDocuments.size()));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String s = "";
                for (String d: selectedDocuments) s = s + "\n" + d;
                Toast.makeText(DocumentsActivity.this, s, Toast.LENGTH_SHORT).show();
                Log.i("SELECTED", s);
                onBackPressed();*/
                Intent output = new Intent();
                output.putExtra("SELECTED_DOCUMENTS", selectedDocuments);
                setResult(RESULT_OK, output);
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        documentsList.setLayoutManager(new LinearLayoutManager(this));
        documentsList.setAdapter(new DocumentsAdapter(this));
        getDocuments();
    }

    private void getDocuments(){
        if (U.checkPermission(this)) ((DocumentsAdapter)documentsList.getAdapter()).setDocuments(getDocumentsList());
    }

    protected ArrayList<Document> getDocumentsList() {

        String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
        String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");

        //Table
        Uri table = MediaStore.Files.getContentUri("external");
        //Column
        String[] column = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE};
        //Where
        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        //args
        String[] args = new String[]{pdf, doc, docx, txt};

        Cursor fileCursor = this.getContentResolver().query(table, column, where, args, null);
        if (fileCursor == null) {
            Log.e(DocumentsActivity.class.getSimpleName(), "Error when Fetchings documents");
            return null;
        }
        if (!fileCursor.moveToFirst()) {
            Log.e(DocumentsActivity.class.getSimpleName(), "0 Documents");
            return null;
        }
        ArrayList<Document> documents = new ArrayList<>();
        int colPath = fileCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        int colSize = fileCursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
        do {
            Document document = new Document();
            document.setFilePath(fileCursor.getString(colPath));
            document.setFileName(document.getFilePath().substring(document.getFilePath().lastIndexOf("/") + 1, document.getFilePath().length()));
            document.setSize(fileCursor.getString(colSize));
            document.setType();
            documents.add(document);
        } while (fileCursor.moveToNext());

        return documents;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case U.READ_EXTERNAL_STORAGE_REQ_PERM_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                    getDocuments();
                }else {
                    // Permission denied
                }
            }
        }
    }

    @Override
    public void fileChoosen(int position, boolean add) {
        if (add && !selectedDocuments.contains(((DocumentsAdapter)documentsList.getAdapter()).getDocuments().get(position).getFilePath()))
            selectedDocuments.add(((DocumentsAdapter)documentsList.getAdapter()).getDocuments().get(position).getFilePath());
        else
            selectedDocuments.remove(((DocumentsAdapter)documentsList.getAdapter()).getDocuments().get(position).getFilePath());
        numberItemsSelected.setText(String.valueOf(selectedDocuments.size()));
    }
}
