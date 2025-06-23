package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

class InputCommand(calc: CalculatorEngine, private val number: String) : CalculatorCommand(calc) {
  override fun execute() {
    calculator.processNumber(number)
  }

}