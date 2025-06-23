package com.dk.calculator.command

import com.dk.calculator.CalculationSnapshot
import com.dk.calculator.CalculatorEngine

class CalculateCommand(calculator: CalculatorEngine) : CalculatorCommand(calculator) {
  private lateinit var snapshot: CalculationSnapshot

  override fun execute() {
    snapshot = calculator.calculate()
  }

  override fun undo() {
    calculator.displayValue = snapshot.oldDisplay
    calculator.calculationExpression = snapshot.oldExpression
    calculator.isNewOperation = true
  }
}