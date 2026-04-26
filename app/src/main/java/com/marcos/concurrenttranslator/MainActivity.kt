package com.marcos.concurrenttranslator

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcos.concurrenttranslator.data.model.Idioma
import com.marcos.concurrenttranslator.databinding.ActivityMainBinding
import com.marcos.concurrenttranslator.ui.EstadoTraducao
import com.marcos.concurrenttranslator.ui.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

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
        configurarObservadores()
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

    private fun configurarObservadores() {
        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is EstadoTraducao.Inicial -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnTraduzir.isEnabled = true
                    binding.textResultado.text = "A tradução aparecerá aqui."
                }

                is EstadoTraducao.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnTraduzir.isEnabled = false
                    binding.textResultado.text = "Traduzindo..."
                }

                is EstadoTraducao.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnTraduzir.isEnabled = true
                    binding.textResultado.text = estado.textoTraduzido
                }

                is EstadoTraducao.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnTraduzir.isEnabled = true
                    binding.textResultado.text = estado.mensagem
                }
            }
        }
    }

    private fun configurarCliqueTraduzir() {
        binding.btnTraduzir.setOnClickListener {
            val texto = binding.editTexto.text.toString()
            val idiomaOrigem = binding.spinnerOrigem.selectedItem as Idioma
            val idiomaDestino = binding.spinnerDestino.selectedItem as Idioma

            viewModel.traduzir(
                texto = texto,
                idiomaOrigem = idiomaOrigem.codigo,
                idiomaDestino = idiomaDestino.codigo
            )
        }
    }
}