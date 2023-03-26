package ge.rogavactive.multipleselectionview.popup

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class MultipleSelectionListPopupViewHolder<I>
    (itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * When state of ViewHolder changes this function will get called.
     */
    abstract fun onStateChange(data: I, selected: Boolean)
}