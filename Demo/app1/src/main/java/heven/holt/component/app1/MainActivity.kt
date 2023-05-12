package heven.holt.component.app1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import heven.holt.component.app1.databinding.ActivityMainBinding
import heven.holt.component.impl.application.ModuleManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            ModuleManager.registerArr("user")
        }

        binding.unregister.setOnClickListener {
            ModuleManager.unregister("user")
        }
    }
}