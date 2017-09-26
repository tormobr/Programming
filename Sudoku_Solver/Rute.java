import java.util.ArrayList;
import java.util.Arrays;
public class Rute{
	private int ID;
	private int startValue;
	private int value;
	private Rad row;
	private Kolonne column;
	private Boks box;
	public Rute next;
	public Rute next2;
	public Rute previous;
	public Rute previous2;
	
	//konstruktor....
	Rute(int ID, int value, Rad row, Kolonne column, Boks box){
		this.value = value;
		this.row = row;
		this.ID = ID;
		this.startValue = this.value;
		this.column = column;
		this.box = box;
	}

	//Henter den unike IDen til en rute..
	public int getID(){ return ID;}

	//Henter verdien til ruten..
	public int getValue(){ return value;}

	//Setter verdien til ruten..
	public void setValue(int value){ this.value = value;}

	//Setter startverdien til ruten..
	public void setStartValue(){ startValue = value;}

/*	private void updateData(){
		for(int i = 0; i < box.getRoutes().length; i++){
			if(this.ID == box.getRoutes()[i].getID()){
					box.getRoutes()[i] = this;
					//System.out.println("Box update");
					break;
			}
		}
		for(int i = 0; i < row.getRoutes().length; i++){
			if(this.ID == row.getRoutes()[i].getID()){
					row.getRoutes()[i] = this;
					//System.out.println("Row update");
					break;
					
			}
		}
		
		for(int i = 0; i < column.getRoutes().length; i++){
			if(this.ID == column.getRoutes()[i].getID()){
					column.getRoutes()[i] = this;
					//System.out.println("Column update");
					break;
			}
		}	
	}*/

	//metoder som brute-forcer seg gjennom brettet og finner alle losningene...
	public void solveRoute(){
		Oblig8.timer ++;
		int[] numbers = this.finnAlleMuligeTall();
		if(Oblig8.timer % 50000000 == 0){
			System.out.println(Oblig8.numberOfBoards);
			Oblig8.printBoard();
		}
		for(int i = 0; i < numbers.length; i++){
			this.value = numbers[i];
			//Oblig8.updateRoutes();
			//System.out.println(Arrays.toString(numbers));
			//Oblig8.printBoard();
			//System.out.println(ID);
			//updateData();
			
			if(this.next == null){
				if(this.value != 0){
					//System.out.println("Loesning funnet!!!");
					//Oblig8.printBoard();
					//Oblig8.updateRoutes();
					Oblig8.numberOfBoards++;
					Oblig8.sendSolved();
				}

			}
			else{
				this.next.solveRoute();
			}
		}
		this.value = startValue;
		//updateData();
	}
	
/*	public int mustBeHere(){
		boolean isPossible = false;
		if(this.value == 0){
			for(int i = 0; i < this.finnAlleMuligeTall().length; i++){
				for(int j = 0; j < box.getRoutes().length; j++){
					for(int p = 0; p < box.getRoutes()[j].finnAlleMuligeTall().length; p++){
						if(box.getRoutes()[j].finnAlleMuligeTall()[p] == this.finnAlleMuligeTall()[i] && box.getRoutes()[j].getID() != this.ID){
							isPossible = true;
							//System.out.println("Tallet kan vare flere steder i boksen");
							break;
						}
					}
					
				}
				for(int j = 0; j < row.getRoutes().length; j++){
					for(int p = 0; p < row.getRoutes()[j].finnAlleMuligeTall().length; p++){
						if(row.getRoutes()[j].finnAlleMuligeTall()[p] == this.finnAlleMuligeTall()[i] && row.getRoutes()[j].getID() != this.ID){
							isPossible = true;
							//System.out.println("Tallet kan vare flere steder i raden");
							break;
						}
					}
					
				}
				for(int j = 0; j < column.getRoutes().length; j++){
					for(int p = 0; p < column.getRoutes()[j].finnAlleMuligeTall().length; p++){
						if(column.getRoutes()[j].finnAlleMuligeTall()[p] == this.finnAlleMuligeTall()[i] && column.getRoutes()[j].getID() != this.ID){
							isPossible = true;
							//System.out.println("Tallet kan vare flere steder i kolonnen");
							break;
						}
					}
					
				}
				if(!isPossible){
					System.out.println("Her er det good! ! ");
					return this.finnAlleMuligeTall()[i];
				}
			}

		}
		return -1;

	}*/

	//Finner alle tall som er lov aa plasere i ruten..
   	public int[] finnAlleMuligeTall(){
		if(value != 0){
			int[] tmp = {startValue};
			return tmp;
		}
		else{
			ArrayList<Integer> numbers = new ArrayList<>();
			for(int i = 0; i < Oblig8.boardWidth+1; i++){
				if(checkBox(i) && checkColumn(i) && checkRow(i)){
					numbers.add(i);
				}
			}
			
			int[] tmp = new int[numbers.size()];
			for(int i = 0; i < numbers.size(); i++){
				tmp[i] = numbers.get(i);
			}
			return tmp;	
		}

	}
	//sjekker om et bestemt tall er lovlig aa plasere i ruten ved aa sjekke boksen den tillhorer...
	private boolean checkBox(int value){
		
		boolean clear = true;
		for(int j = 0; j < box.getRoutes().length; j++){
			if(value == box.getRoutes()[j].getValue()){
					return false;
			} 
		}	
		return true;
	}
	
	//sjekker om et bestemt tall er lovlig aa plasere i ruten ved aa sjekke kolonnen den tillhorer...
	private boolean checkColumn(int value){
		boolean clear = true;
		for(int j = 0; j < column.getRoutes().length; j++){
			if(value == column.getRoutes()[j].getValue()){
				return false;
			}
		}	
		return true;
	}
	
	//sjekker om et bestemt tall er lovlig aa plasere i ruten ved aa sjekke raden den tillhorer...
	private boolean checkRow(int value){
		boolean clear = true;
		for(int j = 0; j < row.getRoutes().length; j++){
			if(value == row.getRoutes()[j].getValue()){
					return false;
			}
		}	
		return true;
	}
}