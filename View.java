
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;

class View{
    Map<String, Map<String, BufferedImage>> images;
	Map<String, Item> items;
	
	ItemListPanel itemList;
	ItemDetailsPanel itemDetails;
	ItemListener itemListener;
	ItemSetPanel itemSetPanel;
	ControlPanel controlPanel;

	JTextField itemFilterField;
	
	JPanel farRight;
	JFrame frame;
	
	int itemListCol = 4;
	int itemListWidth = 325;
	
	int itemDetailsWidth = 285;
	
	int itemSetsWidth = 400;
	
	int maxWidth = itemListWidth + itemDetailsWidth + itemSetsWidth + 10;
	int maxHeight = 450;
	
	Color backgroundColor = Color.DARK_GRAY;
	
	JDialog updatingDialog;
	
	String updateText = "Checking for updates...";
	
	public void displayUpdateDialog(){
		updatingDialog = new JDialog();
		updatingDialog.setTitle("Shopkeeper - Updating...");
		updatingDialog.setModal(true);
		updatingDialog.setContentPane(new JOptionPane(updateText, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null));
		updatingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		updatingDialog.pack();
		
		updatingDialog.setVisible(true);
	}
	
	public void closeUpdateDialog(){
		updatingDialog.dispose();
	}
	
	
    public View(String[] supportedServers){		
        frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialize setup
		FileManager fileManager = new FileManager();
		String region = fileManager.readSettings().get("region");
		UpdateManager updateManager = new UpdateManager();
		updateManager.setView(this);
		
		if(region == null || region.isEmpty()){
			updateText += "\nThe first time is the longest.";
			String server = (String)JOptionPane.showInputDialog(frame, "<html><body><p style='width: 200px;'>Select your server.<br/>Used to get item descriptions in server's default language.</p></body></html>", "Shopkeeper - Select Server", JOptionPane.PLAIN_MESSAGE, null, supportedServers, "NA");
			if(server == null){
				server = "NA";
			}
			region = server.toLowerCase();
		}
		
		updateManager.loadRegion(region);
		
		frame.setPreferredSize(new Dimension(maxWidth,maxHeight));
		frame.setBackground(backgroundColor);
        
		loadBufferedImages();
		loadItems();
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		masterPanel.setBackground(backgroundColor);
		
		// ------
		
		JPanel itemsPanels = new JPanel();
		itemsPanels.setLayout(new BorderLayout());
		
		itemDetails = new ItemDetailsPanel(items, itemDetailsWidth, maxHeight, backgroundColor);
		itemListener = new ItemListener(itemDetails, items);
		itemDetails.addItemListener(itemListener);
		
        itemList = new ItemListPanel(items, backgroundColor);
        itemList.addItemListener(itemListener);
        itemList.populateItems();
        
        JPanel itemListWrapper = new JPanel();
        itemListWrapper.setBackground(backgroundColor);
        itemListWrapper.add(itemList);
        
		JScrollPane scrollArea = new JScrollPane(itemListWrapper);
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollArea.setPreferredSize(new Dimension(itemListWidth, maxHeight));
		
		farRight = new JPanel();
		farRight.setLayout(new BorderLayout());
		
		controlPanel = new ControlPanel(backgroundColor, this);
		itemSetPanel = new ItemSetPanel(itemListener, itemSetsWidth, maxHeight, backgroundColor);
		
		farRight.add(itemSetPanel, BorderLayout.CENTER);
		farRight.add(controlPanel, BorderLayout.NORTH);
		
		JPanel itemFilterPanel = new JPanel();
		//itemFilterPanel.setBackground(backgroundColor);
		itemFilterPanel.setLayout(new BorderLayout());
		
		
		itemFilterField = new JTextField();
		itemFilterField.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }
            public void textChanged(){
                itemList.filterItems(itemFilterField.getText());
            }
		});
		
		itemFilterPanel.add(new JLabel("Search"), BorderLayout.WEST);// Add search icon
		itemFilterPanel.add(itemFilterField, BorderLayout.CENTER);
		
		
		// ------
		
		JPanel itemFiltersAndList = new JPanel();
		itemFiltersAndList.setLayout(new BorderLayout());
		itemFiltersAndList.setBackground(backgroundColor);
		
		itemFiltersAndList.add(scrollArea, BorderLayout.CENTER);
		itemFiltersAndList.add(itemFilterPanel, BorderLayout.NORTH);
		
		itemsPanels.add(itemFiltersAndList, BorderLayout.WEST);
		itemsPanels.add(itemDetails, BorderLayout.EAST);
		
		masterPanel.add(itemsPanels, BorderLayout.WEST);
		masterPanel.add(farRight, BorderLayout.EAST);

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
			BufferedImage spritePage = (images.get("items")).get(item.sprite);
			if(spritePage == null){
				// re-request it
				System.out.println("Null Image");
			}
            item.setIcon(spritePage); // Set the icon
			items.put(itemId, item);
		}
        
    }
}
