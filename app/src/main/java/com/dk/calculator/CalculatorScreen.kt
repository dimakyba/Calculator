package com.dk.calculator


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.dk.calculator.command.BackspaceCommand
import com.dk.calculator.command.CalculateCommand
import com.dk.calculator.command.ClearCommand
import com.dk.calculator.command.CommandManager
import com.dk.calculator.command.DecimalCommand
import com.dk.calculator.command.InputCommand
import com.dk.calculator.command.OperatorCommand
import com.dk.calculator.command.ScientificCommand


@Composable
fun CalculatorScreen(modifier: Modifier) {
  var calc = remember { CalculatorEngine() }
  var cm = remember { CommandManager() }
  var isScientific by remember { mutableStateOf(false) }
  var display by remember { mutableStateOf(calc.displayValue) }
  var expression by remember { mutableStateOf(calc.calculationExpression) }

  fun updateState() {
    display = calc.displayValue
    expression = calc.calculationExpression
  }


  Box(modifier = Modifier) {
    Column(
      modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End
    ) {
      Spacer(modifier = Modifier.size(10.dp))
      Button(onClick = { isScientific = !isScientific }) {
        Text(text = "Sc")
      }
      AutoSizeText(
        text = expression,
        maxFontSize = 30.sp,
        minFontSize = 15.sp,
        style = TextStyle(textAlign = TextAlign.End),
        modifier = Modifier.fillMaxWidth()
      )

      AutoSizeText(
        text = display,
        maxFontSize = 60.sp,
        minFontSize = 30.sp,
        style = TextStyle(textAlign = TextAlign.End),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))
      CalculatorButtonMatrix(isScientific) { btn ->
        processButtonInput(
          btn, calc, cm, onStateChanged = { updateState() })
      }
    }
  }
}


data class CalcButton(
  val label: String, val isScientific: Boolean = false
)

val allButtons = listOf(
  CalcButton("<-", false),
  CalcButton("C", false),
  CalcButton("<=", false),
  CalcButton("/", false),
  CalcButton("π", true),
  CalcButton("7", false),
  CalcButton("8", false),
  CalcButton("9", false),
  CalcButton("*", false),
  CalcButton("e", true),
  CalcButton("4", false),
  CalcButton("5", false),
  CalcButton("6", false),
  CalcButton("-", false),
  CalcButton("√", true),
  CalcButton("1", false),
  CalcButton("2", false),
  CalcButton("3", false),
  CalcButton("+", false),
  CalcButton("x²", true),
  CalcButton("->", false),
  CalcButton("0", false),
  CalcButton(".", false),
  CalcButton("=", false),
  CalcButton("ln", true)
)

@Composable
fun CalculatorButton(btn: String, onClick: () -> Unit) {
  Box(modifier = Modifier.padding(8.dp)) {
    FloatingActionButton(
      onClick = onClick, modifier = Modifier
        .size(80.dp)
        .shadow(0.dp), shape = CircleShape
    ) {
      Text(text = btn)
    }
  }

}

@Composable
fun CalculatorButtonMatrix(isScientific: Boolean, onButtonClick: (String) -> Unit) {
  val buttonsToShow = if (isScientific) {
    allButtons
  } else {
    allButtons.filter { !it.isScientific }
  }
  LazyVerticalGrid(
    columns = GridCells.Fixed(if (isScientific) 5 else 4)
  ) {
    items(buttonsToShow, key = { it.label }) { btn ->
      CalculatorButton(btn.label) {
        onButtonClick(btn.label)
      }
    }
  }
}

@Composable
fun AutoSizeText(
  text: String,
  maxFontSize: TextUnit = 24.sp,
  minFontSize: TextUnit = 12.sp,
  modifier: Modifier = Modifier,
  maxChars: Int = 20,
  style: TextStyle = TextStyle.Default,
  textAlign: TextAlign = TextAlign.End
) {
  val fontSize = remember(text) {
    val length = text.length
    if (length <= maxChars) maxFontSize
    else {
      val scale = maxChars.toFloat() / length
      val scaled = (maxFontSize.value * scale).coerceAtLeast(minFontSize.value)
      scaled.sp
    }
  }
  Text(
    text = text,
    style = style.copy(fontSize = fontSize, textAlign = textAlign),
    maxLines = 1,
    overflow = TextOverflow.Clip,
    modifier = modifier
  )
}


fun processButtonInput(
  btn: String, calc: CalculatorEngine, cm: CommandManager, onStateChanged: () -> Unit
) {
  // If in error state, only allow 'C' (clear) button
  if (calc.isInErrorState) {
    calc.resetErrorState()
    return
  }
  return when (btn) {
    "C" -> {
      cm.executeCommand(ClearCommand(calc))
      onStateChanged()
    }

    "<=" -> {
      cm.executeCommand(BackspaceCommand(calc))
      onStateChanged()
    }

    "=" -> {

      if (calc.calculationExpression.isNotEmpty() && !calc.calculationExpression.trim()
          .endsWith("=")
      ) {
        try {
          cm.executeCommand(CalculateCommand(calc))
        } catch (e: Exception) {
          calc.setErrorState()
        }
      }
      onStateChanged()
    }

    "+", "-", "*", "/" -> {
      try {
        cm.executeCommand(OperatorCommand(calc, btn))
      } catch (e: Exception) {
        calc.setErrorState()
      }
      onStateChanged()
    }

    "π", "e", "√", "x²", "ln" -> {
      try {
        cm.executeCommand(ScientificCommand(calc, btn))
      } catch (e: Exception) {
        calc.setErrorState()
      }
      onStateChanged()
    }

    "." -> {
      cm.executeCommand(DecimalCommand(calc))
      onStateChanged()
    }

    "->" -> {
      cm.redo()
      onStateChanged()
    }

    "<-" -> {
      cm.undo()
      onStateChanged()
    }

    else -> {
      cm.executeCommand(InputCommand(calc, btn))
      onStateChanged()
    }
  }
}
