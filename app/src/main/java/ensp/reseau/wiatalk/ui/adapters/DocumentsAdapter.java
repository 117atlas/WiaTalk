package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.utils.Document;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder> {
    private Context context;
    private ArrayList<Document> documents;

    public DocumentsAdapter(Context context){this.context = context;}

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
        notifyDataSetChanged();
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void add(Document document){
        if (documents==null) documents = new ArrayList<>();
        documents.add(document);
        notifyDataSetChanged();
    }

    @Override
    public DocumentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DocumentsViewHolder(inflater.inflate(R.layout.document_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(DocumentsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return documents==null?0:documents.size();
    }

    class DocumentsViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView size;
        private TextView type;
        private CheckBox select;
        private int currentPosition;
        public DocumentsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.document_name);
            size = itemView.findViewById(R.id.document_size);
            type = itemView.findViewById(R.id.document_type);
            select = itemView.findViewById(R.id.select);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectManager();
                    System.out.println("sasdhasjhdkashdsahkdbksahdksahdkjhskahjk");
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select.isChecked()) select.setChecked(false);
                    else select.setChecked(true);
                    selectManager();
                }
            });
        }

        public void bind(int position){
            currentPosition = position;
            Document document = documents.get(position);
            name.setText(document.getFileName());
            size.setText(normalizeSize(document.getSize()));
            type.setText(document.getType());
            if(document.getType().toLowerCase().contains("pdf")) type.setBackgroundResource(R.drawable.document_pp_pdf);
            else if(document.getType().toLowerCase().contains("doc")) type.setBackgroundResource(R.drawable.document_pp_doc);
            else type.setBackgroundResource(R.drawable.document_pp_txt);
        }

        private void selectManager(){
            if (select.isChecked())
                ((IFileChoosenHandler)context).fileChoosen(currentPosition, true);
            else
                ((IFileChoosenHandler)context).fileChoosen(currentPosition, false);
        }

        private String normalizeSize(String ssize){
            double d = Double.valueOf(ssize);
            double sko = d/1024.0;
            double smo = sko/1024.0;
            String res = "";
            String ext = "";
            if (sko < 1) {
                res = ssize + " o";
                return res;
            }
            else if (smo < 1) {
                res = String.valueOf(sko);
                ext = " ko";
            }
            else {
                res = String.valueOf(smo);
                ext = " Mo";
            }

            if (!res.contains(".")) {return res + ext;}
            else {
                int limit = res.length()-3>=res.indexOf(".")?(res.indexOf("."))+3:res.length();
                return res.substring(0, res.indexOf("."))+"."+res.substring(res.indexOf(".")+1, limit) + ext;
            }
        }
    }
}
