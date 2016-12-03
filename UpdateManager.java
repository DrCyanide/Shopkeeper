
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import com.google.gson.Gson;

import java.awt.*;
import javax.swing.*;
import java.util.List;

class UpdateManager extends SwingWorker<Void, Integer>{
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
	private View view = null;
	
	private String regionSlug;
	
	private int champSprites = 5;
	private int itemSprites = 3;
	private int summonerSprites = 1;
	private int maxSteps;
    private float percentage;
    private int currentProgress = 0;
	
	public UpdateManager(){
		fileManager = new FileManager();
		settings = fileManager.readSettings();
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	// Spawn a thread to do all the updating
	// Show the updating dialog
	// When finished, close the updating dialog
	
	public void loadRegion(){
	    // If no region was specified and none in settings, then use "na"
	    String regionSlug = settings.get("region");
	    if(regionSlug.equals(""))
	        regionSlug = "na";
        loadRegion(regionSlug);
	}
	
	public void loadRegion(String regionSlug){
	    settings.put("region", regionSlug);
	    fileManager.writeSettings(settings);
	    this.regionSlug = regionSlug;
	}
	
	private void updateProgressEstimate(){
	    maxSteps = champSprites + itemSprites + summonerSprites + 4; // 4 for the json files
	    percentage = 100/maxSteps;
	}
	
	@Override
    protected Void doInBackground() {
        // run the updates
        updateProgressEstimate();
        
        // Download the regions file and save it
		String address = "http://ddragon.leagueoflegends.com/realms/" + regionSlug + ".js";
		byte[] regionJson = fileManager.downloadFile(address);
		String download;
		if (regionJson.length > 0){
		    // extract the region's json file from the js file
		    download = new String(regionJson);
		    download = download.replace("Riot.DDragon.m=", "").replace(";", "").trim();
		    fileManager.saveFile(true, "regions/" + regionSlug, regionSlug + ".json", download.getBytes());
		}
		else{
		    // Load the old region, since that's better than empty
		    download = new String(fileManager.loadFile(true, "regions/" + regionSlug, regionSlug + ".json"));
		}
		currentProgress += 1;
		publish(Math.round(currentProgress * percentage)); // first step complete
		
		Gson gson = new Gson();
	    Map<String, Object> json = gson.fromJson(download, Map.class);
	    String version = (String) json.get("v");
	    
	    // If the league_version is the same, then there should be no new images, so no need to update
	    if(!version.equals(settings.get("league_version")))
	    {
		    String baseUrl = getBaseURL(regionSlug);
		    
		    updateStaticData(regionSlug, baseUrl, (String)json.get("l"));
		
		    settings.put("league_version",version);
		    fileManager.writeSettings(settings);
		}
        
        return null;
    }
    
    @Override
    protected void process(List<Integer> complete){
        view.updateProgress(complete.get(0));
    }
    
    @Override
    public void done() {
        view.updateProgress(100);
        view.closeUpdateDialog();
    }
	
	
	
	private String getBaseURL(String regionSlug){
		String regionJson = new String(fileManager.loadFile(true, "regions/"+regionSlug, regionSlug+".json"));
		
		Gson gson = new Gson();
		Map<String, Object> json = gson.fromJson(regionJson, Map.class);
		String version = (String) json.get("v");
		String cdn = (String) json.get("cdn");
		return cdn + "/" + version;
	}
	
	private void updateStaticData(String regionSlug, String basePath, String language){
		String languagePath = basePath + "/data/" + language;
		
		getJsonAndSprites(regionSlug, basePath, languagePath, "item");
		getJsonAndSprites(regionSlug, basePath, languagePath, "champion");
		getJsonAndSprites(regionSlug, basePath, languagePath, "summoner");
	}
	
	private void getJsonAndSprites(String regionSlug, String basePath, String languagePath, String dataName){
		String path = languagePath + "/" + dataName + ".json";
		byte[] data = fileManager.downloadFile(path);
		int tries = 0;
		while(data.length == 0 && tries < 3){
		    data = fileManager.downloadFile(languagePath + "/" + dataName + ".json");
		}
		if(data.length == 0){
			System.out.println("Failed to download '" + path + "'");
			return;
		}
		fileManager.saveFile(true, "regions/" + regionSlug, dataName + ".json", data);
		
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
		
		// A little bit of future proofing for the progress bar
		if (dataName.equals("item") && sprites.size() != itemSprites){
		    itemSprites = sprites.size();
		    updateProgressEstimate();
		}
		if (dataName.equals("champion") && sprites.size() != champSprites){
		    champSprites = sprites.size();
		    updateProgressEstimate();
		}
		if (dataName.equals("summoner") && sprites.size() != summonerSprites){
		    summonerSprites = sprites.size();
		    updateProgressEstimate();
		}
		
		// The .json file is downloaded
		currentProgress += 1;
		publish(Math.round(currentProgress * percentage));
		
		
		// download all the sprites found
		for(String entry : sprites){
			while(true){
				byte[] imageData = fileManager.downloadFile(basePath + "/img/sprite/" + entry);
				if(imageData.length > 0){
					fileManager.saveFile(true,"img/" + dataName, entry, imageData);
					// The sprite file is downloaded
		            currentProgress += 1;
		            publish(Math.round(currentProgress * percentage));
					break;
				}
			}
		}
		
	}
	
}
