

class Shopkeeper{
	
	//private FileManager fileManager;
	private UpdateManager updateManager;
	
	public Shopkeeper(){
		//fileManager = new FileManager();
		//fileManager.loadRegion("na");
		updateManager = new UpdateManager();
		updateManager.loadRegion("na");
		
		
		//String versionURL = fileManager.getBaseURL("na");
		//System.out.println(versionURL);
	}
	
	
	/*
	private String getLeaguePath(){
		String line;
		String path = "";
		Process p = null;
		boolean leagueRunning = false;
		try{ //Linux/Mac
			p = Runtime.getRuntime().exec("ps -e"); 
		} catch (Exception err){
			try{ // Windows
				p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe"); 
			} catch (Exception err2){
				System.out.println("Unable to determine if League is running, aborting");
				System.exit(0);
			}
		}
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				String[] parsed = line.split(" ");
				
				System.out.println(parsed[0]);
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return path;
	}
	*/
	
	public static void main(String[] args){
		Shopkeeper s = new Shopkeeper();
	}
	
	
	
}