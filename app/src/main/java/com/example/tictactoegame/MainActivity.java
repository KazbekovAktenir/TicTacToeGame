package com.example.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3]; // Массив кнопок 3x3
    private boolean player1Turn = true; // Кто сейчас ходит (true = игрок 1, false = игрок 2)
    private int roundCount; // Количество раундов (для подсчета ходов)
    private int player1Points; // Очки игрока 1
    private int player2Points; // Очки игрока 2
    private TextView textViewPlayer1; // Поле для отображения очков игрока 1
    private TextView textViewPlayer2; // Поле для отображения очков игрока 2
    private TextView playerTurnTextView; // Поле для отображения текущего игрока

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация TextView для отображения текущего игрока
        playerTurnTextView = findViewById(R.id.playerTurn);

        // Инициализация текстовых полей для отображения очков
        textViewPlayer1 = findViewById(R.id.player1Score);
        textViewPlayer2 = findViewById(R.id.player2Score);

        // Цикл для связывания кнопок с их ID
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + (i * 3 + j + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Если кнопка уже нажата, ничего не делаем
                        if (!((Button) v).getText().toString().equals("")) {
                            return;
                        }

                        // Определяем, чей ход и выставляем "X" или "O"
                        if (player1Turn) {
                            ((Button) v).setText("X");
                            playerTurnTextView.setText("Ход Игрока 2"); // Изменяем индикатор текущего игрока
                        } else {
                            ((Button) v).setText("O");
                            playerTurnTextView.setText("Ход Игрока 1"); // Изменяем индикатор текущего игрока
                        }

                        roundCount++;

                        // Проверка победителя
                        if (checkForWin()) {
                            if (player1Turn) {
                                player1Wins();
                            } else {
                                player2Wins();
                            }
                        } else if (roundCount == 9) {
                            // Если заполнены все клетки, и нет победителя
                            draw();
                        } else {
                            // Меняем ход
                            player1Turn = !player1Turn;
                        }
                    }
                });
            }
        }

        // Сброс игры (включите кнопку для сброса в activity_main.xml)
        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    // Проверка на победителя
    private boolean checkForWin() {
        String[][] field = new String[3][3];

        // Присваиваем значения с кнопок в поле field
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Проверяем строки, колонки и диагонали
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    // Логика победы игрока 1
    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Игрок 1 выиграл!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Логика победы игрока 2
    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Игрок 2 выиграл!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Логика для ничьей
    private void draw() {
        Toast.makeText(this, "Ничья!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    // Обновление текста с очками
    private void updatePointsText() {
        textViewPlayer1.setText("Игрок 1: " + player1Points);
        textViewPlayer2.setText("Игрок 2: " + player2Points);
    }

    // Сброс игрового поля (без сброса очков)
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
        playerTurnTextView.setText("Ход Игрока 1"); // Сбрасываем индикатор текущего игрока
    }

    // Полный сброс игры (сброс и очков)
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }
}
