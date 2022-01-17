package Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.McGregor.chicagotraintracker.R;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecylcerViewAdapater";
    private ArrayList<String> trainNames;
    private ArrayList<Integer> trainResId;
    private Fragment fragment;
    public RecyclerViewAdapter(Fragment fragment, ArrayList<String> trainNames, ArrayList<Integer> trainResId) {
        this.trainNames = trainNames;
        this.trainResId = trainResId;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_list_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.imageview.setImageResource(trainResId.get(position));
            holder.text.setText(trainNames.get(position));
            holder.checkBox.setOnClickListener(view -> {
                boolean checked = ((CheckBox) view).isChecked();
                Bundle args = new Bundle();
                if (checked) {
                    args.putBoolean(trainNames.get(position), true);
                }
                else {
                    args.putBoolean(trainNames.get(position), false);
                }
                fragment.getParentFragmentManager().setFragmentResult("requestKey", args);


            });
        } catch (IndexOutOfBoundsException | Error e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return trainNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView text;
        RelativeLayout parentLayout;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.rImage);
            text = itemView.findViewById(R.id.rText);
            checkBox = itemView.findViewById(R.id.rCheckBox);
            parentLayout = itemView.findViewById(R.id.rLayout);
        }
    }

}
