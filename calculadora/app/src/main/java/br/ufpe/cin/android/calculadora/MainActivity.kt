package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // Aqui estamos salvando o estado para quando ele destruir a activity
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val editText : EditText = findViewById(R.id.text_calc)
        val textView : TextView = findViewById(R.id.text_info)

        outState?.putString(R.id.text_calc.toString(), editText.text.toString())
        outState?.putString(R.id.text_info.toString(), textView.text.toString())
    }

    //Aqui estamos recuperando o estado para pegar as informações antes da mudança de configuração
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText : EditText = findViewById(R.id.text_calc)
        val textView : TextView = findViewById(R.id.text_info)

        val editTextValue : String? = savedInstanceState?.getString(R.id.text_calc.toString(), "")
        val textViewValue : String? = savedInstanceState?.getString(R.id.text_info.toString(), "")

        editText.setText(editTextValue)
        textView.text = textViewValue
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Aqui estou setando os listeners de cada botão para escrever as informações na tela
        btn_0.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("0")
        }

        btn_1.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("1")
        }

        btn_2.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("2")
        }

        btn_3.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("3")
        }

        btn_4.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("4")
        }

        btn_5.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("5")
        }

        btn_6.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("6")
        }

        btn_7.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("7")
        }

        btn_8.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("8")
        }

        btn_9.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("9")
        }

        btn_Dot.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append(".")
        }

        btn_Add.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("+")
        }

        btn_Subtract.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("-")
        }

        btn_Divide.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("/")
        }

        btn_Multiply.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("*")
        }

        btn_Power.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("^")
        }

        btn_LParen.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append("(")
        }

        btn_RParen.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            editText.append(")")
        }

        // Aqui estou fazendo o clear para limpar o EditText e o TextView
        btn_Clear.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            val textView : TextView = findViewById(R.id.text_info)
            editText.setText("")
            textView.text = ""
        }

        // Aqui estou fazendo o calculo da expressão e colocando no TextView e, caso seja inválida, aparecer um toast informando o usuário
        btn_Equal.setOnClickListener {
            val editText : EditText = findViewById(R.id.text_calc)
            val textView : TextView = findViewById(R.id.text_info)
            try {
                val calculated : Double = eval(editText.text.toString())
                textView.text = calculated.toString()
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Expressão Inválida!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
