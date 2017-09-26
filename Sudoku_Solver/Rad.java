public class Rad{
	private int ID;
	private int currentPosition = 0;
	private Rute[] routes;
	
	//konstruktor....
	Rad(int ID){
		this.routes = new Rute[Oblig8.boardWidth];
		this.ID = ID;
	}

	public void addRoute(Rute r){
		routes[currentPosition] = r;
		currentPosition++;
	}
	
	public void setRoute(Rute r, int index){
		routes[index] = r;
	}
	public Rute[] getRoutes(){
		return routes;
	}
}