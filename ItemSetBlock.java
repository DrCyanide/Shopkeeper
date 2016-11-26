
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ItemSetBlock implements ActionListener{

    ArrayList<Item> items = new ArrayList<Item>();
    String blockName = "New Block";
    ItemListener itemListener;
    JButton addButton, renameButton;
    JLabel nameLabel;
    JPanel itemPanel;
    
    Color titleBarColor = Color.GRAY;
    
    public ItemSetBlock(ItemListener itemListener){
        this.itemListener = itemListener;
    }

    public void addItem(Item item){
        items.add(item);
        redrawItemPanel();
    }
    
    public void removeItem(int index){
        items.remove(index);
    }
    
    public void changeName(String blockName){
        this.blockName = blockName;
        nameLabel.setText(this.blockName);
    }
    
    public void redrawItemPanel(){
        itemPanel.removeAll();
        itemPanel.repaint();
        if(items.size() > 0){
            itemPanel.setLayout(new GridLayout(0,5));
            for(Item item : items){
                itemPanel.add(RenderItem.RenderItem(item, itemListener));
            }
        }
        // add the "add" button
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        itemPanel.add(addButton);
        itemPanel.revalidate();
        itemPanel.repaint();
    }
    
    public JPanel renderAsPanel(){
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        //outerPanel.setPreferredSize(new Dimension(100,100));
        
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(titleBarColor);
        
        nameLabel = new JLabel(blockName);
        upperPanel.add(nameLabel, BorderLayout.WEST);
        
        renameButton = new JButton("Rename");
        renameButton.addActionListener(this);
        
        upperPanel.add(renameButton, BorderLayout.EAST);
        outerPanel.add(upperPanel, BorderLayout.NORTH);
        
        itemPanel = new JPanel();
        redrawItemPanel();
        
        outerPanel.add(itemPanel, BorderLayout.CENTER);
        return outerPanel;
    }
    
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == renameButton){
            String name = (String)JOptionPane.showInputDialog("New block name", null);
            if(name != null && name.length() > 0){
                changeName(name);
            }
        }
        
        if(e.getSource() == addButton){
            // look in the item set manager
            Item item = itemListener.getLastItem();
            if(item != null){
                addItem(item);
            }
        }
    }
}
