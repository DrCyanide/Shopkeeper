
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class ItemListPanel extends JPanel{

    Map<String, Item> items;
    Color backgroundColor;
    int maxWidth, maxHeight;
    int itemListCol = 4;
	ItemListener itemListener;
	
    public ItemListPanel(Map<String, Item> items, Color backgroundColor){
        this.items = items;
	    this.maxWidth = maxWidth;
	    this.maxHeight = maxHeight;
	    this.backgroundColor = backgroundColor;
	    
	    setLayout(new GridLayout(0, itemListCol));
	    setBackground(backgroundColor);
		//setPreferredSize(new Dimension(maxWidth, maxHeight));
    }
    
    public void addItemListener(ItemListener itemListener){
        this.itemListener = itemListener;
    }
    
    public void filterItems(String substring){
        if(substring == null || substring.isEmpty()){
            populateItems(items);
            return;
        }
        substring = substring.toLowerCase();
    
        Map<String, Item> filteredItems = new HashMap<String, Item>();
        
        
        Iterator it = items.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Item item = (Item)pair.getValue();
            String itemName = item.name.toLowerCase();
            String itemColloq = item.colloq.toLowerCase();
            //System.out.println("Testing " + itemName + ": " + itemName.contains(substring));
            if(itemName.contains(substring) || itemColloq.contains(substring)){
                //System.out.println("Adding item " + pair.getKey());
                filteredItems.put((String)pair.getKey(), item);
            }
        }
        populateItems(filteredItems);
    }
    
    public void populateItems(){
        populateItems(items);
    }
    
    public void populateItems(Map<String, Item> filteredItems){
        removeAll();
        ArrayList<Item> itemList = new ArrayList<Item>(filteredItems.values());
        Collections.sort(itemList);
        for(int i=0; i < itemList.size(); i++){
            add(RenderItem.RenderItem(itemList.get(i), itemListener));
        }
        
        revalidate();
        repaint();
    }
}
