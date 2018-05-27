package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.Call;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.fragment.ViewPhotoFragment;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallViewHolder> {
    private Context context;
    private ArrayList<Call> list;

    public CallsAdapter(Context context){
        this.context = context;
    }

    public void setList(ArrayList<Call> calls){
        list = calls;
        notifyDataSetChanged();
    }

    public void addCall(Call call){
        if (list==null) list = new ArrayList<>();
        list.add(call);
        notifyDataSetChanged();
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CallViewHolder(inflater.inflate(R.layout.call_listitem_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class CallViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView pp;
        private TextView initiales;
        private TextView discName;
        private ImageView type;
        private TextView date;
        private ImageButton call;
        private ImageView mode;
        private ImageButton videoCall;

        private int currentPosition;

        public CallViewHolder(View itemView) {
            super(itemView);

            pp = itemView.findViewById(R.id.pp);
            initiales = itemView.findViewById(R.id.discussion_initiales);
            discName = itemView.findViewById(R.id.contact_name);
            type = itemView.findViewById(R.id.call_type);
            date = itemView.findViewById(R.id.date);
            call = itemView.findViewById(R.id.call);
            mode = itemView.findViewById(R.id.call_mode);
            videoCall = itemView.findViewById(R.id.video_call);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
                }
            });
            videoCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Video Call", Toast.LENGTH_SHORT).show();
                }
            });

            pp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewPhotoFragment viewPhotoFragment = ViewPhotoFragment.newInstance(null, new User("id", "697266488", "Samar", "pp3.jpg"));
                    viewPhotoFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), ViewPhotoFragment.class.getSimpleName());
                }
            });
        }

        public void bind(int position){
            currentPosition = position;
            Call call = list.get(position);

            if (call.getPp()==null){
                pp.setVisibility(View.GONE);
                initiales.setVisibility(View.VISIBLE);
                initiales.setText(U.Initiales(call.getContact()));
            }
            else{
                pp.setVisibility(View.VISIBLE);
                initiales.setVisibility(View.GONE);
                //pp.setImageURI(Uri.parse("assets://"+call.getPp()));
                U.loadImage(context, pp, call.getPp());
            }

            discName.setText(call.getContact());

            type.setImageResource(call.getType()==Call.TYPE_RECEIVED?R.drawable.ic_call_received:R.drawable.ic_call_made);

            date.setText(U.NormalizeDate(call.getDate(), context));
        }
    }
}
