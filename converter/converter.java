import java.awt.*;
import javax.swing.*;
import java.util.Scanner;

public class converter{
	static String[] hexSymbols = {"0" ,"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    public static void main(String[] args) {
/*        int number;

        Scanner in = new Scanner(System.in);

        System.out.println("Enter a positive integer");
        number = in.nextInt();

        if (number < 0) {
            System.out.println("Error: Not a positive integer");
        } else {

            System.out.print("Convert to binary is:");
            String res = decToBin(number);
            System.out.println(res);

            System.out.print("Convert to hex is:");
            String res2 = decToHex(number);
            System.out.println(res2);

            System.out.print("Convert to oct is:");
            String res3 = decToOct(number);
            System.out.println(res3);
        }*/

        Disp hax = new Disp();
        hax.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hax.setSize(400, 300);
        hax.setResizable(false);
        hax.setVisible(true);
    }


    public static String decToBin(int number) {
        int remainder = 0;
        String res = "";
        while(number > 0) {
	        remainder = number % 2;
	        res = String.valueOf(remainder) + res;      
	        number = number >> 1;
        }
        return res;
    }

    public static String decToHex(int number) {
        int remainder = 0;
        String res = "";
        while(number > 0) {
	        remainder = number % 16;
	        res = hexSymbols[remainder] + res;      
	        number = number >> 4;
        }
        return res;
    }

    public static String decToOct(int number) {
        int remainder = 0;
        String res = "";
        while(number > 0) {
	        remainder = number % 8;
	        res = hexSymbols[remainder] + res;      
	        number = number >> 3;
        }
        return res;
    }

    private static String binToDec(int number) {
    	String val = String.valueOf(number);
		int decimalValue = Integer.parseInt(val, 2);
		return String.valueOf(decimalValue);
    }

    private static String hexToDec(int number) {
    	String val = String.valueOf(number);
		int decimalValue = Integer.parseInt(val, 16);
		return String.valueOf(decimalValue);
    }

    private static String octToDec(int number) {
    	String val = String.valueOf(number);
		int decimalValue = Integer.parseInt(val, 8);
		return String.valueOf(decimalValue);
    }
}