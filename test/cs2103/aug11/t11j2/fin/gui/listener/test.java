package cs2103.aug11.t11j2.fin.gui.listener;

import java.util.Scanner;



public class test {
	public static void main(String[] args) {
		 // Initialize JIntellitype
		System.out.println("HELLO");
		TestClass test = new TestClass();
		Scanner in = new Scanner(System.in);
		test.test();
		while (in.nextLine().equals("wait")) {
			
		}
		test.dispose();


	}
	


}
