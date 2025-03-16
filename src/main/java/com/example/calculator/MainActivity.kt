package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddDecimal: Boolean = true
    private var canAddOperation: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

    }
    fun numberAction(view:View){
        if (view is Button){
            if (view.text=="."){
                if (canAddDecimal) {
                    binding.workingsTV.append(view.text)
                }
                canAddDecimal = false
            }
            else
                binding.workingsTV.append(view.text)
            canAddOperation = true
        }
    }
    fun operationAction(view: View) {
        if (view is Button) {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun allClearAction(view: View) {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.text.length
        if (length > 0){
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
        }
    }
    fun equalAction(view: View) {
        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""
        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""
        val result = addSubstractionCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubstractionCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x')||list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i<restartIndex){
                val operator  = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when (operator){
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i>restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (char in binding.workingsTV.text){
            if (char.isDigit() || char == '.')
                currentDigit += char
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(char)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorTheme {
        Greeting("Android")
    }
}