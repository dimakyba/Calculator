package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

class BackspaceCommand(calc: CalculatorEngine) : CalculatorCommand(calc) {

  override fun execute() {
    calculator.backspace()
  }
}