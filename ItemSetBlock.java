
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ItemSetBlock{

    ArrayList<Item> items = new ArrayList<Item>();
    String blockName = "New Block";
    ItemListener itemListener;
    public ItemSetBlock(ItemListener itemListener){
        this.itemListener = itemListener;
    }

    public void addItem(Item item){
        items.add(item);
    }
    
    public void removeItem(int index){
        items.remove(index);
    }
    
    public void changeName(String name){
        blockName = name;
    }
    
    public JPanel renderAsPanel(){
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());
        
        JLabel nameLabel = new JLabel(blockName);
        outerPanel.add(nameLabel, BorderLayout.NORTH);
        
        JPanel itemPanel = new JPanel();
        if(items.size() == 0){this.itemListener = itemListener;
            JLabel text = new JLabel("No items added");
            itemPanel.add(text);
        }else{
            itemPanel.setLayout(new GridLayout(0,5));
            for(Item item : items){
                itemPanel.add(RenderItem.RenderItem(item, itemListener));
            }
        }
        
        outerPanel.add(itemPanel, BorderLayout.CENTER);
        return outerPanel;
    }
}
