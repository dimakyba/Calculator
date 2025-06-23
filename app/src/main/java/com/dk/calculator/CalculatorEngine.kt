package com.dk.calculator

data class OldState(val display: String, val expression: String)

data class CalculationSnapshot(
  val oldDisplay: String,
  val oldExpression: String,
  val firstOperand: Double,
  val secondOperand: Double,
  val op: String
)

class CalculatorEngine {
  var displayValue = "0"
    set(value) {
      field = value
    }
  var calculationExpression = ""
    set(value) {
      field = value
    }
  var isNewOperation = true
    set(value) {
      field = value
    }
  var isInErrorState = false
    set(value) {
      field = value
    }

//  fun setDisplayValue(value: String) {
//    displayValue = value
//  }
//
//  fun setCalculationExpression(expression: String) {
//    calculationExpression = expression
//  }
//
//  fun setNewOperation(value: Boolean) {
//    isNewOperation = value
//  }

  fun addDecimalPoint() {
    if (isNewOperation) {
      displayValue = "0,"
      isNewOperation = false
    } else if (!displayValue.contains(",")) {
      displayValue += ","
    }
  }

  fun backspace() {
    if (displayValue.length == 1 || (displayValue.length == 2 && displayValue[0] == '-')) {
      displayValue = "0"
      isNewOperation = true
    } else {
      displayValue = displayValue.dropLast(1)

    }
  }

  fun clear() {
    displayValue = "0"
    calculationExpression = ""
    isNewOperation = true
  }

  fun processNumber(number: String) {
    if (isNewOperation) {
      displayValue = number
      isNewOperation = false
    } else {
      val currentValue = displayValue.replace(",", "").replace("-", "")
      if (currentValue.length >= 24) return

      if (displayValue == "0" && number != ",") displayValue = number
      else displayValue += number

      if (number == "π" || number == "e") applyOperator("*")

    }
  }

  fun applyOperator(op: String) {
    if (displayValue == "π") displayValue = kotlin.math.PI.toString()
    else if (displayValue == "e") displayValue = kotlin.math.E.toString()

    calculationExpression = "$displayValue $op"
    isNewOperation = true
  }

  fun performScientificOperation(
    op: String
  ): OldState {
    val oldState = OldState(displayValue, calculationExpression)

    var input = displayValue.toDoubleOrNull() ?: throw IllegalArgumentException("bad input")

    var result: Double
    var newExpression: String

    when (op) {
      "π" -> {
        result = kotlin.math.PI
        newExpression = "π"
      }

      "e" -> {
        result = kotlin.math.E
        newExpression = "e"
      }

      "√" -> {
        if (input < 0) throw IllegalArgumentException("sqrt only on non-negative")
        result = kotlin.math.sqrt(input)
        newExpression = "√($input)"
      }

      "x²" -> {
        result = input * input
        newExpression = "$input²"
      }

      "ln" -> {
        if (input <= 0) throw IllegalArgumentException("ln only on positive")
        result = kotlin.math.ln(input)
        newExpression = "ln($input)"
      }

      else -> throw IllegalArgumentException()

    }
    displayValue = result.toString()
    calculationExpression = newExpression
    isNewOperation = true

    return oldState
  }

  fun setErrorState() {

    displayValue = "Error"
    calculationExpression = ""
    isNewOperation = true
    isInErrorState = true
  }

  fun resetErrorState() {
    if (isInErrorState) {
      displayValue = "0"
      calculationExpression = ""
      isNewOperation = true
      isInErrorState = false
    }
  }

  fun calculate(): CalculationSnapshot {
    val oldDisplay = displayValue
    val oldExpression = calculationExpression

    val parts = calculationExpression.split(" ")

    val firstOperand = when (parts[0]) {
      "π" -> kotlin.math.PI
      "e" -> kotlin.math.E
      else -> parts[0].toDoubleOrNull() ?: throw IllegalArgumentException()
    }

    val op = parts[1]

    val secondOperand = when (displayValue) {
      "π" -> kotlin.math.PI
      "e" -> kotlin.math.E
      else -> displayValue.toDoubleOrNull() ?: throw IllegalArgumentException()
    }

    val result = when (op) {
      "+" -> firstOperand + secondOperand
      "-" -> firstOperand - secondOperand
      "*" -> firstOperand * secondOperand
      "/" -> {
        if (secondOperand == 0.0) {
          throw ArithmeticException("Divide by zero")
        }
        firstOperand / secondOperand
      }

      else -> throw IllegalArgumentException("Error")
    }

    calculationExpression = "$calculationExpression $displayValue ="

    val resultString = result.toString()
//    displayValue = if (resultString.endsWith("0")) {
//      resultString.dropLast(2)
//    } else {
//      resultString
//    }

    displayValue = resultString
    isNewOperation = true


    return CalculationSnapshot(oldDisplay, oldExpression, firstOperand, secondOperand, op)
  }
}
