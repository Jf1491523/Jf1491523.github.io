public class TableWhile {
    public static void main(String[] args) {
        int row = 1;
        while (row < 11) {
            int col = 1;
            while (col < 11) {
                System.out.print(row * col + "\t");
                col++;
            }
            System.out.println();
            row++;
        }
    }
}
