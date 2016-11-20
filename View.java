
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
	JFrame frame;
	
	int maxWidth = 700;
	int maxHeight = 450;
    public View(){
        frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(maxWidth,maxHeight));
        
		loadBufferedImages();
		loadItems();
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		
		JPanel itemsPanels = new JPanel();
		itemsPanels.setLayout(new BorderLayout());
		
		itemDetails = new ItemDetailsPanel(items);
		itemListener = new ItemListener(itemDetails);
		
		// ------
        itemList = new JPanel();
        itemList.setLayout(new GridLayout(0, 5));
        
        for(Map.Entry<String, Item> item : items.entrySet()){
            itemList.add(getItemSummaryPanel(item.getValue()));
        }
        
		JScrollPane scrollArea = new JScrollPane(itemList);
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollArea.setPreferredSize(new Dimension(400, 400));
		
		// ------
		
		
		
		itemsPanels.add(scrollArea, BorderLayout.WEST);
		itemsPanels.add(itemDetails, BorderLayout.EAST);
		
		masterPanel.add(itemsPanels, BorderLayout.WEST);

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
	
	private JPanel getItemSummaryPanel(Item item){
		int pad = 3;
        JPanel panel = new JPanel();
		panel.setBackground(new Color(153,153,0));
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(pad,pad,pad,pad));
		
        JLabel icon = new JLabel(new ImageIcon(item.itemicon, item.name));
		JLabel gold = new JLabel("" + item.cost_total, SwingConstants.CENTER);
		JLabel itemId = new JLabel("itemId:"+item.id);
		itemId.setVisible(false);
		
        panel.add(icon, BorderLayout.CENTER);
		panel.add(gold, BorderLayout.SOUTH);
		panel.add(itemId, BorderLayout.NORTH);
		
		panel.addMouseListener(itemListener);
		
		// new JPanel to pad the created one in the grid layout
		JPanel output = new JPanel();
		output.add(panel);
		
        return output; 
	}
}
