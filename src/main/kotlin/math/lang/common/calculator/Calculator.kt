package math.lang.common.calculator

import math.lang.common.Operand
import math.lang.tokenizer.getOperand

import java.util.*

enum class Command(val text: String? = null, val isOperator: Boolean = false, val isPreOperator: Boolean = false) {
    zero("0"),
    one("1"),
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9"),
    add("+", true),
    sub("-", true),
    mul("*", true),
    div("/", true),
    pow("^", true),
    mod("%", true),
    log("log(", true, true),
    ln("ln(", true, true),
    opening("(", true, true),
    closing(")", true, true),
    sin("sin(", true, true),
    cos("cos(", true, true),
    tan("tan(", true, true),
    cot("cot(", true, true),
    sec("sec(", true, true),
    cosec("cosec(", true, true),
    result("="),
    left("LEFT"),
    right("RIGHT"),
    delete("DEL"),
    clear("CLEAR")


}

class Node(
    var cursor: Int = 0,
    val data: StringBuilder = StringBuilder(),
    private val lastAnswer: String = "",
    private val lastOperation: String = ""
) {
    private var history: History? = null

    fun command(command: Command): Boolean {
        if (data.isEmpty() && lastOperation.isNotEmpty() && command == Command.result) {
            if (lastOperation.endsWith("(")) {
                data.append(lastOperation).append(lastAnswer).append(")")
            } else {
                data.append(lastAnswer).append(lastOperation)
            }
            prepareResult()
        } else {
            when (command) {
                Command.result -> prepareResult()
                Command.left -> if (cursor > 0) cursor-- else cursor = 0
                Command.right -> if (cursor < data.length) cursor++ else cursor = data.length
                Command.delete -> if (cursor > 0) data.deleteCharAt(--cursor)
                Command.clear -> data.clear()
                else -> Optional.ofNullable(command.text).ifPresent {
                    if (lastAnswer.isNotEmpty() && data.isEmpty() && command.isOperator) {
                        data.append(if (command.isPreOperator) "${command.text}$lastAnswer" else "$lastAnswer${command.text}")
                        cursor = data.length
                    } else {
                        data.insert(cursor, it)
                        cursor += it.length
                    }
                }
            }
        }
        return when (command) {
            Command.result -> true
            else -> false
        }
    }

    private fun prepareResult() {
        val parsed: Operand = getOperand("$data")
        val result = parsed.calc()
        if (result is Double) {
            history = History(parsed, result.toString())
        } else {
            history = History(parsed, result.toString())
        }
    }

    fun getHistory(): History? = history
}

class History(val operand: Operand, val result: String)