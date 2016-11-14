
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import javax.swing.*;


class Item{
    
    
    public String id, name, description;
    public String colloq, plaintext;
    public List<String> into, from, tags;
    public int cost_total, cost_combine, cost_sell;
    
    public String sprite;
    public int x,y,w,h; // location within the sprite
    
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
        sprite = (String)image.get("sprite");
        x = ((Double)image.get("x")).intValue();
        y = ((Double)image.get("y")).intValue();
        h = ((Double)image.get("h")).intValue();
        w = ((Double)image.get("w")).intValue();
    }
    
    private BufferedImage cropImage(BufferedImage spriteImage){
        return spriteImage.getSubimage(x, y, w, h);
    }
    
    
    public JPanel getSummaryPanel(BufferedImage spriteImage){
        JPanel panel = new JPanel();
        JLabel icon = new JLabel(new ImageIcon(cropImage(spriteImage)));
        panel.add(icon);
        return panel;
    }

}
