package ge.rogavactive.multipleselectionviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.rogavactive.multipleselectionviewapp.databinding.ActivityMainBinding

internal class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mutableList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = binding.defaultMultipleSelectionView.populateAdapterWithItems(
            items = mutableList
        )
        binding.addBtn.setOnClickListener {
            mutableList.add(binding.editText.text.toString())
            adapter.setItems(mutableList.toList())
            binding.editText.text?.clear()
        }
        binding.clearBtn.setOnClickListener {
            mutableList.clear()
            adapter.setItems(mutableList.toList())
        }

    }

}