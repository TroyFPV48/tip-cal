package com.example.tipcal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                TipTimeLayout()
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    val amountState = remember { mutableStateOf("") }
    val customTipState = remember { mutableStateOf("") }
    val selectedTipPercentageState = remember { mutableStateOf(0.0) }

    val amount = amountState.value.toDoubleOrNull() ?: 0.0
    val customTip = customTipState.value.toDoubleOrNull() ?: 0.0
    val tipPercent = if (customTipState.value.isNotEmpty()) customTip else selectedTipPercentageState.value
    val tip = calculateTip(amount, tipPercent)

    Column(
        modifier = Modifier.padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculate Tip",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        EditNumberField(
            value = amountState.value,
            onValueChange = { amountState.value = it },
            label = "Bill Amount"
        )

        TipPercentageButton(
            percentage = 10.0,
            selectedTipPercentageState = selectedTipPercentageState,
            customTipState = customTipState
        )
        TipPercentageButton(
            percentage = 15.0,
            selectedTipPercentageState = selectedTipPercentageState,
            customTipState = customTipState
        )
        TipPercentageButton(
            percentage = 20.0,
            selectedTipPercentageState = selectedTipPercentageState,
            customTipState = customTipState
        )

        Row(Modifier.fillMaxWidth()) {
            Checkbox(
                checked = customTipState.value.isNotEmpty(),
                onCheckedChange = { checked ->
                    if (checked) selectedTipPercentageState.value = 0.0
                    customTipState.value = if (checked) "0" else ""
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Custom Tip"

            )
        }

        if (customTipState.value.isNotEmpty()) {
            EditNumberField(
                value = customTipState.value,
                onValueChange = { customTipState.value = it },
                label = "Enter Custom Tip"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tip Amount: $tip"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun TipPercentageButton(
    percentage: Double,
    selectedTipPercentageState: MutableState<Double>,
    customTipState: MutableState<String>
) {
    Button(
        onClick = {
            selectedTipPercentageState.value = percentage
            customTipState.value = ""
        },
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        val buttonText = if (percentage == 0.0) {
            "Custom"
        } else {
            "$percentage%"
        }
        Text(text = buttonText)
    }
}

fun calculateTip(amount: Double, tipPercent: Double): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}

@Composable
fun TipTimeTheme(content: @Composable () -> Unit) {
    content()
}



