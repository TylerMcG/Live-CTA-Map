package Utils

import java.util.ArrayList
import android.util.Log
import android.os.Bundle
import com.McGregor.chicagotraintracker.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.CheckBox
import java.lang.IndexOutOfBoundsException
import android.widget.ImageView
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.Error

class RecyclerViewAdapter(private val fragment: Fragment, private val trainNames: ArrayList<String>, private val trainResId: ArrayList<Int>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.train_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.imageview.setImageResource(trainResId[position])
            holder.text.text = trainNames[position]
            holder.checkBox.setOnClickListener { view: View ->
                val checked = (view as CheckBox).isChecked
                val args = Bundle()
                args.putBoolean(trainNames[position], checked)
                fragment.parentFragmentManager.setFragmentResult("requestKey", args)
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.d(TAG, e.message!!)
        } catch (e: Error) {
            Log.d(TAG, e.message!!)
        }
    }

    override fun getItemCount(): Int {
        return trainNames.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageview: ImageView = itemView.findViewById(R.id.rImage)
        var text: TextView = itemView.findViewById(R.id.rText)
        var checkBox: CheckBox = itemView.findViewById(R.id.rCheckBox)

    }

    companion object {
        private const val TAG = "RecylcerViewAdapater"
    }
}