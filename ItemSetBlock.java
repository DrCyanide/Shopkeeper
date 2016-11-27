
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

class ItemSetBlock implements ActionListener{

    ArrayList<Item> items = new ArrayList<Item>();
    String blockName = "New Block";
    ItemListener itemListener;
    JButton addButton, removeButton, renameButton, deleteButton;
    JLabel nameLabel;
    JPanel itemPanel;
    ItemSetPanel parent;
    
    Color titleBarColor = Color.GRAY;
    
    public ItemSetBlock(ItemListener itemListener, ItemSetPanel parent){
        this.itemListener = itemListener;
        this.parent = parent;
        
        renameButton = makeButton("Rename", "img/edit.png");
        deleteButton = makeButton("Delete", "img/delete.png");
        addButton = makeButton("Add", "img/plus.png");
        removeButton = makeButton("Remove", "img/minus.png");
    }
    
    private JButton makeButton(String backupName, String imagePath){
        JButton button;
        try{
            BufferedImage icon = ImageIO.read(new File(imagePath));
            button = new JButton(new ImageIcon(icon));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            //button.setBackground(new Color(0,0,0,0));
            button.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    try{
                    JButton temp = (JButton)evt.getComponent();
                    temp.setContentAreaFilled(true);
                    temp.setBackground(Color.GREEN);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    try{
                    JButton temp = (JButton)evt.getComponent();
                    temp.setContentAreaFilled(false);
                    temp.setBackground(new Color(0,0,0,0));
                    
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
        } catch (Exception e){
            button = new JButton(backupName);
        }
        button.addActionListener(this);
        return button;
    }
    

    public void addItem(Item item){
        items.add(item);
        redrawItemPanel();
    }
    
    public void removeItem(Item item){
        if(items.contains(item)){
            removeItem(items.lastIndexOf(item));
        } 
    }
    
    public void removeItem(int index){
        items.remove(index);
        redrawItemPanel();
    }
    
    public void changeName(String blockName){
        this.blockName = blockName;
        nameLabel.setText(this.blockName);
    }
    
    public void redrawItemPanel(){
        itemPanel.removeAll();
        itemPanel.setLayout(new GridLayout(0,5));
        if(items.size() > 0){
            for(Item item : items){
                itemPanel.add(RenderItem.RenderItem(item, itemListener));
            }
        }
        itemPanel.add(RenderItem.wrapButton(addButton));
        itemPanel.add(RenderItem.wrapButton(removeButton));
        
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
        
        JPanel nameButtons = new JPanel();
        nameButtons.setBackground(titleBarColor);
        nameButtons.add(renameButton);
        nameButtons.add(deleteButton);
        
        upperPanel.add(nameButtons, BorderLayout.EAST);
        outerPanel.add(upperPanel, BorderLayout.NORTH);
        
        itemPanel = new JPanel();
        redrawItemPanel();
        
        outerPanel.add(itemPanel, BorderLayout.CENTER);
        return outerPanel;
    }
    
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == renameButton){
            String name = (String)JOptionPane.showInputDialog("New block name", blockName);
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
        
        if(e.getSource() == removeButton){
            // look in the item set manager
            Item item = itemListener.getLastItem();
            if(item != null){
                removeItem(item);
            }
        }
        
        if(e.getSource() == deleteButton){
            parent.removeBlock(this);
        }
    }
}
