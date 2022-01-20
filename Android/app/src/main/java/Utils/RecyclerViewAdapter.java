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
    private final ArrayList<String> trainNames;
    private final ArrayList<Integer> trainResId;
    private final Fragment fragment;
    public RecyclerViewAdapter(Fragment fragment, ArrayList<String> trainNames, ArrayList<Integer> trainResId) {
        this.trainNames = trainNames;
        this.trainResId = trainResId;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.imageview.setImageResource(trainResId.get(position));
            holder.text.setText(trainNames.get(position));
            holder.checkBox.setOnClickListener(view -> {
                boolean checked = ((CheckBox) view).isChecked();
                Bundle args = new Bundle();
                args.putBoolean(trainNames.get(position), checked);
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
