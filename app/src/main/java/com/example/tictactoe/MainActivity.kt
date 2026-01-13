package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var boardState by remember { mutableStateOf(Array(3) { Array(3) { R.drawable.empty } }) }
    var currentPlayer by remember { mutableStateOf(R.drawable.x) }
    var gameEnded by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf(R.drawable.empty) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (!gameEnded) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Player Turn: ",
                    color = Color.Black
                )
                Image(
                    painter = painterResource(id = currentPlayer),
                    contentDescription = "Current Player",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 8.dp)
                )
            }
        } else {
            Text(
                text = "Game Over",
                color = Color.Black
            )
        }

        TicTacToeBoard(
            boardState = boardState,
            onCellClick = { row, col ->
                if (!gameEnded && boardState[row][col] == R.drawable.empty) {
                    boardState = boardState.mapIndexed { r, rowArray ->
                        rowArray.mapIndexed { c, cell ->
                            if (r == row && c == col) currentPlayer else cell
                        }.toTypedArray()
                    }.toTypedArray()

                    if (checkWin(boardState)) {
                        gameEnded = true
                        winner = currentPlayer
                    } else if (isDraw(boardState)) {
                        gameEnded = true
                    } else {
                        currentPlayer =
                            if (currentPlayer == R.drawable.x) R.drawable.o else R.drawable.x
                    }
                }
            }
        )

        if (gameEnded) {
            if (winner == R.drawable.empty) {
                Text(text = "It's a Draw!", color = Color.Black)
            } else {
                Text(
                    text = "Winner: ${if (winner == R.drawable.x) "X" else "O"}",
                    color = Color.Black
                )
            }

            Button(
                onClick = {
                    boardState = Array(3) { Array(3) { R.drawable.empty } }
                    currentPlayer = R.drawable.x
                    gameEnded = false
                    winner = R.drawable.empty
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "New Game")
            }
        }
    }
}

@Composable
fun TicTacToeBoard(
    boardState: Array<Array<Int>>,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    TicTacToeCell(
                        resource = boardState[row][col],
                        onClick = { onCellClick(row, col) }
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeCell(resource: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = resource),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = resource == R.drawable.empty, onClick = onClick)
        )
    }
}

fun checkWin(board: Array<Array<Int>>): Boolean {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != R.drawable.empty) return true
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != R.drawable.empty) return true
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != R.drawable.empty) return true
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != R.drawable.empty) return true

    return false
}

fun isDraw(board: Array<Array<Int>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == R.drawable.empty) return false
        }
    }
    return true
}