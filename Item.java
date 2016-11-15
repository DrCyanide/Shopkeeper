
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


class Item{
    
    
    public String id, name, description;
    public String colloq, plaintext;
    public List<String> into, from, tags;
    public int cost_total, cost_combine, cost_sell;
    
    public String sprite;
    public int x,y,w,h; // location within the sprite
    
    public BufferedImage itemicon;
    
    public Item(int itemId){
        this(String.format("%04d",itemId)); // call Item's other constructor
    }
    
    public Item(String itemId){
        Map<String, Object> json = convertItemIdToMap(itemId); // call Item's other constructor
        setConstants(json);
    }
    
    public Item(Map<String, Object> json){
        setConstants(json);
    }
    
    
    private Map<String, Object> convertItemIdToMap(String itemId){
        id = itemId;
        // find the region being used
        FileManager fileManager = new FileManager();
        Map<String, String> settings = fileManager.readSettings();
        String regionSlug = settings.get("region");
        
        // Read the item.json file from the region's folder
        String itemJson = new String(fileManager.loadFile(true, "regions/" + regionSlug, "item.json"));
        Gson gson = new Gson();
        Map<String, Object> items = gson.fromJson(itemJson, Map.class);
        
        Map<String, Object> data = (Map<String, Object>)items.get("data");
        return (Map<String, Object>)data.get(itemId);
    }

    private void setConstants(Map<String, Object> json){
        name = (String)json.get("name");
        description = (String)json.get("description");
        colloq = (String)json.get("colloq");
        plaintext = (String)json.get("plaintext");
        
        into = (List)json.get("into");
        from = (List)json.get("from");
        tags = (List)json.get("tags");
        
        Map<String, Object> image = (Map<String, Object>)json.get("image");
        id = (String)image.get("full");
        id = id.substring(0, id.lastIndexOf('.'));
        sprite = (String)image.get("sprite");
        x = ((Double)image.get("x")).intValue();
        y = ((Double)image.get("y")).intValue();
        h = ((Double)image.get("h")).intValue();
        w = ((Double)image.get("w")).intValue();
		
		Map<String, Object> gold = (Map<String, Object>)json.get("gold");
		cost_total = ((Double)gold.get("total")).intValue();
		cost_combine = ((Double)gold.get("base")).intValue();
		cost_sell = ((Double)gold.get("sell")).intValue();
    }
    
    
    public void setIcon(BufferedImage spriteImage){
        itemicon = cropImage(spriteImage);
    }
    
    private BufferedImage cropImage(BufferedImage spriteImage){
        return spriteImage.getSubimage(x, y, w, h);
    }
    
    
    public JPanel getSummaryPanel(){
		int pad = 3;
        JPanel panel = new JPanel();
		panel.setBackground(new Color(153,153,0));
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(pad,pad,pad,pad));
		
        JLabel icon = new JLabel(new ImageIcon(itemicon));
		JLabel gold = new JLabel("" + cost_total, SwingConstants.CENTER);
		
        panel.add(icon, BorderLayout.CENTER);
		panel.add(gold, BorderLayout.SOUTH);
        return panel;
    }

}
