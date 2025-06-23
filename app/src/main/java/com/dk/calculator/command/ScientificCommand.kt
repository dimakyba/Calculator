package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine
import com.dk.calculator.OldState

class ScientificCommand(calculator: CalculatorEngine, private val operation: String) :
  CalculatorCommand(calculator) {

  private lateinit var oldState: OldState

  override fun execute() {

    oldState = when (operation) {
      "π" -> {
        OldState(calculator.displayValue, calculator.calculationExpression).also {
          calculator.processNumber(kotlin.math.PI.toString())
        }
      }

      "e" -> {
        OldState(calculator.displayValue, calculator.calculationExpression).also {
          calculator.processNumber(kotlin.math.E.toString())
        }
      }

      else -> calculator.performScientificOperation(operation)

    }
  }

}