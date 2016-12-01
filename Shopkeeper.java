

class Shopkeeper{
	
	//private FileManager fileManager;
	//private UpdateManager updateManager;
	String[] supportedServers = {"NA", "BR", "EUNE", "EUW", "JP", "KR", "LAN", "LAS", "OCE", "RU", "TR"};
	
	public Shopkeeper(){
		//updateManager = new UpdateManager();
		//updateManager.loadRegion();
		
		View view = new View(supportedServers);
	}
	
	public static void main(String[] args){
		Shopkeeper s = new Shopkeeper();
	}
}
