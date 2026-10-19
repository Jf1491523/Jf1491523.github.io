import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IdiomLifeDictionary extends JFrame {
    private static final Color BACKGROUND = new Color(24, 29, 49);
    private static final Color PANEL = new Color(37, 44, 68);
    private static final Color TEXT = new Color(241, 243, 255);
    private static final Color MUTED = new Color(166, 175, 202);
    private static final Color GREEN = new Color(105, 220, 164);
    private static final Color RED = new Color(255, 112, 131);

    private final List<Idiom> idioms = Arrays.asList(
        new Idiom("Break the ice", "ice", 0,
            "To make people feel more relaxed in a new or awkward situation.",
            "To end a friendship without warning.",
            "To solve a problem by working faster.",
            "To feel cold even when it is warm."),
        new Idiom("Spill the beans", "beans", 2,
            "To make a mess while cooking dinner.",
            "To waste something that could be saved.",
            "To reveal a secret, often by accident.",
            "To share food with your friends."),
        new Idiom("Raining cats and dogs", "rain", 3,
            "A day when everyone stays home with their pets.",
            "A funny way to describe a noisy neighborhood.",
            "A storm that brings unusually large hail.",
            "To rain heavily."),
        new Idiom("Hit the books", "books", 3,
            "To throw away old books you don't need.",
            "To visit a library to meet a friend.",
            "To get frustrated with a difficult assignment.",
            "To study hard.")
    );

    private final JLabel titleLabel = new JLabel();
    private final JLabel progressLabel = new JLabel();
    private final JLabel livesLabel = new JLabel();
    private final JLabel feedbackLabel = new JLabel("Watch the scene, then choose what the idiom really means.");
    private final JButton[] answerButtons = new JButton[4];
    private final ScenePanel scenePanel = new ScenePanel();
    private final JPanel footer = new JPanel(new CardLayout());
    private JPanel retryPanel;
    private final JPanel content = new JPanel(new BorderLayout(18, 18));
    private final List<Idiom> questionOrder = new ArrayList<Idiom>();
    private int lives = 3;
    private int questionNumber;
    private int orderIndex;
    private boolean gameOver;

    public IdiomLifeDictionary() {
        super("Idiom Life Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 680));
        setSize(900, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);

        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(24, 30, 24, 30));
        setContentPane(content);
        buildHeader();
        buildGameArea();
        buildAnswers();
        restartGame();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 8));
        header.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.add(titleLabel);

        JPanel status = new JPanel();
        status.setOpaque(false);
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        progressLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        progressLabel.setForeground(MUTED);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        livesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        livesLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        status.add(progressLabel);
        status.add(Box.createVerticalStrut(8));
        status.add(livesLabel);

        header.add(heading, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);
        content.add(header, BorderLayout.NORTH);
    }

    private void buildGameArea() {
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);

        JPanel sceneCard = new JPanel(new BorderLayout());
        sceneCard.setBackground(PANEL);
        sceneCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(58, 67, 96), 1),
            new EmptyBorder(10, 10, 10, 10)));
        scenePanel.setPreferredSize(new Dimension(640, 330));
        sceneCard.add(scenePanel, BorderLayout.CENTER);

        feedbackLabel.setForeground(MUTED);
        feedbackLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        feedbackLabel.setBorder(new EmptyBorder(0, 4, 0, 4));
        center.add(sceneCard, BorderLayout.CENTER);
        center.add(feedbackLabel, BorderLayout.SOUTH);
        content.add(center, BorderLayout.CENTER);
    }

    private void buildAnswers() {
        JPanel answers = new JPanel(new GridLayout(2, 2, 12, 12));
        answers.setOpaque(false);
        for (int i = 0; i < answerButtons.length; i++) {
            final int answerIndex = i;
            JButton button = new JButton();
            button.setForeground(TEXT);
            button.setBackground(PANEL);
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 75, 107), 1),
                new EmptyBorder(13, 15, 13, 15)));
            button.addActionListener(event -> submitAnswer(answerIndex));
            answerButtons[i] = button;
            answers.add(button);
        }
        footer.setOpaque(false);
        footer.add(answers, "answers");
        content.add(footer, BorderLayout.SOUTH);
    }

    private void restartGame() {
        lives = 3;
        questionNumber = 0;
        orderIndex = 0;
        gameOver = false;
        questionOrder.clear();
        questionOrder.addAll(idioms);
        Collections.shuffle(questionOrder, new Random());
        feedbackLabel.setForeground(MUTED);
        feedbackLabel.setText("Watch the scene, then choose what the idiom really means.");
        ((CardLayout) footer.getLayout()).show(footer, "answers");
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (orderIndex == questionOrder.size()) {
            Collections.shuffle(questionOrder, new Random());
            orderIndex = 0;
        }
        Idiom idiom = questionOrder.get(orderIndex++);
        questionNumber++;
        titleLabel.setText("“" + idiom.phrase + "”");
        progressLabel.setText("QUESTION " + questionNumber);
        livesLabel.setText(livesText());
        scenePanel.setScene(idiom.scene);
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText((char) ('A' + i) + "   " + idiom.choices[i]);
            answerButtons[i].setEnabled(true);
            answerButtons[i].setVisible(true);
            answerButtons[i].setBackground(PANEL);
        }
        scenePanel.currentIdiom = idiom;
    }

    private String livesText() {
        StringBuilder hearts = new StringBuilder("LIVES  ");
        for (int i = 0; i < 3; i++) {
            hearts.append(i < lives ? "♥" : "♡");
            if (i < 2) {
                hearts.append("  ");
            }
        }
        return hearts.toString();
    }

    private void submitAnswer(int selected) {
        if (gameOver) {
            return;
        }
        Idiom idiom = scenePanel.currentIdiom;
        if (selected == idiom.correctIndex) {
            feedbackLabel.setForeground(GREEN);
            feedbackLabel.setText("That's it! " + idiom.meaning);
            Timer nextQuestion = new Timer(1500, event -> showNextQuestion());
            nextQuestion.setRepeats(false);
            nextQuestion.start();
        } else {
            lives--;
            livesLabel.setText(livesText());
            if (lives == 0) {
                showGameOver();
            } else {
                feedbackLabel.setForeground(RED);
                feedbackLabel.setText("Not quite — " + idiom.meaning + "  (" + lives + " "
                    + (lives == 1 ? "life" : "lives") + " left)");
                Timer nextQuestion = new Timer(2100, event -> showNextQuestion());
                nextQuestion.setRepeats(false);
                nextQuestion.start();
            }
        }
        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }
    }

    private void showGameOver() {
        gameOver = true;
        titleLabel.setText("Game over!");
        progressLabel.setText("YOU MADE IT TO QUESTION " + questionNumber);
        feedbackLabel.setForeground(RED);
        feedbackLabel.setText("Out of lives! Give it another try and see how far you get.");
        scenePanel.setScene("gameover");
        for (JButton button : answerButtons) {
            button.setVisible(false);
        }
        JButton retryButton = new JButton("TRY AGAIN");
        retryButton.setForeground(BACKGROUND);
        retryButton.setBackground(GREEN);
        retryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        retryButton.setFocusPainted(false);
        retryButton.setBorder(new EmptyBorder(15, 24, 15, 24));
        retryButton.addActionListener(event -> {
            restartGame();
        });
        retryPanel = new JPanel();
        retryPanel.setOpaque(false);
        retryPanel.add(retryButton);
        footer.add(retryPanel, "retry");
        ((CardLayout) footer.getLayout()).show(footer, "retry");
        for (Component component : footer.getComponents()) {
            if (component != retryPanel) {
                component.setVisible(false);
            }
        }
        retryPanel.setVisible(true);
        footer.revalidate();
        footer.repaint();
        content.revalidate();
        content.repaint();
    }

    private static class Idiom {
        private final String phrase;
        private final String scene;
        private final int correctIndex;
        private final String meaning;
        private final String[] choices;

        private Idiom(String phrase, String scene, int correctIndex, String... choices) {
            this.phrase = phrase;
            this.scene = scene;
            this.correctIndex = correctIndex;
            this.meaning = choices[correctIndex];
            this.choices = choices;
        }
    }

    private static class ScenePanel extends JPanel {
        private String scene = "";
        private Idiom currentIdiom;
        private double animationTime;

        private ScenePanel() {
            setOpaque(false);
            new Timer(40, event -> {
                animationTime += 0.06;
                repaint();
            }).start();
        }

        private void setScene(String scene) {
            this.scene = scene;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(29, 36, 59));
            g.fill(new RoundRectangle2D.Double(0, 0, width, height, 20, 20));

            if ("ice".equals(scene)) {
                drawIce(g, width, height);
            } else if ("beans".equals(scene)) {
                drawBeans(g, width, height);
            } else if ("rain".equals(scene)) {
                drawRain(g, width, height);
            } else if ("books".equals(scene)) {
                drawBooks(g, width, height);
            } else {
                drawGameOver(g, width, height);
            }
            g.dispose();
        }

        private void drawIce(Graphics2D g, int width, int height) {
            g.setColor(new Color(78, 144, 187));
            g.fillRoundRect(100, height - 93, width - 200, 31, 16, 16);
            g.setColor(new Color(139, 225, 247));
            g.fillRoundRect(111, height - 89, width - 222, 9, 8, 8);
            drawPerson(g, width / 2 - 130, height - 156, new Color(255, 190, 132), new Color(119, 128, 255));
            drawPerson(g, width / 2 + 65, height - 156, new Color(247, 177, 159), new Color(255, 136, 153));
            double crack = Math.sin(animationTime * 2.4) * 10;
            g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(225, 249, 255));
            int cx = width / 2;
            int cy = height - 78;
            int[] xs = {cx, cx - 18, cx - 7, cx - 27, cx + 3, cx + 13, cx + 28};
            int[] ys = {cy - 20, cy - 7, cy + 3, cy + 14, cy + 7, cy + 18, cy + 27};
            for (int i = 0; i < xs.length - 1; i++) {
                g.drawLine(xs[i], (int) (ys[i] + crack * i / 7), xs[i + 1], (int) (ys[i + 1] + crack * (i + 1) / 7));
            }
        }

        private void drawBeans(Graphics2D g, int width, int height) {
            g.setColor(new Color(199, 138, 99));
            g.fillRoundRect(width / 2 - 58, height - 120, 116, 56, 12, 12);
            g.setColor(new Color(245, 187, 129));
            g.fillOval(width / 2 - 58, height - 132, 116, 28);
            g.setColor(new Color(111, 74, 61));
            g.fillOval(width / 2 - 49, height - 125, 98, 17);
            for (int i = 0; i < 12; i++) {
                double x = width / 2.0 + Math.sin(animationTime * 2 + i * 2.1) * (18 + i * 8);
                double y = 75 + ((animationTime * 65 + i * 39) % (height - 205));
                g.setColor(i % 2 == 0 ? new Color(111, 190, 110) : new Color(160, 207, 115));
                g.fill(new Ellipse2D.Double(x, y, 18, 11));
                g.setColor(new Color(75, 135, 84));
                g.drawLine((int) x + 5, (int) y + 6, (int) x + 13, (int) y + 4);
            }
        }

        private void drawRain(Graphics2D g, int width, int height) {
            g.setColor(new Color(98, 111, 147));
            g.fillOval(width / 2 - 100, 56, 150, 76);
            g.fillOval(width / 2 - 34, 35, 171, 103);
            g.fillOval(width / 2 + 42, 59, 121, 71);
            for (int i = 0; i < 15; i++) {
                int x = 40 + (int) ((i * 73 + animationTime * 52) % Math.max(1, width - 80));
                int y = 155 + (i * 31 + (int) (animationTime * 85)) % Math.max(1, height - 220);
                g.setColor(new Color(110, 193, 246));
                g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(x, y, x - 8, y + 20);
                if (i % 4 == 0) {
                    drawCat(g, x + 18, y + 3, 0.48);
                } else if (i % 4 == 1) {
                    drawDog(g, x + 18, y + 3, 0.48);
                }
            }
        }

        private void drawBooks(Graphics2D g, int width, int height) {
            double bob = Math.sin(animationTime * 3) * 5;
            int x = width / 2 - 75;
            int y = (int) (height - 132 + bob);
            g.setColor(new Color(239, 178, 92));
            g.fillRoundRect(x, y, 59, 73, 6, 6);
            g.setColor(new Color(126, 104, 232));
            g.fillRoundRect(x + 49, y - 18, 65, 91, 6, 6);
            g.setColor(new Color(90, 197, 157));
            g.fillRoundRect(x + 103, y + 7, 55, 66, 6, 6);
            g.setColor(new Color(255, 240, 211));
            g.fillRect(x + 12, y + 12, 3, 49);
            g.fillRect(x + 61, y - 3, 3, 62);
            g.fillRect(x + 115, y + 18, 3, 43);
            g.setColor(new Color(255, 215, 119));
            g.setFont(new Font("SansSerif", Font.BOLD, 27));
            g.drawString("★", x + 55, y - 32);
        }

        private void drawGameOver(Graphics2D g, int width, int height) {
            g.setColor(new Color(255, 112, 131));
            g.setFont(new Font("SansSerif", Font.BOLD, 44));
            String text = "NO LIVES LEFT";
            FontMetrics metrics = g.getFontMetrics();
            g.drawString(text, (width - metrics.stringWidth(text)) / 2, height / 2);
            g.setColor(MUTED);
            g.setFont(new Font("SansSerif", Font.PLAIN, 17));
            String smaller = "Every mistake is a chance to learn.";
            metrics = g.getFontMetrics();
            g.drawString(smaller, (width - metrics.stringWidth(smaller)) / 2, height / 2 + 36);
        }

        private void drawPerson(Graphics2D g, int x, int y, Color skin, Color shirt) {
            g.setColor(skin);
            g.fillOval(x + 22, y, 43, 43);
            g.setColor(shirt);
            g.fillRoundRect(x + 12, y + 42, 63, 68, 18, 18);
            g.setColor(new Color(39, 43, 62));
            g.fillOval(x + 34, y + 16, 5, 5);
            g.fillOval(x + 50, y + 16, 5, 5);
            g.drawArc(x + 35, y + 24, 20, 12, 195, 150);
        }

        private void drawCat(Graphics2D g, int x, int y, double scale) {
            int size = (int) (35 * scale);
            g.setColor(new Color(242, 173, 119));
            g.fillOval(x, y, size, (int) (25 * scale));
            Polygon ears = new Polygon(
                new int[] {x + size / 5, x + size / 3, x + size / 4, x + size * 4 / 5, x + size * 3 / 4, x + size * 4 / 5},
                new int[] {y + 6, y - 5, y + 11, y + 11, y - 5, y + 6}, 6);
            g.fillPolygon(ears);
        }

        private void drawDog(Graphics2D g, int x, int y, double scale) {
            int size = (int) (37 * scale);
            g.setColor(new Color(201, 154, 115));
            g.fillOval(x, y, size, (int) (25 * scale));
            g.fillOval(x - 4, y + 2, (int) (11 * scale), (int) (19 * scale));
            g.fillOval(x + size - 5, y + 2, (int) (11 * scale), (int) (19 * scale));
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IdiomLifeDictionary().setVisible(true));
    }
}
