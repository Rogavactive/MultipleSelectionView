package ge.rogavactive.multipleselectionview.popup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.ListPopupWindow

class MultipleSelectionListPopupWindow @JvmOverloads constructor(
    context: Context,
    anchorView: View,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
): ListPopupWindow(
    context,
    attrs,
    defStyleAttr
) {

    init {
        setAnchorView(anchorView)
        isModal = true
        promptPosition = POSITION_PROMPT_ABOVE
    }
}