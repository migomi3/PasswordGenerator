package com.example.passwordgenerator

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.chip.Chip
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val seekBar: SeekBar = findViewById(R.id.seekBar)
        val specialCharChip: Chip = findViewById(R.id.specialCharactersChip)
        val numberChip: Chip = findViewById(R.id.numbersChip)
        val capitalChip: Chip = findViewById(R.id.capitalChip)
        val createPasswordButton: Button = findViewById(R.id.createPasswordButton)
        val charCountView: TextView = findViewById(R.id.character_count)

        seekBar.min = 4
        seekBar.max = 16
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    charCountView.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    charCountView.text = seekBar.progress.toString()
                }
            }
        )

        var password: String

        createPasswordButton.setOnClickListener() {
            password = GeneratePassword(seekBar.getProgress(), capitalChip.isChecked, numberChip.isChecked, specialCharChip.isChecked)

            showResultDialog(this, password)
        }
    }
}

fun GeneratePassword(length: Int, requiresCapitals: Boolean, requireNumbers: Boolean, requireSpecialCharacters: Boolean) : String {
    val allowedChars: List<Char> =  ('a'..'z') + ('A'..'Z')

    var password: String = List(length) { allowedChars.random()}.joinToString("")
    if (!requiresCapitals) {
        password.lowercase()
    }

    if (requireNumbers) {
        password = replaceRandomCharWithNumber(length, password)
    }
    if (requireSpecialCharacters) {
        password = replaceRandomCharWithSpecialChar(length, password)
    }
    
    return password
}

fun replaceRandomCharWithSpecialChar(length: Int, str: String) : String {
    val specialCharacter: List<Char> = listOf('!', '@','$', '%', '*', '-', '_', '+', ':')
    val indexToReplace: Int = Random.nextInt(0, length)
    val newChar: Char = specialCharacter[Random.nextInt(0, specialCharacter.size)]
    return str.replaceRange(indexToReplace, indexToReplace + 1, newChar.toString())
}

fun replaceRandomCharWithNumber(length: Int, str: String) : String {
    val indexToReplace: Int = Random.nextInt(0, length)
    val numb: String = Random.nextInt(0,10).toString()
    return str.replaceRange(indexToReplace, indexToReplace + 1, numb)
}

fun showResultDialog(context: Context, result: String) {
    val inflater = LayoutInflater.from(context)
    val resultDialog = inflater.inflate(R.layout.password_result_dialog_box, null)

    val resultView = resultDialog.findViewById<TextView>(R.id.password_result_text)
    if (resultView != null) {
        resultView.text = result
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        .setView(resultDialog)

    builder.create().show()
}

