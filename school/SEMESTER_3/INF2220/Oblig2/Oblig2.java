import java.util.Arrays;
import java.util.Scanner;

public class Oblig2{
	public static void main(String[] args)throws Exception{
		Scanner input = new Scanner(System.in);
		ProjectPlaner test = new ProjectPlaner();
		System.out.println("Wtrite txt file to read from: ");
		test.readFile(input.nextLine());
		if(test.isRealizable()){
			test.optimalSchedule();
			test.calculateSlack();
			System.out.println("\n***** Project execution simulation *****\n-----------------------------------");
			test.printSim();

			System.out.println("\n***** Printing info on each task *****\n--------------------------------------");
			test.printOptimal();			
		}
		else{
			System.out.println("Project cant be done!!");
		}

	}
}