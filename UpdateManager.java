
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import com.google.gson.Gson;

class UpdateManager{
	// Reads settings and region.json files to understand what to update
	
	/*
		Determine Version
			http://ddragon.leagueoflegends.com/realms/na.js
	
		Static Data
			http://ddragon.leagueoflegends.com/cdn/6.22.1/data/en_US/item.json
			http://ddragon.leagueoflegends.com/cdn/6.22.1/data/en_US/champion.json
			http://ddragon.leagueoflegends.com/cdn/6.22.1/data/en_US/summoner.json
	
		Images
			http://ddragon.leagueoflegends.com/cdn/6.22.1/img/sprite/item2.png
			http://ddragon.leagueoflegends.com/cdn/6.22.1/img/sprite/champion0.png
			http://ddragon.leagueoflegends.com/cdn/6.22.1/img/sprite/spell0.png
	*/
	
	private FileManager fileManager;
	private Map<String,String> settings;
	
	public UpdateManager(){
		fileManager = new FileManager();
		settings = fileManager.readSettings();
	}
	
	public void loadRegion(String regionSlug){
		// Download the regions file and save it
		String address = "http://ddragon.leagueoflegends.com/realms/" + regionSlug + ".js";
		String download = new String(fileManager.downloadFile(address));
		download = download.replace("Riot.DDragon.m=", "").replace(";", "").trim();
		fileManager.saveFile(true, "regions/" + regionSlug, regionSlug + ".json", download.getBytes());
		
		String baseUrl = getBaseURL(regionSlug);
		System.out.println(baseUrl);
		// check if that baseUrl was the same as before.
		// If so, no need to re-download files
	}
	
	
	private String getBaseURL(String regionSlug){
		String regionJson = new String(fileManager.loadFile(true, "regions/"+regionSlug, regionSlug+".json"));
		
		Gson gson = new Gson();
		Map<String, Object> json = gson.fromJson(regionJson, Map.class);
		String version = (String) json.get("v");
		String cdn = (String) json.get("cdn");
		return cdn + "/" + version;
	}
	
	private void updateStaticData(String region, String cdn, String version, String language){
		String basePath = cdn + "/" + version;
		String languagePath = basePath + "/data/" + language;
		
		getJsonAndSprites(region, basePath, languagePath, "item");
		getJsonAndSprites(region, basePath, languagePath, "champion");
		getJsonAndSprites(region, basePath, languagePath, "summoner");
	}
	
	private void getJsonAndSprites(String region, String basePath, String languagePath, String dataName){
		byte[] data = fileManager.downloadFile(languagePath + "/" + dataName + ".json");
		fileManager.saveFile(true, "regions/" + region, dataName + ".json", data);
		
		Set<String> sprites = new HashSet<String>();
		Gson gson = new Gson();
		Map<String, Object> json = gson.fromJson(new String(data), Map.class);
		
		// Always formatted "data"->(String Key)->"image"->"sprite"
		Map<String, Object> jsonData = (Map<String, Object>)json.get("data");
		Map<String, String> image = null;
		Map<String, Object> dataElement = null;
		for(Map.Entry<String, Object> entry : jsonData.entrySet()){
			// entry is our String Key
			dataElement = (Map<String, Object>) entry.getValue();
			image = (Map<String, String>) dataElement.get("image");
			sprites.add((String)image.get("sprite"));
		}
		
		// download all the sprites found
		for(String entry : sprites){
			byte[] imageData = fileManager.downloadFile(basePath + "/img/sprite/" + entry);
			fileManager.saveFile(true,"img/" + dataName, entry, imageData);
		}
		
	}
	
}