public class SearchDemo {
    public static int search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // Return the index of the target
            }
        }
        return -1; // Return -1 if the target is not found
    }

    public static void main(String[] args) {
        int[] data = {4, 8, 15, 16, 23, 42};
        int target = 15;
        System.out.println(search(data, target));
    }
}