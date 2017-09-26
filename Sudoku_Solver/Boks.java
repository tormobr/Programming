public class Boks{
	private int ID;
	private Rute[] routes;
	private int currentPosition = 0;
	
	//kontruktor...
	Boks(int ID, int boxWidth, int boxHeight){
		
		this.ID = ID;
		this.routes = new Rute[boxHeight * boxWidth];
	}
	
	public void addRoute(Rute r){
		routes[currentPosition] = r;
		currentPosition++;
	}
	
	public Rute[] getRoutes(){
		return routes;
	}
}