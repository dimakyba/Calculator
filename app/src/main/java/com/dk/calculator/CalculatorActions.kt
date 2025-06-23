package com.dk.calculator

import androidx.compose.ui.graphics.Color
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

      "⌫" -> {
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

      "↪" -> {
        cm.redo()
        onStateChanged()
      }

      "↩" -> {
        cm.undo()
        onStateChanged()
      }

      else -> {
        cm.executeCommand(InputCommand(calc, btn))
        onStateChanged()
      }
    }
  }

  fun getColor(btn: String): Color {
    when (btn) {
      "=", "=", "+", "-", "*", "/", "π", "e", "√", "x²", "ln" -> return Color(0xFFFF9F0A)
      "1", "2", "3", "4", "5", "6", "7", "8", "9", "↪", "0", "." -> return Color(0xFF2A2A2C)
      "↩", "C", "⌫" -> return Color(0xFF5C5C5F)
      else -> return Color(0xFFFF1100)
    }
  }
}