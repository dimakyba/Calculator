package com.dk.calculator.command

import com.dk.calculator.CalculatorEngine

abstract class CalculatorCommand(calculator: CalculatorEngine) : Command {
  protected val calculator = calculator
  protected val oldDisplayValue = calculator.displayValue
  protected val oldCalculationExpression = calculator.calculationExpression
  protected val oldIsNewOperaion = calculator.isNewOperation


  abstract override fun execute()

  open override fun undo() {
    calculator.displayValue = oldDisplayValue
    calculator.calculationExpression = oldCalculationExpression
    calculator.isNewOperation = oldIsNewOperaion
  }

}