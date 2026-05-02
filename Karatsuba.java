import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;


public class Karatsuba {


    static int totalOperation = 0;


    // Generate a random integer w the digits
    public static BigInteger generateRandomNumber(int digit) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(digit);
        for (int i = 0; i < digit; i++) {
            int randomDigit = i == 0 ? random.nextInt(9) + 1 : random.nextInt(10);
            sb.append(randomDigit);
        }
        return new BigInteger(sb.toString());
    }


    // no. of digits
    public static int numLength(BigInteger n) {
        int noLen = 0;
        while (n.compareTo(BigInteger.ZERO) > 0) {
            noLen++;
            n = n.divide(BigInteger.TEN);
        }
        return noLen;
    }


    // Karatsuba
    public static BigInteger mult(BigInteger x, BigInteger y) {


        int additionCounter       = 0;
        int subtractionCounter    = 0;
        int multiplicationCounter = 0;
        int divideCounter         = 0;
        int modCounter            = 0;
        int assignmentCounter     = 0;
        int comparisonCounter     = 0;
        int returnCounter         = 0;


        // Base case
        comparisonCounter += 2;
        if (x.compareTo(BigInteger.TEN) < 0 && y.compareTo(BigInteger.TEN) < 0) {
            multiplicationCounter += 1;
            returnCounter         += 1;
            totalOperation += additionCounter + subtractionCounter + multiplicationCounter
                            + divideCounter + modCounter + assignmentCounter
                            + comparisonCounter + returnCounter;
            return x.multiply(y);
        }


        // length of both numbers
        int noOneLength = numLength(x);   assignmentCounter += 1;
        int noTwoLength = numLength(y);   assignmentCounter += 1;


        // max length
        int maxNumLength = Math.max(noOneLength, noTwoLength);
        assignmentCounter += 1; comparisonCounter += 1;


        // Round up half of max length
        int halfMaxNumLength = (maxNumLength / 2) + (maxNumLength % 2);
        assignmentCounter += 1; additionCounter += 1;
        divideCounter += 1; modCounter += 1;


        // base = 10^half
        BigInteger base = BigInteger.TEN.pow(halfMaxNumLength);
        assignmentCounter     += 1;
        multiplicationCounter += halfMaxNumLength;


        // split x and y
        BigInteger a = x.divide(base);   divideCounter += 1; 
assignmentCounter += 1;
        BigInteger b = x.mod(base);      modCounter    += 1; 
assignmentCounter += 1;
        BigInteger c = y.divide(base);   divideCounter += 1; 
assignmentCounter += 1;
        BigInteger d = y.mod(base);      modCounter    += 1; 
assignmentCounter += 1;


        // 3 recursive calls
        BigInteger z0 = mult(a, c);               assignmentCounter += 1;
        BigInteger z1 = mult(a.add(b), c.add(d)); assignmentCounter += 1; additionCounter += 2;
        BigInteger z2 = mult(b, d);               assignmentCounter += 1;


        // combine all
        BigInteger ans = z0.multiply(BigInteger.TEN.pow(halfMaxNumLength * 2))
                          .add((z1.subtract(z0).subtract(z2))
                          .multiply(BigInteger.TEN.pow(halfMaxNumLength)))
                          .add(z2);
        assignmentCounter     += 1;
        additionCounter       += 2;
        subtractionCounter    += 2;
        multiplicationCounter += 2 + (halfMaxNumLength * 3);


        returnCounter += 1;
        totalOperation += additionCounter + subtractionCounter + multiplicationCounter
                        + divideCounter + modCounter + assignmentCounter
                        + comparisonCounter + returnCounter;
        return ans;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of digits (n): ");
        int n = sc.nextInt();


        BigInteger num1 = generateRandomNumber(n);
        BigInteger num2 = generateRandomNumber(n);


        String divider = "-".repeat(28);


        // output for n digits
        System.out.println();
        System.out.println("Karatsuba Multiplication  n = " + n);
        System.out.println(divider);
        System.out.println("Multiplicand : " + num1);
        System.out.println("Multiplier   : " + num2);
        System.out.println();
        totalOperation = 0;
        BigInteger result = mult(num1, num2);
        System.out.println("Result     : " + result);
        System.out.println("Operations : " + totalOperation);
        System.out.println(divider);


        System.out.println();
        // write into csv
       
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Karatsuba.csv"))) {
            writer.write("number of digits,total operations\n");


            for (int digit = 1; digit <= n; digit++) {
                BigInteger a = generateRandomNumber(digit);
                BigInteger b = generateRandomNumber(digit);
                totalOperation = 0;
                mult(a, b);
                System.out.println("  n=" + digit + "  ops=" + totalOperation + "  (" + a + " x " + b + ")");
                writer.write(digit + "," + totalOperation + "\n");
            }


        } catch (Exception e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }
}
