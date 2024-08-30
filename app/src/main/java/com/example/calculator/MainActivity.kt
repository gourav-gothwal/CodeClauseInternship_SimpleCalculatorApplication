package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private var currentInput = ""
    private var operand1: Double? = null
    private var operator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)

        val buttonIds = intArrayOf(
            R.id.buttonAC, R.id.buttonPercent, R.id.buttonClear, R.id.buttonDivide,
            R.id.button7, R.id.button8, R.id.button9, R.id.buttonMultiply,
            R.id.button4, R.id.button5, R.id.button6, R.id.buttonMinus,
            R.id.button1, R.id.button2, R.id.button3, R.id.buttonPlus,
            R.id.button00, R.id.button0, R.id.buttonDot, R.id.buttonEquals
        )

        for (id in buttonIds) {
            val button: Button = findViewById(id)
            button.setOnClickListener { onButtonClick(button) }
        }
    }

    private fun onButtonClick(button: Button) {
        when (button.id) {
            R.id.buttonAC -> {
                currentInput = ""
                resultTextView.text = ""
                operand1 = null
                operator = null
            }
            R.id.buttonPercent -> {
                val inputNumber = currentInput.toDoubleOrNull()
                currentInput = inputNumber?.let { (it / 100).toString() } ?: ""
                resultTextView.text = formatExpression()
            }
            R.id.buttonClear -> {
                if (currentInput.isNotEmpty()) {
                    currentInput = currentInput.dropLast(1)
                    resultTextView.text = formatExpression()
                }
            }
            R.id.buttonDivide, R.id.buttonMultiply, R.id.buttonMinus, R.id.buttonPlus -> {
                if (currentInput.isNotEmpty()) {
                    operand1 = currentInput.toDoubleOrNull()
                    operator = button.text.toString()
                    currentInput = ""
                }
                resultTextView.text = formatExpression()
            }
            R.id.buttonEquals -> {
                val operand2 = currentInput.toDoubleOrNull()
                val result = when (operator) {
                    "/" -> if (operand2 != null && operand2 != 0.0) operand1?.div(operand2) else "Error"
                    "*" -> operand1?.times(operand2 ?: 1.0)
                    "-" -> operand1?.minus(operand2 ?: 0.0)
                    "+" -> operand1?.plus(operand2 ?: 0.0)
                    else -> null
                }
                resultTextView.text = if (result is String) result else "$operand1 $operator $currentInput = ${result ?: ""}"
                currentInput = result?.toString() ?: ""
                operand1 = null
                operator = null
            }
            else -> {
                if (button.text == "." && currentInput.contains(".")) return

                currentInput += button.text
                resultTextView.text = formatExpression()
            }
        }
    }

    private fun formatExpression(): String {
        return buildString {
            if (operand1 != null && operator != null) {
                append("$operand1 $operator $currentInput")
            } else {
                append(currentInput)
            }
        }
    }
}
