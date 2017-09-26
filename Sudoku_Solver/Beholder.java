import java.util.ArrayList;
import java.util.Arrays;
public class Beholder{
	private ArrayList<int[][]> solvedBoards = new ArrayList<>();

	//Legger til et lost brett i arrayen..
	public void addSolve(int[][] array){
		solvedBoards.add(array);
	}

	//Henter Arraylisten med alle losningene..
	public ArrayList<int[][]> getSolve(){
		return solvedBoards;
	}

	//Skriver ut losningene til terminalen..
	public void printSolved(){
		if(solvedBoards.size() > 0){
			System.out.println("\n");
			for(int i = 0; i < solvedBoards.get(0).length; i++){
				
				if(i % (Oblig8.boxHeight) == 0 && i != 0){
					System.out.print("\t\t\t\t");
					for(int j = 0; j < Oblig8.boardWidth; j++){
						if(j % Oblig8.boxWidth == 0 && j != 0){
							System.out.print("+");

						}
						System.out.print("--");
					}
					System.out.println("");
				} 
				System.out.print("\t\t\t\t");
				for(int j = 0; j < solvedBoards.get(0)[i].length; j++){
					if(j % (Oblig8.boxWidth) == 0 && j != 0){
						System.out.print("|");
					}
					
					System.out.print(Oblig8.verdiTilTegn(solvedBoards.get(0)[i][j], ' ') + " ");
					
					

				}

			System.out.println("");
				
			}
			System.out.println("-----------------------------------------------------------------------------------------\n");



			if(solvedBoards.size() > 0){
				for(int i = 0; i < solvedBoards.size(); i++){
					System.out.print(i+1 + ":\t");
					for(int j = 0; j < solvedBoards.get(i).length; j++){
						for(int p = 0; p < solvedBoards.get(i)[j].length; p++){
							System.out.print(solvedBoards.get(i)[j][p]);
						}
						System.out.print("//");
						
					}
					System.out.println("");
				}				
			}
			else{
				System.out.println("Det er ingen loesninger..");
			}			
		}

	}
}