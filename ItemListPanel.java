
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Map;
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
    
    public void populateItems(){
        removeAll();
        repaint();
        ArrayList<Item> itemList = new ArrayList<Item>(items.values());
        Collections.sort(itemList);
        //for(Map.Entry<String, Item> item : items.entrySet()){
        //    add(RenderItem.RenderItem(item.getValue(), itemListener));
        //}
        for(int i=0; i < itemList.size(); i++){
            add(RenderItem.RenderItem(itemList.get(i), itemListener));
        }
    }
}
