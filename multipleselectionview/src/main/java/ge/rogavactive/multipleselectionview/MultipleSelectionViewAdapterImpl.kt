package ge.rogavactive.multipleselectionview

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import ge.rogavactive.multipleselectionview.databinding.ItemListBinding

class MultipleSelectionViewAdapterImpl <I>(
    private val modalTickColor: Int,
    private val modalTextColor: Int,
    private val modalTextSize: Int
) : MultipleSelectionViewAdapter<I>(){

    private var strTransformFun: (I) -> String = {it.toString()}

    override fun onCreateView(parent: ViewGroup): View {
        return ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
    }

    internal fun setTransformFun(transform : (I) -> String) {
        strTransformFun = transform
    }

    override fun onViewUpdate(item: View, data: I, selected: Boolean) {
        val checkBox = (item as FrameLayout).getChildAt(0) as CheckBox
        checkBox.text = strTransformFun(data)
        checkBox.isChecked = selected

        checkBox.buttonTintList = ColorStateList.valueOf(modalTickColor)

        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, modalTextSize.toFloat())
        checkBox.setTextColor(modalTextColor)
    }

}