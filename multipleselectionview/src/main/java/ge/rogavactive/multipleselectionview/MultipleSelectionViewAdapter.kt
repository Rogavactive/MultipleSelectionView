package ge.rogavactive.multipleselectionview

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class MultipleSelectionViewAdapter<I> : BaseAdapter() {

    private var mItems: List<I> = emptyList()
    private val mSelectedItems: MutableSet<I> = mutableSetOf()

    private var onChangeListener: MultipleSelection.OnSelectionChanged<I>? = null
    private var onFinishedListener: MultipleSelection.OnSelectionFinished<I>? = null
    internal var onShouldUpdateText: (() -> Unit)? = null

    fun setItems(items: List<I>, selectedItems: List<I> = listOf()) {
        mItems = items
        mSelectedItems.clear()
        mSelectedItems.addAll(selectedItems)
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
    abstract fun onCreateView(parent: ViewGroup): View

    /**
     * Called each time a view should be updated.
     */
    abstract fun onViewUpdate(item: View, data: I, selected: Boolean)

    /**
     * Function responsible to generate a string for Main TextView when items are selected.
     */
    open fun onFormatSelected(selected: Set<I>): String
            = selected.sortedBy { it.toString() }.joinToString()

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
        if (convertView == null) {
            val v = onCreateView(parent)
            onViewUpdate(v, mItems[position], mItems[position] in mSelectedItems)
            v.setOnClickListener {
                val item = processItemClick(position)
                onViewUpdate(it, item, item in mSelectedItems)
            }
            return v
        }
        onViewUpdate(convertView, mItems[position], mItems[position] in mSelectedItems)
        convertView.setOnClickListener {
            val item = processItemClick(position)
            onViewUpdate(it, item, item in mSelectedItems)
        }
        return convertView
    }

    internal fun modalDismiss() {
        onFinishedListener?.onFinished(mSelectedItems.toList())
    }

    internal fun getFormattedSelectedText(): String = onFormatSelected(mSelectedItems)

    private fun processItemClick(position: Int): I {
        val itemData = mItems[position]
        if (mSelectedItems.contains(itemData)) {
            mSelectedItems.remove(itemData)
        } else {
            mSelectedItems.add(itemData)
        }
        val isSelected = itemData in mSelectedItems
        onChangeListener?.onChange(itemData, isSelected)
        return itemData
    }

}