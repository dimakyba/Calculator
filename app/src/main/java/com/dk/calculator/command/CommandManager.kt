package com.dk.calculator.command

import java.util.Stack

class CommandManager {
  private var undoStack = Stack<Command>()
  private var redoStack = Stack<Command>()

  fun executeCommand(command: Command) {
    command.execute()
    undoStack.push(command)
    redoStack.clear()
  }

  fun undo() {
    if (undoStack.count() > 0) {
      var command = undoStack.pop()
      command.undo()
      redoStack.push(command)
    }
  }

  fun redo() {
    if (redoStack.count() > 0) {
      var command = redoStack.pop()
      command.execute()
      undoStack.push(command)
    }
  }
}