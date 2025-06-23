package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

class OperatorCommand(calc: CalculatorEngine, private val op: String) : CalculatorCommand(calc) {
  override fun execute() {
    calculator.applyOperator(op)
  }
}
