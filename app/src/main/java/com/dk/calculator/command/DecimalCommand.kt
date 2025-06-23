package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

class DecimalCommand(calc: CalculatorEngine): CalculatorCommand(calc) {

  override fun execute() {
    calculator.addDecimalPoint()
  }
}