import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

//JavaFX import
/*import javafx.application.Application; // Klassen alle JavaFX-applikasjoner utvider fra
import javafx.event.ActionEvent; // Se javafx.event.Event for andre typer events
import javafx.event.EventHandler; // Tilsvarer ActionListener i swing
import javafx.scene.image.*; // for bilder av forskjellige formater png, jpg ... 
import javafx.scene.Scene; // Beholderen for alt innholdet
import javafx.scene.text.Text; // En tekstnode
import javafx.scene.layout.*; // for panes, HBox, VBox andre layoutnoder
import javafx.scene.control.*; // for Button, Checkbox, Hyperlink og andre grensesnittkontroller
import javafx.geometry.*; // plassering, orientering o.l.
import javafx.stage.*; // Hoeyeste nivaa av JavaFX-beholdere
import javafx.scene.paint.Color;  // Definerer fargekonstanter
import javafx.scene.text.Font; // For fonter*/

//import javafx.scene.text.FontWeight;  // For fonttype normal, bold, italic osv.


public class Oblig8{

	//Variabler som tar vare paa informasjon om brettstorrelsen..
	public static int boardWidth;
	public static int boardHeigth;
	public static int boxWidth;
	public static int boxHeight;
	
	//lister som inneholder alle rader, kolonner og ruter..
	private static String[][] stringBoard;
	private static char[][] charBoard;
	private static Rad[] rows;
	private static Kolonne[] columns;
	private static Rute[][] routes;
	private static Rute[][] sortedRoutes;
	private static Boks[][] boxes;
	public static int timer = 0;
	public static int numberOfBoards = 0;
	private static Beholder solvedBoards = new Beholder();
	

	//Main metode....
	public static void main (String[] args) throws Exception{
		String fileName = "info4.txt";
		lesFil(fileName);
		//sortArray();
		printBoard();

		solve();
		printBoard();
		routes[0][0].solveRoute();	
		solvedBoards.printSolved();
		System.out.println("\nAntall loesninger:  " + numberOfBoards);
		//launch(args);

	}
	/*
 	@Override 
	public void start(Stage window) throws Exception{
		BorderPane lerret = new BorderPane();
		BorderPane top = new BorderPane();
		BorderPane left = new BorderPane();
		BorderPane right = new BorderPane();
		BorderPane bottom = new BorderPane();
		Scene scene = new Scene(lerret, 1000, 1000);
		lerret.setStyle("-fx-background-color: #FFFFFF;");
		Text text = new Text(250, 50, "Soduko Solver");
		text.setFont(Font.font("Arial", 60));

		Button button1 = new Button("test");

		lerret.setTop(top);
		lerret.setLeft(left);
		lerret.setRight(right);
		lerret.setBottom(bottom);

		right.setTop(addHBoxLeft());
		bottom.setCenter(addHBoxBottom());
		left.setRight(addHBoxLeft());
		top.setCenter(addTitle());
		lerret.setCenter(addGrid());
		//lerret.setBottom(addGrid());


		window.setScene(scene);
		window.show(); 
		window.setTitle("Captain Hestro");

	}
	public GridPane addGrid(){
		boolean thick = false;
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #FFFFFF;");
		int top = 1;
		int left = 1;
		int bottom = 1;
		int right = 1;
		for(int i = 0; i < boardWidth; i++){

			for(int j = 0; j < boardWidth; j++){
				if(j == boardHeigth-1){
					bottom = 5;
				}
				else{
					bottom = 1;
				}
				if(i == boardWidth-1){
					right = 5;
				}
				if(j % boxHeight == 0 && j != 0 || j ==0){
					top = 5;
				}
				else{
					top = 1;
				}
				if(i % boxWidth == 0 && i != 0 || i == 0){
					left = 5;
				}
				else{
					left = 1;
				}

				StackPane s = new StackPane();
				s.setAlignment(Pos.CENTER);
				
				//s.setStyle("-fx-border-width: 2px; -fx-border-style: solid; -fx-border-color: black;");

				Border border = (new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(top, right, bottom, left))));
				s.setBorder(border);

				Text text = new Text(String.valueOf(solvedBoards.getSolve().get(0)[j][i]));
				text.setFont(Font.font("Arial", 30));
				s.getChildren().add(text);
				grid.add(s, i, j);
			}
		}
		for (int i = 0; i < 9; i++) {
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setPercentWidth(10);
			grid.getColumnConstraints().add(column1);

			RowConstraints r1 = new RowConstraints();
			r1.setPercentHeight(10);
			grid.getRowConstraints().add(r1);
		}
		return grid;
	}
 	//Sender ferdig brett til beholder som tar vare paa alle losningene..

	public HBox addHBoxLeft() {
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 50, 15, 12));
	    hbox.setSpacing(10);
	    //hbox.setStyle("-fx-background-color: #FFFFFF;");


	    Button buttonCurrent = new Button("Current");
	    buttonCurrent.setPrefSize(100, 20);
	    hbox.getChildren().addAll(buttonCurrent);

	    return hbox;
	}
	public HBox addHBoxBottom(){
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 50, 50, 300));
	    hbox.setSpacing(10);

	    hbox.setStyle("-fx-background-color: #EEEEEE;");
	    Text txt = new Text("Dette er 1 av " + solvedBoards.getSolve().size() + " Losninger!");
	    txt.setFont(Font.font("Arial", 30));
	    hbox.getChildren().addAll(txt);

	    return hbox;
	}

	public HBox addTitle(){
		HBox title = new HBox();
		Text titleTxt = new Text("Soduko Solver");
		titleTxt.setFont(Font.font("Arial", 60));
		title.setPadding(new Insets(15, 5, 5, 300));
		title.getChildren().add(titleTxt);
		return title;
	}*/

//-----------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------


	public static void sendSolved(){
		System.out.println(numberOfBoards);
		if(solvedBoards.getSolve().size() < 3500){
			int[][] tmpRoutes = new int[boardWidth][boardWidth];
			for(int i = 0; i < tmpRoutes.length; i++){
				for(int j = 0; j < tmpRoutes[i].length; j++){
					tmpRoutes[i][j] = routes[i][j].getValue();
				}
			}
			solvedBoards.addSolve(tmpRoutes);
		}
		else{
			//System.out.println("Det er ikke plass til flere losninger...");
		}
		
	}

	//Fyller alle ruter som bare har en loesning..
	public static void solve(){
		System.out.println("solve");
		boolean wasSolved = false;
		for(int i = 0; i < routes.length; i++){
			for(int j = 0; j < routes[i].length; j++){
				if(routes[i][j].getValue() == 0 && routes[i][j].finnAlleMuligeTall().length == 1){
					wasSolved = true;
					routes[i][j].setValue(routes[i][j].finnAlleMuligeTall()[0]);
					routes[i][j].setStartValue();
				}
			}
		}
		if(wasSolved){
			solve();
		}
	}

	//Lager datastukturen for brettet..
	private static void createBoard(){
		for(int i = 0; i < boardWidth; i++){
			Rad tmpRow = new Rad(i);
			rows[i] = tmpRow;
			Kolonne tmpColumn = new Kolonne(i);
			columns[i] = tmpColumn;
		}
		
		
		//Oppretter 2-dim array for boksene...
 		boxes = new Boks[boxWidth][boxHeight];
		int boxID = 0;
		for(int i = 0; i < boxHeight; i++){
			
			for(int j = 0; j < boxWidth; j++){
				Boks tmpBox = new Boks(boxID, boxWidth, boxHeight);
				boxes[j][i] = tmpBox;
				boxID++;
			}
		} 
	}

	
	//Metode som leser en fil og oppretter brettet...
	private static void lesFil(String fileName)throws Exception{
		int currentRow = 0;
		int currentColumn = 0;
		Scanner myFile = new Scanner(new File(fileName));
		
		boxHeight = Integer.parseInt(myFile.nextLine());
		boxWidth = Integer.parseInt(myFile.nextLine());
		boardWidth = boxHeight * boxWidth;
		boardHeigth = boxHeight * boxWidth;
		
		
		//oppretter arrayer som trengs, og oppretter datastukturen...
		stringBoard = new String[boardHeigth][boardWidth];
		charBoard = new char[boardHeigth][boardWidth];
		routes = new Rute[boardHeigth][boardWidth];
		rows = new Rad[boardHeigth];
		columns = new Kolonne[boardHeigth];
		
		createBoard();
		
		
		//Leser inn filen og lagrer det i en array...
		int loopCounter = 0;
		while(myFile.hasNextLine()){
			String[] tmpArray = myFile.nextLine().split("");
			stringBoard[loopCounter] = tmpArray;
			loopCounter++;
		}
		
		for(int i = 0; i < stringBoard.length; i++){
			for(int j = 0; j < stringBoard[i].length; j++){
				String s = stringBoard[i][j];
				charBoard[i][j] = s.charAt(0);
			}
		}
		//Leser info fra stringArray og lagrer det i rader, kolonner, bokser og ruter...
		int routeID = 0;
		int currentBoxX = 0;
		int currentBoxY = 0;
		for (int i = 0; i < boardHeigth; i++){
			//System.out.println("---------------");
			for(int j = 0; j < boardHeigth; j++){
				currentBoxY = j/boxWidth;
				currentBoxX = i/boxHeight;

				//System.out.println("Y: " + currentBoxY + "      X:" +  currentBoxX);
 				Rute tmpRute = new Rute(routeID, tegnTilVerdi(charBoard[i][j]), rows[i], columns[j], boxes[currentBoxX][currentBoxY]);
				routes[i][j] = tmpRute;
				if(routes[0][0] != null){
					if(j == 0 && i != 0){
						//System.out.println("i: " + i + "/t j: " + j);
						//System.out.println(tmpRute.getValue());
						tmpRute.previous = routes[i-1][boardWidth-1];
						tmpRute.previous.next = tmpRute;
					}					

					else if(j != 0){
						//System.out.println(tmpRute.getValue());
						tmpRute.previous = routes[i][j-1];
						tmpRute.previous.next = tmpRute;
					}
				}
				rows[i].addRoute(tmpRute);
				columns[j].addRoute(tmpRute);
				boxes[currentBoxX][currentBoxY].addRoute(tmpRute);
				routeID++; 

			}
			
		}
	}
	
	//Skriver ut brettet 
 	public static void printBoard(){
		System.out.println("\n");
		for(int i = 0; i < routes.length; i++){
			
			if(i % (boxHeight) == 0 && i != 0){
				System.out.print("\t\t\t\t");
				for(int j = 0; j < boardWidth; j++){
					if(j % boxWidth == 0 && j != 0){
						System.out.print("+");

					}
					System.out.print("--");
				}
				System.out.println("");
			} 
			System.out.print("\t\t\t\t");
			for(int j = 0; j < routes[i].length; j++){
				if(j % (boxWidth) == 0 && j != 0){
					System.out.print("|");
				}
				
				System.out.print(verdiTilTegn(routes[i][j].getValue(), ' ') + " ");
				
				

			}

		System.out.println("");
			
		}
		System.out.println("-------------------------------------------------------------------------------------\n");
	}

	//Tegn til verdier...
	public static int tegnTilVerdi(char tegn) {
		if (tegn == '.') {                // tom rute
			// DENNE KONSTANTEN MAA DEKLARERES
			return 0;
		} else if ('1' <= tegn && tegn <= '9') {    // tegn er i [1, 9]
			return tegn - '0';
		} else if ('A' <= tegn && tegn <= 'Z') {    // tegn er i [A, Z]
			return tegn - 'A' + 10;
		} else if (tegn == '@') {                   // tegn er @
			return 36;
		} else if (tegn == '#') {                   // tegn er #
			return 37;
		} else if (tegn == '&') {                   // tegn er &
			return 38;
		} else if ('a' <= tegn && tegn <= 'z') {    // tegn er i [a, z]
			return tegn - 'a' + 39;
		} else {                                    // tegn er ugyldig
			return -1;
		
		}
	}
	//Verdier til tegn....
    public static char verdiTilTegn(int verdi, char tom){
        if (verdi == 0) {                           // tom
            return tom;
        } else if (1 <= verdi && verdi <= 9) {      // tegn er i [1, 9]
            return (char) (verdi + '0');
        } else if (10 <= verdi && verdi <= 35) {    // tegn er i [A, Z]
            return (char) (verdi + 'A' - 10);
        } else if (verdi == 36) {                   // tegn er @
            return '@';
        } else if (verdi == 37) {                   // tegn er #
            return '#';
        } else if (verdi == 38) {                   // tegn er &
            return '&';
        } else if (39 <= verdi && verdi <= 64) {    // tegn er i [a, z]
            return (char) (verdi + 'a' - 39);
        }else {                                    // tegn er ugyldig
            throw new IllegalStateException(verdi + " Er ugyldig");    
            // HUSK DEFINISJON AV UNNTAKSKLASSE
        }
		
    }
}
/* 	public static void sortArray(){
 		sortedRoutes = routes;
 		for(int i = 0; i < sortedRoutes.length; i++){
			for(int j=  0; j < sortedRoutes[i].length; j++){
				for(int p = 0; p < sortedRoutes.length; p++){
					for(int u = 0; u < sortedRoutes[i].length; u++){
						if(sortedRoutes[p][u].finnAlleMuligeTall().length > sortedRoutes[i][j].finnAlleMuligeTall().length){
							Rute temp = sortedRoutes[i][j];
							sortedRoutes[i][j] = sortedRoutes[p][u];
							sortedRoutes[p][u] = temp;
						}
					}
				}
			}
		}
 		for(int i = 0; i < sortedRoutes.length; i++){
			for(int j = 0; j < sortedRoutes[i].length; j++){
				if(sortedRoutes[0][0] != null){
					if(j == 0 && i != 0){
						sortedRoutes[i][j].previous2 = sortedRoutes[i-1][boardWidth-1];
						sortedRoutes[i][j].previous2.next2 = sortedRoutes[i][j];
					}					
					else if(j != 0){
						//System.out.println(sortedRoutes[i][j].getValue());
						sortedRoutes[i][j].previous2 = routes[i][j-1];
						sortedRoutes[i][j].previous2.next2 = sortedRoutes[i][j];
					}
				}
			}
		}

 	}*/

/*	public static void updateRoutes(){
		for(int i = 0; i < routes.length; i++){
			for(int j = 0; j < routes[i].length; j++){
				for(int p = 0; p < routes.length; p++){
					for(int u = 0; u < routes[p].length; u++){
						if(sortedRoutes[i][j].getID() == routes[p][u].getID()){
							routes[p][u].setValue(sortedRoutes[i][j].getValue());
						}
					}
				}
			}
		}
	}*/
