
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class View{
    Map<String, Map<String, BufferedImage>> images;
	
	Map<String, Item> items;
	
    public View(){
        JFrame frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		loadBufferedImages();
		loadItems();
		
        JScrollPane scrollArea = new JScrollPane();
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel allContent = new JPanel();
        allContent.setLayout(new GridLayout(0, 5));
        
        //allContent.setPreferredSize(new Dimension(500, 400));
        //allContent.add(scrollPane);
        
        for(Map.Entry<String, Item> item : items.entrySet()){
            allContent.add(item.getValue().getSummaryPanel());
        }
        
        frame.getContentPane().add(allContent);
        frame.pack();
        frame.setVisible(true);
    }
	
	private void loadBufferedImages(){
	    images = new HashMap<String, Map<String, BufferedImage>>();
	
		FileManager fileManager = new FileManager();
		images.put("champions", fileManager.loadImages("champion"));
		images.put("items", fileManager.loadImages("item"));
		images.put("summoners", fileManager.loadImages("summoner"));
	}

    private void loadItems(){
        items = new HashMap<String, Item>();
        
        FileManager fileManager = new FileManager();
        Map<String, String> settings = fileManager.readSettings();
        String regionSlug = settings.get("region");
        
        // Read the item.json file from the region's folder
        String itemJsonString = new String(fileManager.loadFile(true, "regions/" + regionSlug, "item.json"));
        Gson gson = new Gson();
        Map<String, Object> itemJson = gson.fromJson(itemJsonString, Map.class);
        Map<String, Object> data = (Map<String, Object>)itemJson.get("data"); 
        
        for(String itemId : data.keySet()){
            Item item = new Item((Map<String, Object>)data.get(itemId));
            item.setIcon(images.get("items").get(item.sprite)); // Set the icon
			items.put(itemId, item);
		}
        
    }
   
}
