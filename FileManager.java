import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.net.*;
import java.util.Arrays;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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
		
		//System.out.println(osType);
		
		//updateStaticData("na", "http://ddragon.leagueoflegends.com/cdn", "6.22.1", "en_US");
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
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			return "windows";
		}else{
			return "mac"; // no Linux version of the game right now.
		}
	}
	
	private String determineLeagueDirectory(){
		// Use the OS to guess where League would be installed 
		// for Windows "C:\Riot Games\League of Legends" or "D:\Riot Games\League of Legends"
		String path = "Riot Games/League of Legends";
		String leagueDir = "";
		if(osType.equals("windows")){
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
		}
		// Don't know where to check on Mac... 
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
		}
		// Don't know where to save on Mac...
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
		if(loadFromShopkeeper){
			path = saveDir + "/" + path;
		} else {
			path = settings.get("league_dir") + "/" + path;
		}
		File f = new File(path + "/" + filename);
		if(!f.exists()){
			// throw filenotfoundexception
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

}
