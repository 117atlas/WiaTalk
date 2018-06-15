package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ensp.reseau.wiatalk.R;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class NumberOfItemsViewHolder extends RecyclerView.ViewHolder {
    private TextView text;
    public NumberOfItemsViewHolder(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.number_items);
    }
    public void bind(int position, Context context){
        text.setText(context.getString(R.string.number_items).replace("???", String.valueOf(position)));
    }
}
