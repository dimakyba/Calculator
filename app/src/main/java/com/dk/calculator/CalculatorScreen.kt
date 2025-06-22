package com.dk.calculator


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorScreen(modifier: Modifier) {
  var isScientific by remember { mutableStateOf(false) }
  var expression by remember { mutableStateOf("") }

  Box(modifier = Modifier) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.End
    ) {
      Spacer(modifier = Modifier.size(10.dp))
      Button(onClick = { isScientific = !isScientific }) {
        Text(text = "Sc")
      }
      Text(
        text = expression,
        style = TextStyle(
          fontSize = 30.sp,
          textAlign = TextAlign.End
        ),
        maxLines = 5,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = "246",
        style = TextStyle(
          fontSize = 60.sp,
          textAlign = TextAlign.End
        ),
        maxLines = 5,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(10.dp))
      CalculatorButtonMatrix(isScientific) { btn ->
        expression = processButtonInput(
          btn,
          expression
        )
      }
    }
  }
}


val buttonList = listOf(
  "<-", "C", "<=", "/",
  "7", "8", "9", "*",
  "4", "5", "6", "-",
  "1", "2", "3", "+",
  "->", "0", ".", "="
)

data class CalcButton(
  val label: String,
  val isScientific: Boolean = false
)

val allButtons = listOf(
  CalcButton("<-", false),
  CalcButton("C", false),
  CalcButton("<=", false),
  CalcButton("/", false),
  CalcButton("pi", true),
  CalcButton("7", false),
  CalcButton("8", false),
  CalcButton("9", false),
  CalcButton("*", false),
  CalcButton("e", true),
  CalcButton("4", false),
  CalcButton("5", false),
  CalcButton("6", false),
  CalcButton("-", false),
  CalcButton("v", true),
  CalcButton("1", false),
  CalcButton("2", false),
  CalcButton("3", false),
  CalcButton("+", false),
  CalcButton("x^2", true),
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
      onClick = onClick,
      modifier = Modifier
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

fun processButtonInput(btn: String, currentExpr: String): String {
  return when (btn) {
    "C" -> ""
    "<=" -> if (currentExpr.isNotEmpty()) currentExpr.dropLast(1) else ""
    "=" -> currentExpr
    else -> currentExpr + btn
  }
}

