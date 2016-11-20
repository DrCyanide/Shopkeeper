
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ItemSetPanel extends JPanel{
    JPanel controls, main;
    ArrayList<ItemSetBlock> itemBlocks = new ArrayList<ItemSetBlock>();
    ItemListener itemListener;
    
    int maxWidth, maxHeight;
    Color backgroundColor;
    
    public ItemSetPanel(ItemListener itemListener, int maxWidth, int maxHeight, Color backgroundColor){
        this.itemListener = itemListener;
        this.maxWidth = maxWidth;
	    this.maxHeight = maxHeight;
	    this.backgroundColor = backgroundColor;
	    
        controls = new JPanel();
        main = new JPanel();
        
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(maxWidth, maxHeight));
        
        
        add(controls, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }
    
    public void addBlock(){
        itemBlocks.add(new ItemSetBlock(itemListener));
    }
    
    public void removeBlock(int index){
        itemBlocks.remove(index);
    }
    
    public ItemSetBlock getBlock(int index){
        return itemBlocks.get(index);
    }
}
