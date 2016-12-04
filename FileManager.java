import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.*;
import java.util.Arrays;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

class FileManager{	
	private String osType, saveDir, cdn, language;
	private Map<String, String> settings;
	private String settingsFile = "settings.json";
	
	public FileManager(){
		osType = determineOSType();
		saveDir = determineSaveDir();
		
		settings = readSettings();
		if(settings.get("league_dir").equals("")){
			setLeagueDir(determineLeagueDirectory());
			
		} 
		//updateStaticData("na", "http://ddragon.leagueoflegends.com/cdn", "6.22.1", "en_US");
		convertRiotItemSets();
	}
	
	// Read saveDir/settings.json
	public Map<String, String> readSettings(){
		Map<String, String> json;
		String f = new String(loadFile(true,"", settingsFile));
		
		if(f.length() > 0){
			Gson gson = new Gson();
			json = gson.fromJson(f, Map.class);
		} else { 
			json = new HashMap<String,String>();
			json.put("region", "");
			json.put("league_version", "");
			json.put("league_dir","");
		}
		return json;
	}
	
	public void writeSettings(Map<String, String> set){
		settings = set;
		Gson gson = new Gson();
		String json = gson.toJson(set);
		saveFile(true, "", settingsFile, json.getBytes());
	}	
	
	private String determineOSType(){
		//System.getProperties().list(System.out);
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("windows")){
			return "windows";
		} else {
			return "mac"; 
		}
		// no Linux version of the game right now
		//else if(osName.contains("linux")){
		//    return "linux";
		//}
	}
	
	private String determineLeagueDirectory(){
		// Use the OS to guess where League would be installed 
		
		String leagueDir = "";
		if(osType.equals("windows")){
		    String path = "Riot Games/League of Legends/Config";
		    /*
			// check C: first, then D:
			File f = new File("C:/" + path);
			if(f.exists() && f.isDirectory()){
				leagueDir = "C:/" + path;
			} else{
				f = new File("D:/" + path);
				if(f.exists() && f.isDirectory()){
					leagueDir = "D:/" + path;
				}
			}
			*/
			File[] roots = File.listRoots(); // Loop through all drive letters on Windows
			for(int i=0; i<roots.length; i++){
			    File f = new File(roots[i], path);
			    if(f.exists() && f.isDirectory()){
			        leagueDir = f.getAbsolutePath();
			        break;
			    } 
			}
		} else {
		    // Mac dir
		    String macPath = "/Applications/League of Legends.app/Contents/Lol/Config";
		    File f = new File(macPath);
		    if(f.exists() && f.isDirectory()){
		        leagueDir = macPath;
		    }
		}
		 
		return leagueDir;
	}
	public String getLeagueDir(){
		return settings.get("league_dir");
	}
	public void setLeagueDir(String path){
		// Used incase the UI has to prompt the user where League is installed
		settings.put("league_dir", path);
		writeSettings(settings);
	}
	
	private String determineSaveDir(){
		// Use the OS to know where to save files (images, item.json, etc)
		if(osType.equals("windows")){
			// save it in their AppData/Roaming, the same place Minecraft saves it's content
			return System.getProperty("user.home") + "/AppData/Roaming/LoL_Shopkeeper";
		} else if(osType.equals("mac")){
		    // Don't know where to save on Mac...
		    // Minecraft saves to ~/Library/Application Support/minecraft/
		    return System.getProperty("user.home") + "/Library/Application Support/LoL_Shopkeeper";
	    }
		String unixFile = System.getProperty("user.home") + "/.LoL_Shopkeeper";
		return unixFile;
	}
	
	public void saveFile(boolean saveWithShopkeeper, String path, String filename, byte[] content){
		/* saveWithShopkeeper: 
			True = save it with LoL Shopkeeper's files
			False = save it in the League dir
		*/
		if( saveWithShopkeeper){
			path = saveDir + "/" + path;
		} else {
			path = settings.get("league_dir") + "/" + path;
		}
		
		File f = new File(path);
		if(!f.exists()){
			try{
			f.mkdirs();
			//f.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		// write it
		f = new File(path + "/" + filename);
		try{
			FileOutputStream writer = new FileOutputStream(f, false); // false = don't append
			writer.write(content);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public byte[] loadFile(boolean loadFromShopkeeper, String path, String filename){
		/* loadFromShopkeeper: 
			True = load it from LoL Shopkeeper's files
			False = load it from the League dir
		*/
		String newPath = "";
		if(loadFromShopkeeper){
			newPath = saveDir;
		} else {
			newPath = settings.get("league_dir");
		}
		if(path.length() > 0){
			newPath += "/" + path;
		}
		
		File f = new File(newPath + "/" + filename);
		if(!f.exists()){
			// throw filenotfoundexception
			//System.out.println("File not found: " + f.toPath());
			return new byte[0];
		}
		try{
			//Path path = Paths.get(path + "/" + filename);
			Path targetPath = f.toPath();
			byte[] data = Files.readAllBytes(targetPath);
			return data;
		} catch (Exception e){
			e.printStackTrace();
		}
		return new byte[0];
	}	
	
	
	public Map<String, BufferedImage>  loadImages(String type){
		// type = "item", "champion" or "summoner"
		Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	    
		// Iterate over the folder, returning all images 
		File directory = new File(saveDir + "/img/" + type);
		File[] files = directory.listFiles();
		Arrays.sort(files);
		for(File file : files){
			if(file.getName().contains(type)){
				try{
				images.put(file.getName(), ImageIO.read(file));
				} catch (Exception e){
					System.out.println("Unable to read '" + file.getName() + "'");
					e.printStackTrace();
				}
			}
		}
	    
		return images;
	}
	
	public byte[] downloadFile(String address){
		InputStream instream = null;
		BufferedInputStream bin = null;

		ArrayList<Byte> allBytes = new ArrayList<Byte>();
		try{
			URL url = new URL(address);
			URLConnection connection = url.openConnection();
			instream = connection.getInputStream();
			bin = new BufferedInputStream(instream);
			
			while(bin.available()>0){ // had issues with the file being truncated, added the sleep to get more bytes...
				while(bin.available()>0){
					allBytes.add((byte)bin.read());
				}
				Thread.sleep(750);
			}

			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				if(instream != null){
					instream.close();
				}
				if(bin != null){
					bin.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		byte[] output = new byte[allBytes.size()];
		for(int i=0; i<allBytes.size(); i++){
			output[i] = allBytes.get(i);
		}
		return output;
	}

	public void convertRiotItemSets(){
		// Riot didn't follow the API for global sets, which means that pre-existing item sets are going to be painful to maintain/edit without changing them first.
		// Riot saved all active item sets in Config/ItemSets.json so that'll be the master list to re-build

		File backupFolder = new File(saveDir + "/backup/Champions");
		if(!backupFolder.exists()){
			try{
				backupFolder.mkdirs();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Path dest = backupFolder.toPath();
		
		File championsPath = new File(settings.get("league_dir") + "/Champions");
		// Make a backup first, then delete the original
		try{
			if(championsPath.exists() && championsPath.isDirectory()){
			
				Files.copy(championsPath.toPath(), dest, StandardCopyOption.COPY_ATTRIBUTES,StandardCopyOption.REPLACE_EXISTING);
			
				// iterate through each champion and remove item sets that look like RIOT_ItemSet_#.json
				String[] allChampions = championsPath.list();
				if(allChampions != null){
					for(String championKey : allChampions){
						File champion = new File(championsPath, championKey);
						if(champion.isDirectory()){
							File backupChampion = new File(backupFolder, championKey);
							//System.out.println("" + champion.getPath() + "\n" + backupChampion.getPath());
							Files.copy(champion.toPath(), backupChampion.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
							
							File recommended = new File(champion.getPath() + "/Recommended");
							if(recommended.exists() && recommended.isDirectory()){
								File backupRecommended = new File(backupChampion, "Recommended");
								Files.copy(champion.toPath(), backupRecommended.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
								
								File[] sets = recommended.listFiles();
								for(File set : sets){
									String fileName = set.getName();
									if(fileName.contains("RIOT_ItemSet_") && fileName.contains(".json")){
										Files.copy(set.toPath(), (new File(backupRecommended, fileName)).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
										
										//set.delete(); // delete the original
									}
								}
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("Failed to make backup or clear bad item sets.");
			e.printStackTrace();
		}
		
		// Backup original ItemSets.json
		try{
			Files.copy((new File(settings.get("league_dir") + "/ItemSets.json")).toPath(), (new File(saveDir + "/backup/ItemSets.json")).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e){
			System.out.println("Failed to make backup of ItemSets.json");
		}
		
		
		// Rebuild from ItemSet.json
		Map<String, Object> ItemSet;
		Gson gson = new Gson();
		String f = new String(loadFile(false,"", "ItemSets.json"));
		ItemSet = gson.fromJson(f, Map.class);
		List<Map<String,Object>> itemsets = (ArrayList)ItemSet.get("itemSets");
		for(Map<String, Object> set : itemsets){
			System.out.println(set.get("title"));
		}
		
		
		
		System.exit(0);
		
	}
	
}
