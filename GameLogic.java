import java.util.Random;

public class GameLogic {

    private static Random rand = new Random();

    public static String generateQuestion(String level) {
        int a, b;

        switch (level) {
            case "EASY":
                a = rand.nextInt(10) + 1;
                b = rand.nextInt(10) + 1;
                break;
            case "MEDIUM":
                a = rand.nextInt(50) + 1;
                b = rand.nextInt(50) + 1;
                break;
            case "HARD":
                a = rand.nextInt(100) + 1;
                b = rand.nextInt(100) + 1;
                break;
            default:
                a = 1; b = 1;
        }

        int op = rand.nextInt(4);

        switch (op) {
            case 0: return a + " + " + b;
            case 1: return a + " - " + b;
            case 2: return a + " * " + b;
            case 3: return (a * b) + " / " + a;
        }
        return "";
    }

    public static int getAnswer(String question) {
        String[] parts = question.split(" ");
        int a = Integer.parseInt(parts[0]);
        String op = parts[1];
        int b = Integer.parseInt(parts[2]);

        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
        }
        return 0;
    }
}