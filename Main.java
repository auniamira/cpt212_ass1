import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static int totalOperation = 0;

    // generate random numbers
    public static BigInteger generateRandomNumber(int digit) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(digit);
        for (int i = 0; i < digit; i++) {
            int randomDigit = i == 0 ? random.nextInt(9) + 1 : random.nextInt(10);
            sb.append(randomDigit);
        }
        return new BigInteger(sb.toString());
    }

    
    public static BigInteger multiply(BigInteger num1, BigInteger num2, int digit, boolean printSteps) {

        int additionCounter       = 0;
        int subtractionCounter    = 0;
        int multiplicationCounter = 0;
        int divideCounter         = 0;
        int modCounter            = 0;
        int assignmentCounter     = 0;
        int comparisonCounter     = 0;
        int returnCounter         = 0;

        BigInteger[] partialRows = new BigInteger[digit];
        BigInteger[] carrierRows = new BigInteger[digit];
        assignmentCounter += 2;

        for (int i = 0; i < digit; i++) {
            comparisonCounter += 1; additionCounter += 1; assignmentCounter += 1;
            partialRows[i] = BigInteger.ZERO;
            carrierRows[i] = BigInteger.ZERO;
            assignmentCounter += 2;
        }

        BigInteger result = BigInteger.ZERO;
        assignmentCounter += 1;

        BigInteger num2Temp = num2;
        assignmentCounter += 1;

        for (int i = 0; i < digit; i++) {
            comparisonCounter += 1; additionCounter += 1; assignmentCounter += 1;

            BigInteger d2 = num2Temp.mod(BigInteger.TEN);
            assignmentCounter += 1; modCounter += 1;

            int[] partialDigits = new int[digit];
            int[] carrierDigits = new int[digit];

            BigInteger num1Temp = num1;
            assignmentCounter += 1;

            for (int j = 0; j < digit; j++) {
                comparisonCounter += 1; additionCounter += 1; assignmentCounter += 1;

                BigInteger d1 = num1Temp.mod(BigInteger.TEN);
                assignmentCounter += 1; modCounter += 1;

                BigInteger product = d1.multiply(d2);
                assignmentCounter += 1; multiplicationCounter += 1;

                if (product.compareTo(BigInteger.TEN) < 0) {
                    comparisonCounter += 1;
                    partialDigits[digit - 1 - j] = product.intValue();
                    carrierDigits[digit - 1 - j] = 0;
                    assignmentCounter += 2;
                } else {
                    comparisonCounter += 1;
                    partialDigits[digit - 1 - j] = product.mod(BigInteger.TEN).intValue();
                    carrierDigits[digit - 1 - j] = product.divide(BigInteger.TEN).intValue();
                    assignmentCounter += 2; modCounter += 1; divideCounter += 1;
                }

                BigInteger posVal = BigInteger.TEN.pow(j);
                partialRows[i] = partialRows[i].add(BigInteger.valueOf(partialDigits[digit - 1 - j]).multiply(posVal));
                carrierRows[i] = carrierRows[i].add(BigInteger.valueOf(carrierDigits[digit - 1 - j]).multiply(posVal));
                assignmentCounter += 2; additionCounter += 2; multiplicationCounter += 2;

                num1Temp = num1Temp.divide(BigInteger.TEN);
                assignmentCounter += 1; divideCounter += 1;
            }

            if (printSteps) {
                // Build partial and carrier strings with leading zeros preserved
                StringBuilder pStr = new StringBuilder();
                StringBuilder cStr = new StringBuilder();
                for (int k = 0; k < digit; k++) {
                    pStr.append(partialDigits[k]);
                    cStr.append(carrierDigits[k]);
                }
                // Apply shift (multiply by 10^i) for display
                String shiftPad = "0".repeat(i);
                System.out.println(num1 + " x " + d2 + "  (shift " + i + ")");
                System.out.println("  partials : " + pStr + shiftPad);
                System.out.println("  carriers : " + cStr + shiftPad);
                System.out.println();
            }

            partialRows[i] = partialRows[i].multiply(BigInteger.TEN.pow(i));
            carrierRows[i] = carrierRows[i].multiply(BigInteger.TEN.pow(i + 1));
            assignmentCounter += 2; multiplicationCounter += (2 * i) + 2;

            num2Temp = num2Temp.divide(BigInteger.TEN);
            assignmentCounter += 1; divideCounter += 1;
        }

        for (int i = 0; i < digit; i++) {
            comparisonCounter += 1; additionCounter += 1; assignmentCounter += 1;
            result = result.add(partialRows[i]).add(carrierRows[i]);
            assignmentCounter += 1; additionCounter += 2;
        }

        totalOperation = additionCounter + subtractionCounter + multiplicationCounter
                       + divideCounter + modCounter + assignmentCounter
                       + comparisonCounter + returnCounter;

        if (printSteps) {
            System.out.println("Final result : " + result);
            System.out.println("Operations   : " + totalOperation);
        }

        return result;
    }

    //main fx
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of digits (n): ");
        int n = sc.nextInt();

        BigInteger num1 = generateRandomNumber(n);
        BigInteger num2 = generateRandomNumber(n);

        String divider = "-".repeat(28);

        System.out.println();
        System.out.println("Simple Multiplication  n = " + n);
        System.out.println(divider);
        System.out.println("Multiplicand : " + num1);
        System.out.println("Multiplier   : " + num2);
        System.out.println();

        totalOperation = 0;
        multiply(num1, num2, n, true);

        System.out.println(divider);

        System.out.println();
        System.out.println("=== n = 1 to " + n + " ===");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Multiplication.csv"))) {
            writer.write("number of digits,total operations\n");

            for (int digit = 1; digit <= n; digit++) {
                BigInteger a = generateRandomNumber(digit);
                BigInteger b = generateRandomNumber(digit);
                totalOperation = 0;
                multiply(a, b, digit, false);
                System.out.println("  n=" + digit + "  ops=" + totalOperation + "  (" + a + " x " + b + ")");
                writer.write(digit + "," + totalOperation + "\n");
            }

        } catch (Exception e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }
}
