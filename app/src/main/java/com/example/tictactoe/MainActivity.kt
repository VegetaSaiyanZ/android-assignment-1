package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttons: Array<Button>
    private lateinit var statusText: TextView
    private lateinit var playAgainButton: Button

    private var playerOneTurn = true
    private var roundCount = 0
    private var boardStatus = Array(3) { IntArray(3) }
    private lateinit var board: Array<Array<Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.tv_status)
        playAgainButton = findViewById(R.id.btn_play_again)

        buttons = Array(9) { i ->
            val buttonID = resources.getIdentifier("btn_$i", "id", packageName)
            findViewById(Button(this).context.resources.getIdentifier("btn_$i", "id", packageName)) // This is a bit clumsy, better to use loops or explicit IDs
        }
        
        // Re-doing the button initialization cleanly
        buttons = arrayOf(
            findViewById(R.id.btn_0), findViewById(R.id.btn_1), findViewById(R.id.btn_2),
            findViewById(R.id.btn_3), findViewById(R.id.btn_4), findViewById(R.id.btn_5),
            findViewById(R.id.btn_6), findViewById(R.id.btn_7), findViewById(R.id.btn_8)
        )

        for (button in buttons) {
            button.setOnClickListener(this)
        }

        playAgainButton.setOnClickListener {
            resetGame()
        }

        initializeBoardStatus()
        updateStatusText()
    }

    override fun onClick(v: View?) {
        if (v !is Button) return
        
        // Find which button was clicked
        var clickedIndex = -1
        for (i in buttons.indices) {
            if (buttons[i] == v) {
                clickedIndex = i
                break
            }
        }

        if (clickedIndex == -1) return

        // Check if button is already used
        if (buttons[clickedIndex].text.toString() != "") {
            return
        }

        if (playerOneTurn) {
            buttons[clickedIndex].text = "X"
            buttons[clickedIndex].setTextColor(ContextCompat.getColor(this, R.color.blue_player))
        } else {
            buttons[clickedIndex].text = "O"
            buttons[clickedIndex].setTextColor(ContextCompat.getColor(this, R.color.red_player))
        }

        roundCount++

        if (checkForWin()) {
            if (playerOneTurn) {
                playerWins("X")
            } else {
                playerWins("O")
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            playerOneTurn = !playerOneTurn
            updateStatusText()
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(9) { i -> buttons[i].text.toString() }

        // Horizontal
        if (checkLine(field[0], field[1], field[2])) return true
        if (checkLine(field[3], field[4], field[5])) return true
        if (checkLine(field[6], field[7], field[8])) return true

        // Vertical
        if (checkLine(field[0], field[3], field[6])) return true
        if (checkLine(field[1], field[4], field[7])) return true
        if (checkLine(field[2], field[5], field[8])) return true

        // Diagonal
        if (checkLine(field[0], field[4], field[8])) return true
        if (checkLine(field[2], field[4], field[6])) return true

        return false
    }

    private fun checkLine(s1: String, s2: String, s3: String): Boolean {
        return s1 == s2 && s1 == s3 && s1 != ""
    }

    private fun playerWins(player: String) {
        statusText.text = getString(R.string.winner_text, player)
        disableButtons()
        playAgainButton.visibility = View.VISIBLE
    }

    private fun draw() {
        statusText.text = getString(R.string.draw_text)
        playAgainButton.visibility = View.VISIBLE
    }

    private fun updateStatusText() {
        val player = if (playerOneTurn) "X" else "O"
        statusText.text = getString(R.string.turn_text, player)
    }

    private fun disableButtons() {
        for (button in buttons) {
            button.isEnabled = false
        }
    }

    private fun resetGame() {
        playerOneTurn = true
        roundCount = 0
        initializeBoardStatus()
        
        for (button in buttons) {
            button.text = ""
            button.isEnabled = true
        }
        
        playAgainButton.visibility = View.INVISIBLE
        updateStatusText()
    }

    private fun initializeBoardStatus() {
        // Not strictly necessary as we read from buttons directly now, but could be useful for state
    }
}
