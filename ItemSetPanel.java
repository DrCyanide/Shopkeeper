
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ItemSetPanel extends JPanel{
    JPanel main;
    ArrayList<ItemSetBlock> itemBlocks = new ArrayList<ItemSetBlock>();
    ItemListener itemListener;
    
    int maxWidth, maxHeight;
    Color backgroundColor;
    
    public ItemSetPanel(ItemListener itemListener, int maxWidth, int maxHeight, Color backgroundColor){
        this.itemListener = itemListener;
        this.maxWidth = maxWidth;
	    this.maxHeight = maxHeight;
	    this.backgroundColor = backgroundColor;
	    

        main = new JPanel();
        main.setLayout(new GridLayout(0,1));
        
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(maxWidth, maxHeight));
        
        JScrollPane scrollArea = new JScrollPane(main);
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setPreferredSize(new Dimension(maxWidth, maxHeight));
        
        add(scrollArea, BorderLayout.CENTER);
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        addBlock();
        
    }
    
    public void addBlock(){
        itemBlocks.add(new ItemSetBlock(itemListener));
        redrawBlocks();
    }
    
    public void removeBlock(int index){
        itemBlocks.remove(index);
        // if length of itemBlocks = 0, re-add an empty block
        redrawBlocks();
    }
    
    public ItemSetBlock getBlock(int index){
        return itemBlocks.get(index);
    }

    public void redrawBlocks(){
        // get scroll location to resume from there
        main.removeAll();
        main.repaint();
        for(int i=0; i<itemBlocks.size(); i++){
            main.add(itemBlocks.get(i).renderAsPanel());
        }
    }
}
