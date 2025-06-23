package com.dk.calculator

import com.dk.calculator.command.BackspaceCommand
import com.dk.calculator.command.CalculateCommand
import com.dk.calculator.command.ClearCommand
import com.dk.calculator.command.CommandManager
import com.dk.calculator.command.DecimalCommand
import com.dk.calculator.command.InputCommand
import com.dk.calculator.command.OperatorCommand
import com.dk.calculator.command.ScientificCommand

object CalculatorActions {
  fun processButtonInput(
    btn: String, calc: CalculatorEngine, cm: CommandManager, onStateChanged: () -> Unit
  ) {
    if (calc.isInErrorState) {
      calc.resetErrorState()
      return
    }
    return when (btn) {
      "C" -> {
        cm.executeCommand(ClearCommand(calc))
        onStateChanged()
      }

      "<=" -> {
        cm.executeCommand(BackspaceCommand(calc))
        onStateChanged()
      }

      "=" -> {

        if (calc.calculationExpression.isNotEmpty() && !calc.calculationExpression.trim()
            .endsWith("=")
        ) {
          try {
            cm.executeCommand(CalculateCommand(calc))
          } catch (e: Exception) {
            calc.setErrorState()
          }
        }
        onStateChanged()
      }

      "+", "-", "*", "/" -> {
        try {
          cm.executeCommand(OperatorCommand(calc, btn))
        } catch (e: Exception) {
          calc.setErrorState()
        }
        onStateChanged()
      }

      "π", "e", "√", "x²", "ln" -> {
        try {
          cm.executeCommand(ScientificCommand(calc, btn))
        } catch (e: Exception) {
          calc.setErrorState()
        }
        onStateChanged()
      }

      "." -> {
        cm.executeCommand(DecimalCommand(calc))
        onStateChanged()
      }

      "->" -> {
        cm.redo()
        onStateChanged()
      }

      "<-" -> {
        cm.undo()
        onStateChanged()
      }

      else -> {
        cm.executeCommand(InputCommand(calc, btn))
        onStateChanged()
      }
    }
  }
}