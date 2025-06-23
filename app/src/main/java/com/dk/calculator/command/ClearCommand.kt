package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

class ClearCommand(calc: CalculatorEngine) : CalculatorCommand(calc) {

  override fun execute() {
    calculator.clear()
  }
}