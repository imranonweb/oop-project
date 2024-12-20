import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OnlineQuizApplication {
    public static void main(String[] args) {
        new Homepage();
    }
}

// Homepage class
class Homepage extends JFrame {
    public Homepage() {
        setTitle("Online Quiz Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Utility.centerWindow(this);

        // Homepage Colors
        Color backgroundColor = new Color(33, 150, 243); // Blue
        Color buttonColor = new Color(76, 175, 80);      // Green
        Color textColor = Color.WHITE;

        JLabel titleLabel = new JLabel("Welcome to IT Quiz");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(50, 50, 400, 50);
        titleLabel.setForeground(textColor);
        add(titleLabel);

        JButton startButton = createCustomButton("Let's Go", buttonColor, textColor);
        startButton.setBounds(200, 150, 120, 50);
        startButton.addActionListener(e -> {
            dispose();
            new QuizPage();
        });
        add(startButton);

        JButton exitButton = createCustomButton("Exit", new Color(60, 60, 60), textColor); // Dark gray
        exitButton.setBounds(200, 230, 120, 50);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        getContentPane().setBackground(backgroundColor);
        setVisible(true);
    }

    private JButton createCustomButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(fgColor);
                button.setForeground(bgColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setForeground(fgColor);
            }
        });

        return button;
    }
}

// Quiz Page class
class QuizPage extends JFrame {
    private int currentQuestion = 0;
    private int score = 0;
    private int timeLeft = 15; // Timer for each question
    private Timer timer;

    private String[] questions = {
            "What does JVM stand for?",
            "Which is not a programming language?",
            "What is the extension of Java files?",
            "What is the square root of 144?",
            "What is the value of pi approximately?",
            "If x + 2 = 7, what is the value of x?",
            "What is the capital of Bangladesh?",
            "Who is our OOP Lab Instructor?",
            "What is the national fruit of Bangladesh?",
            "What is 1 + 1 ?"
    };
    private String[][] options = {
            {"Java Virtual Machine", "Java Vendor Machine", "Java Variable Machine", "None of these"},
            {"Python", "Java", "HTML", "C++"},
            {".java", ".py", ".html", ".js"},
            {"12", "10", "11", "13"},
            {"3.14", "3.5", "2.71", "1.62"},
            {"5", "4", "3", "6"},
            {"Dhaka", "Chattogram", "Khulna", "Sylhet"},
            {"Md Riad Hassan", "Nazmus Sakib", "Iron Man", "Spider Man"},
            {"Jackfruit", "Mango", "Banana", "Litchi"},
            {"2", "10", "11", "0"}
    };
    private int[] correctAnswers = {0, 2, 0, 0, 0, 0, 0, 0, 0, 1};

    private JRadioButton[] radioButtons = new JRadioButton[4];
    private ButtonGroup group = new ButtonGroup();
    private JLabel timerLabel = new JLabel("Time Left: " + timeLeft + "s");

    public QuizPage() {
        setTitle("Quiz");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Utility.centerWindow(this);

        // Quiz Page Colors
        Color backgroundColor = new Color(60, 87, 34); // Deep Orange
        Color textColor = Color.WHITE;

        JLabel questionLabel = new JLabel();
        questionLabel.setBounds(50, 50, 500, 30);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(textColor);
        add(questionLabel);

        int y = 100;
        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setBounds(50, y, 500, 30);
            radioButtons[i].setBackground(backgroundColor);
            radioButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            radioButtons[i].setForeground(textColor);
            group.add(radioButtons[i]);
            add(radioButtons[i]);
            y += 40;
        }

        timerLabel.setBounds(400, 10, 150, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.YELLOW);
        add(timerLabel);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(250, 350, 120, 50);
        nextButton.setBackground(new Color(76, 175, 80));
        nextButton.setForeground(textColor);
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.addActionListener(e -> handleNext(questionLabel));
        add(nextButton);

        updateQuestion(questionLabel);
        startTimer(questionLabel);

        getContentPane().setBackground(backgroundColor);
        setVisible(true);
    }

    private void updateQuestion(JLabel questionLabel) {
        questionLabel.setText((currentQuestion + 1) + ". " + questions[currentQuestion]);
        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(options[currentQuestion][i]);
        }
        group.clearSelection();
        timeLeft = 15;
        timerLabel.setText("Time Left: " + timeLeft + "s");
    }

    private void handleNext(JLabel questionLabel) {
        timer.stop();
        if (radioButtons[correctAnswers[currentQuestion]].isSelected()) {
            score++;
        }
        currentQuestion++;
        if (currentQuestion < questions.length) {
            updateQuestion(questionLabel);
            startTimer(questionLabel);
        } else {
            dispose();
            new ResultPage(score, questions.length);
        }
    }

    private void startTimer(JLabel questionLabel) {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                timer.stop();
                handleNext(questionLabel);
            }
        });
        timer.start();
    }
}

// Result and Leaderboard class
class ResultPage extends JFrame {
    private static ArrayList<String> leaderboard = new ArrayList<>();

    public ResultPage(int score, int totalQuestions) {
        setTitle("Result");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Utility.centerWindow(this);

        // Result Page Colors
        Color backgroundColor = new Color(76, 175, 80); // Green
        Color textColor = Color.WHITE;

        JLabel resultLabel = new JLabel("Your Score: " + score + "/" + totalQuestions);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setBounds(150, 50, 300, 30);
        add(resultLabel);

        leaderboard.add("User" + (leaderboard.size() + 1) + " - " + score);

        JLabel leaderboardLabel = new JLabel("Leaderboard:");
        leaderboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardLabel.setBounds(50, 100, 200, 30);
        add(leaderboardLabel);

        StringBuilder lbContent = new StringBuilder("<html>");
        for (int i = 0; i < leaderboard.size(); i++) {
            lbContent.append((i + 1)).append(". ").append(leaderboard.get(i)).append("<br>");
        }
        lbContent.append("</html>");

        JLabel leaderboardContent = new JLabel(lbContent.toString());
        leaderboardContent.setBounds(50, 140, 400, 150);
        leaderboardContent.setFont(new Font("Arial", Font.PLAIN, 16));
        add(leaderboardContent);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(100, 300, 120, 50);
        homeButton.setBackground(new Color(33, 150, 243));
        homeButton.setForeground(textColor);
        homeButton.setFont(new Font("Arial", Font.BOLD, 18));
        homeButton.addActionListener(e -> {
            dispose();
            new Homepage();
        });
        add(homeButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 300, 120, 50);
        exitButton.setBackground(new Color(60, 60, 60)); // Dark gray
        exitButton.setForeground(textColor);
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        getContentPane().setBackground(backgroundColor);
        setVisible(true);
    }
}

// Utility class for centering windows
class Utility {
    public static void centerWindow(Window frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }
}
