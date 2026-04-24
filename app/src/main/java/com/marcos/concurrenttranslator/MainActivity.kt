package com.marcos.concurrenttranslator

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcos.concurrenttranslator.data.model.Idioma
import com.marcos.concurrenttranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val idiomas = listOf(
        Idioma("Português", "pt"),
        Idioma("Inglês", "en"),
        Idioma("Espanhol", "es"),
        Idioma("Francês", "fr"),
        Idioma("Alemão", "de"),
        Idioma("Italiano", "it")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBarrasDoSistema()
        configurarSpinners()
        configurarCliqueTraduzir()
    }

    private fun configurarBarrasDoSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun configurarSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            idiomas
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerOrigem.adapter = adapter
        binding.spinnerDestino.adapter = adapter

        binding.spinnerOrigem.setSelection(0)
        binding.spinnerDestino.setSelection(1)
    }

    private fun configurarCliqueTraduzir() {
        binding.btnTraduzir.setOnClickListener {
            val texto = binding.editTexto.text.toString()
            val idiomaOrigem = binding.spinnerOrigem.selectedItem as Idioma
            val idiomaDestino = binding.spinnerDestino.selectedItem as Idioma

            binding.textResultado.text = """
                Texto informado: $texto
                
                Idioma de origem: ${idiomaOrigem.nome}
                Idioma de destino: ${idiomaDestino.nome}
            """.trimIndent()
        }
    }
}