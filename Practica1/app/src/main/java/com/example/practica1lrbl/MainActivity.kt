package com.example.practica1lrbl

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.practica1lrbl.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import kotlin.math.PI

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, TextWatcher {

    private lateinit var binding: ActivityMainBinding
    var formulaSelected: FormulaSelected = FormulaSelected.cone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextNumber.addTextChangedListener(this)
        binding.editTextNumber2.addTextChangedListener(this)
        binding.editTextNumber3.addTextChangedListener(this)
        setUpUI()
        adapt()
    }

    private fun setUpUI() {
        binding.button2.isEnabled = false
        binding.imageView.setImageResource(R.drawable.cilindro)
    }

    private fun adapt() {
        ArrayAdapter.createFromResource(
            this,
            R.array.formulasPicker,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner2.adapter = it
        }
        binding.spinner2.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 != null) {
        val selectedItemPosition = p0.selectedItemPosition
        if (selectedItemPosition == 0) {
            this.formulaSelected = FormulaSelected.cylinder
            binding.imageView.setImageResource(R.drawable.cilindro)
        } else if (selectedItemPosition == 1) {
            this.formulaSelected = FormulaSelected.cone
            binding.imageView.setImageResource(R.drawable.cone1)
        } else if (selectedItemPosition == 2) {
            this.formulaSelected = FormulaSelected.acceleration
            binding.imageView.setImageResource(R.drawable.acceleration)
        }
            setUpTextFields(this.formulaSelected)
            shouldEnableButton()

        }
    }

    private fun shouldEnableButton() {
        var shouldContinue = false
        val operation = this.formulaSelected
        when (operation) {
            FormulaSelected.cylinder -> {
                val isFirstEmpty = binding.editTextNumber.text.isEmpty()
                val isSecondEmpty = binding.editTextNumber2.text.isEmpty()
                shouldContinue = !isFirstEmpty && !isSecondEmpty
            }
            FormulaSelected.cone -> {
                val isFirstEmpty = binding.editTextNumber.text.isEmpty()
                val isSecondEmpty = binding.editTextNumber2.text.isEmpty()
                shouldContinue = !isFirstEmpty && !isSecondEmpty
            }
            FormulaSelected.acceleration -> {
                val isFirstEmpty = binding.editTextNumber.text.isEmpty()
                val isSecondEmpty = binding.editTextNumber2.text.isEmpty()
                val isThirdEmpty = binding.editTextNumber3.text.isEmpty()
                shouldContinue = !isFirstEmpty && !isSecondEmpty && !isThirdEmpty
            }
        }
        binding.button2.isEnabled = shouldContinue
    }

    private fun setUpTextFields(itemSelected: FormulaSelected) {

        when (itemSelected) {
            FormulaSelected.cylinder -> {
                binding.editTextNumber3.isEnabled = false
                binding.editTextNumber.setHint(R.string.radius)
                binding.editTextNumber2.setHint(R.string.height)
                binding.editTextNumber3.setHint(R.string.emptyString)
                binding.editTextNumber3.setText(R.string.emptyString)
            }
            FormulaSelected.cone -> {
                binding.editTextNumber3.isEnabled = false
                binding.editTextNumber.setHint(R.string.radius)
                binding.editTextNumber2.setHint(R.string.height)
                binding.editTextNumber3.setHint(R.string.time)
                binding.editTextNumber3.setHint(R.string.emptyString)
                binding.editTextNumber3.setText(R.string.emptyString)
            }
            FormulaSelected.acceleration -> {
                binding.editTextNumber3.isEnabled = true
                binding.editTextNumber.setHint(R.string.initialSpeed)
                binding.editTextNumber2.setHint(R.string.finalSpeed)
                binding.editTextNumber3.setHint(R.string.time)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    fun continueButton(view: View) {
        val bundle = Bundle()
        val result: Double? = getOperationResult(this.formulaSelected)
        if (result == null) {
            val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.toastText)
                .setPositiveButton(R.string.accept) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(false)
                .show()
        } else {
            bundle.putDouble("result",result)
            val intentNextScreen = Intent(this, DetailActivity::class.java)
            intentNextScreen.putExtras(bundle)
            startActivity(intentNextScreen)
        }
    }

    private fun getOperationResult(operation: FormulaSelected): Double? {
        when (operation) {
            FormulaSelected.cylinder -> {
                val radius: Double = binding.editTextNumber.text.toString().toDouble()
                val height: Double = binding.editTextNumber2.text.toString().toDouble()
                val result: Double = cylindersVolume(radius,height)
                return result
            }
            FormulaSelected.cone -> {
                val radius: Double = binding.editTextNumber.text.toString().toDouble()
                val height: Double = binding.editTextNumber2.text.toString().toDouble()
                val result: Double = coneVolume(radius,height)
                return result
            }
            FormulaSelected.acceleration -> {
                val initialSpeed: Double = binding.editTextNumber.text.toString().toDouble()
                val finalSpeed: Double = binding.editTextNumber2.text.toString().toDouble()
                val time: Double = binding.editTextNumber3.text.toString().toDouble()
                val result = averageSpeed(initialSpeed,finalSpeed,time)
                return result
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        shouldEnableButton()
    }

    fun cylindersVolume(radius: Double, height: Double): Double {
        val pi: Double = PI
        val squaredRadius: Double = radius * radius
        val volume: Double = pi * squaredRadius * height
        return volume
    }

    fun coneVolume(radius: Double, height: Double): Double {
        val pi: Double = PI
        val squaredRadius: Double = radius * radius
        val volume: Double = (pi * squaredRadius * height)/3
        return volume
    }

    fun averageSpeed(initialSpeed: Double, finalSpeed: Double, time: Double): Double? {
        val speedDifference: Double = finalSpeed -initialSpeed
        if (time == 0.0) {
            return null
        }
        val speed: Double = speedDifference / time
        return speed
    }

}


enum class FormulaSelected {
    cylinder, cone, acceleration
}





