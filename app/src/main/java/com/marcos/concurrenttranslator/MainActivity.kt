package com.marcos.concurrenttranslator

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
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
        configurarCliqueInverterIdiomas()
        configurarCliqueColarTexto()
        configurarCliqueCopiarResultado()
        configurarObservadores()
        configurarCliqueTraduzir()
    }

    private fun configurarBarrasDoSistema() {
        WindowCompat.getInsetsController(window, binding.root).isAppearanceLightStatusBars = true

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
            R.layout.item_spinner_selected,
            idiomas
        )

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)

        binding.spinnerOrigem.adapter = adapter
        binding.spinnerDestino.adapter = adapter

        binding.spinnerOrigem.setSelection(0)
        binding.spinnerDestino.setSelection(1)
    }

    private fun configurarObservadores() {
        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is EstadoTraducao.Inicial -> {
                    binding.btnTraduzir.isEnabled = true
                    binding.btnTraduzir.text = getString(R.string.translate_button)
                    binding.textResultado.text = getString(R.string.result_placeholder)
                }

                is EstadoTraducao.Carregando -> {
                    binding.btnTraduzir.isEnabled = false
                    binding.btnTraduzir.text = getString(R.string.translate_button)
                    binding.textResultado.text = getString(R.string.result_loading)
                }

                is EstadoTraducao.Sucesso -> {
                    binding.btnTraduzir.isEnabled = true
                    binding.btnTraduzir.text = getString(R.string.translate_button)
                    binding.textResultado.text = estado.textoTraduzido
                }

                is EstadoTraducao.Erro -> {
                    binding.btnTraduzir.isEnabled = true
                    binding.btnTraduzir.text = getString(R.string.translate_button)
                    binding.textResultado.text = estado.mensagem
                }
            }
        }
    }

    private fun configurarCliqueInverterIdiomas() {
        binding.btnInverterIdiomas.setOnClickListener {
            val origemSelecionada = binding.spinnerOrigem.selectedItemPosition
            val destinoSelecionado = binding.spinnerDestino.selectedItemPosition

            binding.spinnerOrigem.setSelection(destinoSelecionado)
            binding.spinnerDestino.setSelection(origemSelecionada)
        }
    }

    private fun configurarCliqueColarTexto() {
        binding.btnColarTexto.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            val textoColado = if (clip != null && clip.itemCount > 0) {
                clip.getItemAt(0).coerceToText(this).toString().trim()
            } else {
                ""
            }

            if (textoColado.isBlank()) {
                Toast.makeText(this, getString(R.string.clipboard_empty_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.editTexto.setText(textoColado)
            binding.editTexto.setSelection(textoColado.length)
        }
    }

    private fun configurarCliqueCopiarResultado() {
        binding.btnCopiarResultado.setOnClickListener {
            val textoResultado = binding.textResultado.text.toString().trim()
            val semResultado = textoResultado.isBlank() ||
                textoResultado == getString(R.string.result_placeholder) ||
                textoResultado == getString(R.string.result_loading)

            if (semResultado) {
                Toast.makeText(this, getString(R.string.result_empty_copy_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("resultado_traducao", textoResultado)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, getString(R.string.result_copied_message), Toast.LENGTH_SHORT).show()
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