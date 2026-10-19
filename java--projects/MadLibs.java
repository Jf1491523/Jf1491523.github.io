import java.util.Scanner;

public class MadLibs {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a whole number: ");
        int number = input.nextInt();
        input.nextLine(); // consume the newline character

        System.out.println("Enter an adjective: ");
        String adjective = input.nextLine();

        System.out.println("Enter a place: ");
        String place = input.nextLine();

        System.out.printf("%d went to %s and had a %s time!", number, place, adjective);
        input.close();
    }
}