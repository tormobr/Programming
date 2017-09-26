public class Kolonne{
	private int ID;
	private int currentPosition = 0;
	private Rute[] routes;
	
	//konstruktor....
	Kolonne(int ID){
		this.routes = new Rute[Oblig8.boardWidth];
		this.ID = ID;
	}
	
	public void addRoute(Rute r){
		routes[currentPosition] = r;
		currentPosition++;
	}
	
	public Rute[] getRoutes(){
		return routes;
	}
}