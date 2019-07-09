/*
	Program to determine whether integer given by user is 
	a prime number or not.
*/

import java.util.*;
import java.util.Scanner;

public class isPrime {

    public static void main(String[] args) {

    	boolean flag = false;
        int num;
        
    	System.out.print("Enter an integer: ");

    	Scanner input = new Scanner(System.in);
        num = input.nextInt();


        // negative integers are never prime
        while (num < 0) {
            System.out.print("Please enter a nonnegative integer: ");
            input = new Scanner(System.in);
            num = input.nextInt();
        }
        
        // consider the fact that 0 and 1 are not prime
        if (num == 0 || num == 1) {
            flag = true; 
        } else {
            for (int i = 2; i <= num/2; ++i) {
                // condition for nonprime number
                if (num % i == 0) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag)
            System.out.println(num + " is a prime number.");
        else
            System.out.println(num + " is not a prime number.");
    }
}