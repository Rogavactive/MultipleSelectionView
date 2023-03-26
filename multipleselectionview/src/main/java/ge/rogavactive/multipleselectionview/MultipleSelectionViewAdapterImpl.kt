package ge.rogavactive.multipleselectionview

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import ge.rogavactive.multipleselectionview.databinding.ItemListBinding
import ge.rogavactive.multipleselectionview.popup.MultipleSelectionListPopupViewHolder


class MultipleSelectionViewAdapterImpl <I>(
    private val modalTickColor: Int,
    private val modalTextColor: Int,
    private val modalTextSize: Int
) : MultipleSelectionViewAdapter<I>(){

    private var strTransformFun: (I) -> String = {it.toString()}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleSelectionListPopupViewHolderImpl {
        return MultipleSelectionListPopupViewHolderImpl(
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    internal fun setTransformFun(transform : (I) -> String) {
        strTransformFun = transform
    }

    inner class MultipleSelectionListPopupViewHolderImpl(private val binding: ItemListBinding) : MultipleSelectionListPopupViewHolder<I>(binding.root) {
        override fun onStateChange(data: I, selected: Boolean) {
            binding.itemCheckbox.text = strTransformFun(data)
            binding.itemCheckbox.isChecked = selected

            binding.itemCheckbox.buttonTintList = ColorStateList.valueOf(modalTickColor)

            binding.itemCheckbox.setTextSize(TypedValue.COMPLEX_UNIT_PX, modalTextSize.toFloat())
            binding.itemCheckbox.setTextColor(modalTextColor)

        }
    }

}