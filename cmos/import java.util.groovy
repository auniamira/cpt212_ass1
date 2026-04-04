import java.util.Random;

public class SimpleMultiplication {

    static long counter = 0; // operation counter

    public static void main(String[] args) {

        // ===== SMALL INPUT (FOR PRINTING) =====
        String str1 = "52301";
        String str2 = "380";

        System.out.println("=== DEMONSTRATION (SMALL INPUT) ===");
        multiply(str1, str2, true);

        // ===== EXPERIMENT (LARGE INPUT) =====
        System.out.println("\n=== EXPERIMENT ===");

        int[] testSizes = {5, 10, 50, 100, 200};

        for (int n : testSizes) {
            String num1 = generateRandomNumber(n);
            String num2 = generateRandomNumber(n);

            counter = 0;
            multiply(num1, num2, false);

            System.out.println("n = " + n + " → Operations = " + counter);
        }
    }

    // ===== CORE MULTIPLICATION FUNCTION =====
    public static String multiply(String str1, String str2, boolean printSteps) {

        int n1 = str1.length();
        int n2 = str2.length();

        // result array (max length = n1 + n2)
        int[] result = new int[n1 + n2];

        // loop through digits (RIGHT → LEFT)
        for (int i = n2 - 1; i >= 0; i--) {

            int d2 = str2.charAt(i) - '0';
            int carry = 0;

            int[] partial = new int[n1];   // store partial product
            int[] carriers = new int[n1];  // store carriers

            for (int j = n1 - 1; j >= 0; j--) {

                int d1 = str1.charAt(j) - '0';

                int product = d1 * d2 + carry;
                counter += 2; // multiplication + addition

                int digit = product % 10;
                carry = product / 10;
                counter += 2; // modulo + division

                partial[j] = digit;
                carriers[j] = carry;

                // add to final result (with shifting)
                result[i + j + 1] += digit;
                counter++; // addition
            }

            // handle remaining carry
            result[i] += carry;
            counter++;

            // ===== PRINT PARTIAL + CARRIER (ONLY SMALL INPUT) =====
            if (printSteps) {
                System.out.print("Partial products for (" + str1 + " x " + d2 + "): ");
                for (int k = 0; k < n1; k++) {
                    System.out.print(partial[k] + " ");
                }
                System.out.println();

                System.out.print("Carriers: ");
                for (int k = 0; k < n1; k++) {
                    System.out.print(carriers[k] + " ");
                }
                System.out.println("\n");
            }
        }

        // ===== FINAL ADDITION (HANDLE CARRY) =====
        for (int i = result.length - 1; i > 0; i--) {
            if (result[i] >= 10) {
                result[i - 1] += result[i] / 10;
                result[i] %= 10;
                counter += 2;
            }
        }

        // convert result array to string
        StringBuilder sb = new StringBuilder();
        for (int num : result) {
            if (!(sb.length() == 0 && num == 0)) {
                sb.append(num);
            }
        }

        String finalResult = sb.length() == 0 ? "0" : sb.toString();

        if (printSteps) {
            System.out.println("Final Result: " + finalResult);
        }

        return finalResult;
    }

    // ===== RANDOM NUMBER GENERATOR =====
    public static String generateRandomNumber(int n) {
        Random rand = new Random();

        StringBuilder sb = new StringBuilder();

        // first digit cannot be 0
        sb.append(rand.nextInt(9) + 1);

        for (int i = 1; i < n; i++) {
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }
}