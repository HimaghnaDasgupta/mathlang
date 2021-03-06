package math.lang.controller

import math.lang.common.Operand
import math.lang.tokenizer.getOperand
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
class Expression {
    @RequestMapping(value = ["/expression"])
    fun getTemplate(
        @RequestParam(required = false) formula: String? = null,
        map: ModelMap,
        req: HttpServletRequest
    ): String {
        map["formula"] = formula

        return "ExpressionViewer"
    }

    @RequestMapping(value = ["/html"])
    fun getHtml(@RequestParam formula: String, map: ModelMap): String {
        val operand: Operand = getOperand(formula)
        map["operand"] = operand

        return "operands/MainOperand"
    }

}
