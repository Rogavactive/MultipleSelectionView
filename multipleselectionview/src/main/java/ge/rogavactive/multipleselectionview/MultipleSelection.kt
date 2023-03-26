package ge.rogavactive.multipleselectionview

interface  MultipleSelection {

    fun interface OnSelectionChanged<I>{
        fun onChange(item: I, newSelection: Boolean)
    }

    fun interface OnSelectionFinished<I>{
        fun onFinished(selectedItems: List<I>)
    }

}