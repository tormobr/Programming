import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.util.Scanner;
public class ProjectPlaner{
	
	Task[] tasks;
	Task first;

	//-----------------------------------------TASK CLASS----------------------------------------------

	class Task{
		int currentTime = 0;
		int status = 0;
		boolean done = false;
		int slack = 0;
		int id, staff, time, cntPredecessors;
		int earliestStart, earliestFinnish, latestStart, latestFinnish;
		String name;
		ArrayList<Task> outEdges = new ArrayList<>();
		int[] tasksBefore;

		Task(int id, String name, int time, int staff, int[] tasksBefore){
			this.id = id;
			this.name = name;
			this.time = time;
			this.staff = staff;
			//this.outEdges = outEdges;
			this.tasksBefore = tasksBefore;
			cntPredecessors = tasksBefore.length;
		}
		private void printTask(){
			System.out.println("Name:  " + name + "\tID:  " + id);
			System.out.println("\tTime needed: " + time);
			System.out.println("\tmanpower needed: " + staff);
			System.out.println("\tEarliestStart: " + earliestStart);
			System.out.println("\tlatestStart: " + latestStart);
			System.out.println("\tearliestFinnish: " + earliestFinnish);
			System.out.println("\tlatestFinnish: " + latestFinnish);
			System.out.println("\tSlack: " + slack);
			System.out.println("Tasks depending on this one:");
			for(int i = 0; i < outEdges.size(); i++){
				System.out.println(outEdges.get(i).id);
			}
			System.out.println("-------------\n");
		}

	}

	//-----------------------------------REALIZIBILITY-----------------------------------------------

	//checks if the graph contains loops
	boolean loopFound = false;
	boolean loopExist = false;
	Task loopHit;
	ArrayList<Task> loopTasks = new ArrayList<>();
	private void isRealizable(Task t){

		//if node is hit, when not finnished
		if(t.status == 1){
			System.out.println("\n\nLoop found:\n---------------");
			loopFound = true;
			loopExist = true;
			loopHit = t;
			System.out.println("Loop hit: " + loopHit.id + ", " + loopHit.name);

		}

		//if node is unvisited
		else if(t.status == 0){
			t.status = 1;

			for(int i = 0; i < t.outEdges.size(); i++){
				isRealizable(t.outEdges.get(i));
				//prints out the nodes contained in potential loops
				if(loopFound){
					if(t.id != loopHit.id){
						System.out.println("Node contained in loop: " + t.id + ", " + t.name);
					}
					else{
						loopFound = false;
					}
					break;
				}

			}
			//sets node to finnished
			t.status = 2;
		}

	}

	//calls the recursive method
	public boolean isRealizable(){
		isRealizable(first);
		if(loopExist){
			return false;
		}
		return true;
	}

	//-------------------------------------OLD CODE-----------------------------------------

	//finds the next task to start, the one to save most time as posible
/*	private Task calculateNext(){
		Task next = potentialNext.get(0);
		int index = 0;
		for(int i = 0; i < potentialNext.size(); i++){
			if(potentialNext.get(i).currentTime + potentialNext.get(i).time < next.currentTime + next.time){
				next = potentialNext.get(i);
				index = i;	
			}
		}
		potentialNext.remove(index);

		return next; 

	}*/

	//-------------------------------------------------------------------------------------


	//goes trough the graph and updating start and finnish time of tasks
	Task[] optimalTasks;
	int index = 0;
	ArrayList<Task> potentialNext = new ArrayList<>();
	private void optimalSchedule(Task t){

		//checks if the task is ready to execute
		if(t.cntPredecessors > 0){
			System.out.println("dfsdfsdfsdf : " + t.id);
			return;
		}

		//sets time vars for tasks
		t.earliestStart = t.currentTime;
		t.earliestFinnish = t.currentTime + t.time;
		t.latestStart = t.earliestStart;
		t.latestFinnish = t.earliestFinnish;
		t.done = true;
		optimalTasks[index] = t;
		index++;

		//recurs to the next node, and updates the times if posible
		for(int i = 0; i < t.outEdges.size(); i++){
			t.outEdges.get(i).cntPredecessors --;
			if(t.outEdges.get(i).currentTime < t.earliestFinnish){
				t.outEdges.get(i).currentTime = t.earliestFinnish;
			}
			if(t.outEdges.get(i).cntPredecessors == 0){		
				optimalSchedule(t.outEdges.get(i));				
			}

		}

		//-------------------------------OLD CODE, LESS EFECTIVE-------------------------------------

		//updates the time for tasks and marks it done, and adding it to optimal schedule array

		//Checks if the neighbor tasks can be added to posible next.. if all the prev tasks needed are completed
/*		boolean canBeAdded = true;
		for(int i = 0; i < t.outEdges.size(); i++){
			if(t.outEdges.get(i).done == false){

				for(int j = 0; j < t.outEdges.get(i).tasksBefore.length; j++){
					if(tasks[t.outEdges.get(i).tasksBefore[j] - 1].done == false){
						canBeAdded = false;
					}
				}
				if(canBeAdded){
					t.outEdges.get(i).currentTime = t.earliestFinnish;
					potentialNext.add(t.outEdges.get(i));					
				}

			}
			
		}

		if(potentialNext.size() > 0){
			optimalSchedule(calculateNext());
		}*/
		//-------------------------------------------------------------------------------------------------

		return;

	}

	//calls the recursive method to find the optimal schedule for project
	public void optimalSchedule(){
		optimalSchedule(first);

		//resets the done value, in case of new search or traverse
		for(int i = 0; i < tasks.length; i++){
			tasks[i].done = false;
		}
	}



	//----------------------------------------CALCULATE SLACK----------------------------------------------

	private void calculateSlack(Task t){

		if(t.outEdges.size() > 0){
			int smallestSlack = t.outEdges.get(0).earliestStart - t.earliestFinnish;
			for(int i = 0; i < t.outEdges.size(); i++){
				if(t.outEdges.get(i).earliestStart - t.earliestFinnish < smallestSlack){
					smallestSlack = t.outEdges.get(i).earliestStart - t.earliestFinnish;
				}
			}			
			t.slack = smallestSlack;
			t.latestStart = t.earliestStart + t.slack;
			t.latestFinnish = t.earliestFinnish + t.slack;
		}
		for(int i = 0; i <t.outEdges.size(); i++){
			calculateSlack(t.outEdges.get(i));
		}
		return;

	}



	public void calculateSlack(){
		calculateSlack(first);
	}

	//----------------------------------------READING FILE----------------------------------------------

	//setting the neighbor nodes/tasks
	private void setOutEdges(){
		for(int i = 0; i < tasks.length; i++){
			for(int j = 0; j < tasks[i].tasksBefore.length; j++){
				tasks[tasks[i].tasksBefore[j] - 1].outEdges.add(tasks[i]);
			}
		}
	}

	//Sets the starting node
	private void setFirst(){
		boolean isFirst = false;
		for(int i = 0; i < tasks.length; i++){
			if(tasks[i].tasksBefore.length == 0){
				first = tasks[i];
				System.out.println("firrrst: " + first.id);
				isFirst = true;
				break;
			}
		}
		if(isFirst == false){
			System.out.println("Cant start project, every task depends on another task");
			endProject();
		}
	}

	//Reding file, and creates data structure
	public void readFile(String fileName) throws Exception{
		Scanner myFile = new Scanner(new File(fileName));

		tasks = new Task[Integer.parseInt(myFile.nextLine())];
		optimalTasks = new Task[tasks.length];
		myFile.nextLine();

		while(myFile.hasNextLine()){
			String line = myFile.nextLine();
			String[] lineArray = line.split("\\s+");

			int[] tasksBefore = new int[lineArray.length - 5];			
			int index = 0;
			for(int i = 4; i < lineArray.length; i++){
				int tmp = Integer.parseInt(lineArray[i]);
				if(tmp != 0){
					tasksBefore[index] = tmp;
					index++;
				}
				else{
					break;

				}
			}

			//creates the task objects
			Task tmp = new Task(Integer.parseInt(lineArray[0]), 
								lineArray[1], 
								Integer.parseInt(lineArray[2]), 
								Integer.parseInt(lineArray[3]), 
								tasksBefore);

			tasks[Integer.parseInt(lineArray[0]) -1 ] = tmp;

		}
		setOutEdges();
		setFirst();
	}

	//prints info on all tasks
	public void print(){
		for(int i = 0; i < tasks.length; i++){
			tasks[i].printTask();
		}
	}

	public void printOptimal(){
		for(int i = 0; i < optimalTasks.length; i++){
			optimalTasks[i].printTask();
		}
	}

	//prints a simulation of the project execution
	public void printSim(){
		int time = 0;
		boolean run = true;
		int currentStaff = 0;
		while(run){
			System.out.println("Time: " + time);
			for(int i = 0; i < tasks.length; i++){
				if(tasks[i].earliestStart == time){
					System.out.println("\tstarting : " + tasks[i].id);
					currentStaff += tasks[i].staff;
				}
				else if(tasks[i].earliestFinnish == time){
					System.out.println("\tFinnished: " + tasks[i].id);
					currentStaff -= tasks[i].staff;
				}
			}
			System.out.println("\tcurrent staff: " + currentStaff + "\n----------");

			if(time == optimalTasks[optimalTasks.length -1].earliestFinnish){
				System.out.println("\n**** Shortest possible project execution is " + time + " ****");
				run = false;
			}
			time ++;
		}
	}

	//Ending the program
	private void endProject(){
		System.out.println("\nproject ending..");
	}

}