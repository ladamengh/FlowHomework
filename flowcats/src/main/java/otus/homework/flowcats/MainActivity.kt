package otus.homework.flowcats

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    catsViewModel.catsStateFlow.collect { state ->
                        state.fact?.let { view.populate(it) }
                    }
                }

                launch {
                    catsViewModel.errorEvents.collect { error ->
                        Toast.makeText(
                            this@MainActivity,
                            error.message ?: error.javaClass.simpleName,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
