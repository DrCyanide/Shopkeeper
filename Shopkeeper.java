

class Shopkeeper{
	
	//private FileManager fileManager;
	private UpdateManager updateManager;
	
	public Shopkeeper(){
		updateManager = new UpdateManager();
		updateManager.loadRegion();
		
		/*
		Item item = new Item("3751");
		System.out.println("Item Name: " + item.name);
		System.out.println("Sprite: " + item.sprite);
		System.out.println("X: " + item.x + " Y: " + item.y);
		System.out.println("H: " + item.h + " W: " + item.w);
		*/
	}
	
	public static void main(String[] args){
		Shopkeeper s = new Shopkeeper();
	}
	
	
	
}
