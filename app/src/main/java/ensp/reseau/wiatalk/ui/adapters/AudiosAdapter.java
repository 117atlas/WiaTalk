package ensp.reseau.wiatalk.ui.adapters;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.utils.Audio;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class AudiosAdapter extends RecyclerView.Adapter<AudiosAdapter.AudiosViewHolder> {
    private Context context;
    private ArrayList<Audio> audios;
    public AudiosAdapter(Context context){this.context = context;}

    public void setAudios(ArrayList<Audio> audios) {
        this.audios = audios;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getAudios() {
        return audios;
    }

    public void add(Audio audio){
        if (audios==null) audios = new ArrayList<>();
        audios.add(audio);
        notifyDataSetChanged();
    }

    @Override
    public AudiosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new AudiosViewHolder(inflater.inflate(R.layout.audio_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(AudiosViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return audios==null?0:audios.size();
    }

    class AudiosViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView title;
        private TextView artist;
        private TextView length;
        private CheckBox select;
        private int currentPosition;
        public AudiosViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.audio_name);
            title = itemView.findViewById(R.id.audio_title);
            artist = itemView.findViewById(R.id.audio_artist);
            length = itemView.findViewById(R.id.audio_length);
            select = itemView.findViewById(R.id.select);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectManager();
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
            Audio audio = audios.get(position);
            name.setText(audio.getFileName());
            title.setText(audio.getTitle());
            artist.setText(audio.getArtist());
            length.setText(normalizeDuration(audio.getLength()));
        }

        private void selectManager(){
            if (select.isChecked())
                ((IFileChoosenHandler)context).fileChoosen(currentPosition, true);
            else
                ((IFileChoosenHandler)context).fileChoosen(currentPosition, false);
        }

        private String normalizeDuration(String sduration){
            long ldur = Long.valueOf(sduration);
            long sec = (int)(ldur/1000);
            String smin = String.valueOf(sec/60);
            String ssec = String.valueOf(sec%60);
            if (ssec.length()==1) ssec = "0"+ssec;
            return smin+":"+ssec;
        }
    }
}
