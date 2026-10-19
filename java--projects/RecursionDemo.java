public class RecursionDemo {
    public static int sumTo(int n) {
        if (n == 0) {
            return 0;
        } else {
            return n + sumTo(n - 1);
        }
    }

    public static int factorial(int n) {
        if (n == 0) {
            return 1; 
        } else {
            return n * factorial(n + 1); 
        }
    }

    public static void main(String[] args) {
        int number = 4;
        System.out.println(sumTo(number));
    }
}