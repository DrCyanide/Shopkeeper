

class Shopkeeper{
	// Ideally find an automatic way to generate the supported servers list
	String[] supportedServers = {"NA", "BR", "EUNE", "EUW", "JP", "KR", "LAN", "LAS", "OCE", "RU", "TR"};
	
	public Shopkeeper(){
		View view = new View(supportedServers);
	}
	
	public static void main(String[] args){
		Shopkeeper s = new Shopkeeper();
	}
}
