package ge.rogavactive.multipleselectionview

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ge.rogavactive.multipleselectionview.popup.MultipleSelectionListPopupViewHolder

abstract class MultipleSelectionViewAdapter<I> : BaseAdapter() {

    private var mItems: List<I> = emptyList()
    private val mSelectedItems: MutableList<I> = mutableListOf()
    private val mViews: HashMap<Int, MultipleSelectionListPopupViewHolder<I>> = HashMap()

    private var onChangeListener: MultipleSelection.OnSelectionChanged<I>? = null
    private var onFinishedListener: MultipleSelection.OnSelectionFinished<I>? = null
    internal var onShouldUpdateText: (() -> Unit)? = null

    fun setItems(items: List<I>, selectedItems: List<I> = listOf()) {
        mItems = items
        mSelectedItems.clear()
        mSelectedItems.addAll(selectedItems)
        mViews.clear()
        onShouldUpdateText?.invoke()
        notifyDataSetChanged()
    }

    /**
     * Get all items currently in dropdown.
     */
    fun getItems(): List<I> = mItems

    /**
     * Get dropdown selected items list.
     */
    fun getSelectedItems(): List<I> = mSelectedItems.toList()

    /**
     * Instantiate a custom view for selected viewType
     */
    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleSelectionListPopupViewHolder<I>

    /**
     * Function responsible to generate a string for Main TextView when items are selected.
     */
    open fun onFormatSelected(selected: List<I>): String
            = selected.joinToString()

    /**
     * Detect value changes in selection process
     */
    fun setOnChangeListener(listener: MultipleSelection.OnSelectionChanged<I>) {
        onChangeListener = listener
    }

    /**
     * Detect value changes after dismissing the modal.
     */
    fun setOnSelectionFinishedListener(listener: MultipleSelection.OnSelectionFinished<I>) {
        onFinishedListener = listener
    }

    override fun getCount(): Int = mItems.size

    override fun getItem(position: Int): I
            = mItems[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            return convertView
        }
        val v = onCreateViewHolder(parent, getItemViewType(position))
        updateViewWithRelevantState(v, position)
        mViews[position] = v
        v.itemView.setOnClickListener {
            processItemClick(v, position)
        }
        return v.itemView
    }

    internal fun modalDismiss() {
        onFinishedListener?.onFinished(mSelectedItems.toList())
    }

    internal fun getFormattedSelectedText(): String = onFormatSelected(mSelectedItems)

    private fun processItemClick(vh: MultipleSelectionListPopupViewHolder<I>, position: Int) {
        val itemData = mItems[position]
        if (mSelectedItems.contains(itemData)) {
            mSelectedItems.remove(itemData)
        } else {
            mSelectedItems.add(itemData)
        }
        val isSelected = itemData in mSelectedItems
        vh.onStateChange(itemData, isSelected)
        onChangeListener?.onChange(itemData, isSelected)
    }

    private fun updateViewWithRelevantState(vh: MultipleSelectionListPopupViewHolder<I>, position: Int) {
        vh.onStateChange(mItems[position], mItems[position] in mSelectedItems)
    }

}