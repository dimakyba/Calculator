package com.dk.calculator.command

interface Command {
  fun execute()

  fun undo()
}