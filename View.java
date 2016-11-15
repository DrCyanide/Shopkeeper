
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class View implements MouseListener{
    Map<String, Map<String, BufferedImage>> images;
	
	Map<String, Item> items;
	
	JPanel itemList;
	ItemDetailsPanel itemDetails;
	JFrame frame;
    public View(){
        frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(700,450));
        
		loadBufferedImages();
		loadItems();
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		
		JPanel itemsPanels = new JPanel();
		itemsPanels.setLayout(new BorderLayout());
		
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
		itemDetails = new ItemDetailsPanel();
		
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
		
		panel.addMouseListener(this);
		
		// new JPanel to pad the created one in the grid layout
		JPanel output = new JPanel();
		output.add(panel);
		
        return output; 
	}
	
   
	// ===============
	// Mouse Events
	// ===============
	private String getItemId(MouseEvent e){
		if(e.getComponent().getClass().getName() == "javax.swing.JPanel"){
			for(Component c: ((JPanel)e.getComponent()).getComponents()){
				if(!c.isVisible() && c.getClass().getName() == "javax.swing.JLabel"){
					String text = ((JLabel)c).getText();
					if(text.contains("itemId:")){
						return text.substring(text.indexOf(":")+1,text.length());
					}
				}
			}
		}
		return "";
	}
	
	public void mouseClicked(MouseEvent e){
		//System.out.println("Clicked!");
		String id = getItemId(e);
		itemDetails.updateItem(items.get(id));
	}
	public void mousePressed(MouseEvent e){
		//System.out.println("Pressed");
	}
	public void mouseReleased(MouseEvent e){
		//System.out.println("Released");
	}
	public void mouseEntered(MouseEvent e){
		//System.out.println("Entered");
	}
	public void mouseExited(MouseEvent e){
		//System.out.println("Exited");
	}
   
}
