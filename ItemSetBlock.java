
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ItemSetBlock{

    ArrayList<Item> items = new ArrayList<Item>();
    String blockName = "New Block";
    ItemDetailsPanel itemDetails;
    ItemListener itemListener;
    public ItemSetBlock(ItemDetailsPanel itemDetails){
        this.itemDetails = itemDetails;
        itemListener = new ItemListener(itemDetails);
    }

    public void addItem(Item item){
        items.add(item);
    }
    
    public void removeItem(int index){
        
    }
    
    public JPanel renderAsPanel(){
        JPanel panel = new JPanel();
        
        if(items.size() == 0){
            JLabel text = new JLabel("No items added");
            panel.add(text);
        }else{
            panel.setLayout(new GridLayout());
            for(Item item : items){
                panel.add(RenderItem.RenderItem(item, itemListener));
            }
        }
        return panel;
    }
}
