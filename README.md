# Multiple Selection View

[![](https://jitpack.io/v/Rogavactive/MultipleSelectionView.svg)](https://jitpack.io/#Rogavactive/MultipleSelectionView)
![build status](https://github.com/Rogavactive/MultipleSelectionView/actions/workflows/android.yml/badge.svg?branch=main)

# Overview
MultipleSelectionView is an Android library that provides a custom view for multiple selection with modal. You can include it in your XML layout files and customize the UI and logic to suit your needs.

# Features
- Display a spinner with multiple selection functionality
- Basic functionality: pass a list of items and listen to changes
- Control displayed text after selection: use the provided function to generate a string from selected data
- Custom adapter: create your own adapter from our abstract class and pass it to dropdown UI and logic
- It is a parent of TextView and you can use all functions that TextView can, except "text"
# Getting Started
To use MultipleSelectionView in your Android project, you need to first add the following code to your root build.gradle file at the end of the repositories section:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Next, add the following code to your app module's build.gradle file to include MultipleSelectionView as a dependency:

```gradle
dependencies {
    implementation 'com.github.Rogavactive:MultipleSelectionView:1.0.1'
}
```
# Basic functionality

First add it to your xml file
```xml
    <ge.rogavactive.multipleselectionview.MultipleSelectionView
        android:id="@+id/default_multiple_selection_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Select options"
        android:padding="10dp"
        app:msv_modal_text_size="14sp"
        app:msv_modal_background="@color/white"
        app:msv_modal_text_color="@color/black"
        app:msv_modal_tick_color="@color/teal_700"
        app:layout_constraintTop_toTopOf="parent"
        tools:ignore="HardcodedText" />
```
- Adjust text size in Basic Implementation dropdown items with `app:msv_modal_text_size`
- Change modal text color by setting `app:msv_modal_text_size`
- Change modal tick color by setting `app:msv_modal_tick_color`
- Set dropdown background with `app:msv_modal_background`

After that retrieve view in your Activity/Fragment and populate adapter with data
```kotlin
      val adapter = binding.defaultMultipleSelectionView.populateAdapterWithItems(
          items = itemList,
          selectedItems = selectedItemsList,
          itemTransformFun = { "Option: $it" }
      )
```
- pass dropdown list to `items`
- (Optional) pass `selectedItems` to indicate which items are selected at start.
- (Optional)  `itemTransformFun` is a function that takes data on each list item in dropdown and produces a string which will be displayed in its place

In order to change main view you can treat it as a `TextView` . Every functionality of `TextView` is present here except setting a text from xml.

# Extended functionality
If basic functionality is not enough for us we can do more to create more manageable UI and logic.
1. Create a ViewHolder which extends `MultipleSelectionListPopupViewHolder`
```kotlin
    inner class MultipleSelectionListPopupViewHolderImpl(private val binding: ItemListBinding) : MultipleSelectionListPopupViewHolder<I>(binding.root) {
        override fun onStateChange(data: I, selected: Boolean) {
          // Logic goes here
        }
    }
```
onStateChange gets called each time a view state changes and should get updated.

2. Create an Adapter class and extend `MultipleSelectionViewAdapter<I>`
```kotlin
  class MultipleSelectionViewAdapterImpl <I>() : MultipleSelectionViewAdapter<I>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleSelectionListPopupViewHolderImpl {
        return MultipleSelectionListPopupViewHolderImpl(
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
  }
```
override onCreateViewHolder (Note that I am using viewBinding here, but it is not required in your implementation)

3. Set data with `adapter.setItems(data)`

4. Listen to changes with one of the two listeners: `setOnChangeListener` and `setOnSelectionFinishedListener`

# Interaction with `MultipleSelectionViewAdapter<I>`
1. Use `setItems` to set list of items to display in dropdown and (Optional) selected items at start.
```kotlin
  fun setItems(items: List<I>, selectedItems: List<I> = listOf())
```
2. Listen to each selection change
```kotlin
  fun setOnChangeListener(listener: MultipleSelection.OnSelectionChanged<I>)
```
3. Listen to overall changes after modal dismiss
```kotlin
  fun setOnSelectionFinishedListener(listener: MultipleSelection.OnSelectionFinished<I>)
```
4. override `onFormatSelected` in your adapter implementation in order to control how selected items will be displayed
```kotlin
  open fun onFormatSelected(selected: List<I>): String
```

# Contributing
Developers can contribute to the development of MultipleSelectionView by creating pull requests and closing issues or creating new issues. We welcome contributions from the community and appreciate your help in making MultipleSelectionView better for everyone.

# License
MultipleSelectionView is released under the MIT License. See [LICENSE](https://github.com/Rogavactive/MultipleSelectionView/blob/main/LICENSE) for details.
