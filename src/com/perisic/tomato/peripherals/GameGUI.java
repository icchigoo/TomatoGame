package com.perisic.tomato.peripherals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import com.perisic.tomato.engine.GameEngine;

public class GameGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = -107785653906635L;

    private JLabel questArea;
    private GameEngine myGame;
    private JLabel infoLabel;
    private JButton[] numberButtons;
    private Timer timer;
    private int timerSeconds;
    private int currentLevel;
    private int lives;
    private JButton restartButton; // New restart button

    public GameGUI() {
        super();
        initGame(null);
    }

    public GameGUI(String player) {
        super();
        initGame(player);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton sourceButton = (JButton) e.getSource();
    
            if (sourceButton == restartButton) {
                restartGame();
            } else {
                int solution;
                
                try {
                    solution = Integer.parseInt(e.getActionCommand());
                } catch (NumberFormatException ex) {
                    // Handle the case when the action command is not a number
                    System.out.println("Invalid input: " + e.getActionCommand());
                    return;
                }
    
                boolean correct = myGame.checkSolution(solution);
    
                if (correct) {
                    handleCorrectSolution();
                } else {
                    handleIncorrectSolution();
                }
            }
        } else if (e.getSource() == timer) {
            handleTimerTick();
        }
    }
    

    private void initGame(String player) {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tomato Game");
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        myGame = new GameEngine(player);

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.WHITE);
        infoLabel = new JLabel("What is the value of the tomato?   Score: 0");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(infoLabel);

        // Restart Button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        topPanel.add(restartButton);

        // Center Panel
        questArea = new JLabel();
        questArea.setHorizontalAlignment(JLabel.CENTER);

        // Add Panels to Frame
        add(topPanel, BorderLayout.NORTH);
        add(questArea, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(new Font("Arial", Font.PLAIN, 24));
            numberButtons[i].setBackground(new Color(52, 152, 219));
            numberButtons[i].setForeground(Color.WHITE);
            numberButtons[i].setFocusPainted(false);
            numberButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            bottomPanel.add(numberButtons[i]);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        // Additional features
        timerSeconds = 60;
        currentLevel = 1;
        lives = 4;

        // Timer setup
        timer = new Timer(1000, this);
        timer.start();

        // Initial game update
        updateGame();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void restartGame() {
        // Reset game state
        timer.stop();
        timerSeconds = 60;
        currentLevel = 1;
        lives = 4;
    
        // Enable the restart button
        restartButton.setEnabled(true);
    
        // Update UI
        updateGame();
        infoLabel.setText("<html><div style='text-align: center; color: #34495E; font-size: 18px;'>New Game Started! | Level: "
                + currentLevel + " | Lives: " + lives + " | Score: " + myGame.getScore() + "</div></html>");
    }
    

    private void handleCorrectSolution() {
        int score = myGame.getScore();
        updateGame();
        infoLabel.setText("<html><div style='text-align: center; color: #4CAF50; font-size: 18px;'>Good!<br>Score: "
                + score + " | Level: " + currentLevel + " | Lives: " + lives + " | Time Left: " + timerSeconds + " seconds</div></html>");
    }

    private void handleIncorrectSolution() {
        lives--;

        if (lives < 0) {
            lives = 0; // Ensure lives don't go negative
        }

        if (lives == 0) {
            handleGameOver();
        } else {
            updateGame();
            infoLabel.setText("<html><div style='text-align: center; color: #FF5252; font-size: 18px;'>Oops. Try again!<br>Score: "
                    + myGame.getScore() + " | Level: " + currentLevel + " | Lives: " + lives + " | Time Left: " + timerSeconds + " seconds</div></html>");
        }
    }

    private void handleTimerTick() {
        timerSeconds--;

        if (timerSeconds <= 0) {
            handleGameOver();
        } else {
            infoLabel.setText("<html><div style='text-align: center; color: #34495E; font-size: 18px;'>Time Left: "
                    + timerSeconds + " seconds | Level: " + currentLevel + " | Lives: " + lives + " | Score: " + myGame.getScore() + "</div></html>");
        }
    }

    private void handleGameOver() {
        timer.stop();
        infoLabel.setText("<html><div style='text-align: center; color: #E74C3C; font-size: 18px;'>Game Over! Final Score: "
                + myGame.getScore() + " | Level: " + currentLevel + " | Lives: " + lives + "</div></html>");
    
        // Disable the restart button when the game is over
        restartButton.setEnabled(false);
    
        // Additional actions for game over, e.g., prompt for restart or display a game over dialog
        int option = JOptionPane.showOptionDialog(
                this,
                "Game Over! Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Yes", "No"},
                "Yes");
    
        if (option == JOptionPane.YES_OPTION) {
            // User chose to play again
            restartGame();
        } else {
            // User chose not to play again
            System.exit(0); // Exit the application (you can adjust this based on your needs)
        }
    }
    
    private void updateGame() {
        // Additional features for level increment and life reset
        if (myGame.getScore() > currentLevel * 10) {
            currentLevel++;
            lives = 4;  // Reset lives for each new level
            timerSeconds += 10;  // Add extra time for each new level
        }

        BufferedImage currentGame = myGame.nextGame();
        ImageIcon ii = new ImageIcon(currentGame.getScaledInstance(500, 500, Image.SCALE_SMOOTH));

        // Add a null check for questArea
        if (questArea != null) {
            questArea.setIcon(ii);
        } else {
            System.out.println("questArea is null!");
        }

        // Reset timer for each new game
        timer.restart();
        timerSeconds = 60;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameGUI().setVisible(true);
        });
    }
}
