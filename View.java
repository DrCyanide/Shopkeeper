
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class View{
    Map<String, Map<String, BufferedImage>> images;
	Map<String, Item> items;
	
	JPanel itemList;
	ItemDetailsPanel itemDetails;
	ItemListener itemListener;
	ItemSetPanel itemSetPanel;
	JFrame frame;
	
	int itemListCol = 4;
	int itemListWidth = 325;
	
	int itemDetailsWidth = 285;
	
	int itemSetsWidth = 325;
	
	int maxWidth = itemListWidth + itemDetailsWidth + itemSetsWidth + 10;
	int maxHeight = 450;
	
	Color backgroundColor = Color.DARK_GRAY;
	
    public View(){
        frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(maxWidth,maxHeight));
		frame.setBackground(backgroundColor);
        
		loadBufferedImages();
		loadItems();
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		masterPanel.setBackground(backgroundColor);
		
		JPanel itemsPanels = new JPanel();
		itemsPanels.setLayout(new BorderLayout());
		
		itemDetails = new ItemDetailsPanel(items, itemDetailsWidth, maxHeight, backgroundColor);
		itemListener = new ItemListener(itemDetails);
		itemSetPanel = new ItemSetPanel(itemListener, itemSetsWidth, maxHeight, backgroundColor);
		
		// ------
        itemList = new JPanel();
        itemList.setLayout(new GridLayout(0, itemListCol));
        
        for(Map.Entry<String, Item> item : items.entrySet()){
            itemList.add(RenderItem.RenderItem(item.getValue(), itemListener));
        }
        
		JScrollPane scrollArea = new JScrollPane(itemList);
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollArea.setPreferredSize(new Dimension(itemListWidth, maxHeight));
		
		// ------
		
		itemsPanels.add(scrollArea, BorderLayout.WEST);
		itemsPanels.add(itemDetails, BorderLayout.EAST);
		
		masterPanel.add(itemsPanels, BorderLayout.WEST);
		masterPanel.add(itemSetPanel, BorderLayout.EAST);

		frame.getContentPane().add(masterPanel);
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
            item.setIcon((images.get("items")).get(item.sprite)); // Set the icon
			items.put(itemId, item);
		}
        
    }
}
