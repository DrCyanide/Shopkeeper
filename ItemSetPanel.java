
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

class ItemSetPanel extends JPanel{
    String setName = "New Item Set";
    JLabel nameLabel;
    JPanel main;
    ArrayList<ItemSetBlock> itemBlocks = new ArrayList<ItemSetBlock>();
    ItemListener itemListener;
    JButton addBlockButton;
    
    Color backgroundColor;
    Color setNameColor = new Color(12, 232, 101);
    
    public ItemSetPanel(ItemListener itemListener, Color backgroundColor){
        this.itemListener = itemListener;
	    this.backgroundColor = backgroundColor;
	    
        addBlockButton = makeButton("Add Block", "img/plus.png");
        addBlockButton.addActionListener(e -> addBlock());
        
        
        JButton renameButton = makeButton("Rename", "img/rename.png");
        renameButton.addActionListener(e -> {
            String name = (String)JOptionPane.showInputDialog("New item set name", setName);
            if(name != null && name.length() > 0){
                setName = name;
                nameLabel.setText(name);
            }
        });

        main = new JPanel();
        main.setLayout(new GridLayout(0,1));
        
        
        JPanel mainWrapper = new JPanel();
        mainWrapper.add(main);
        
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        
        JScrollPane scrollArea = new JScrollPane(mainWrapper);
        scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        
        nameLabel = new JLabel(setName);
        //nameLabel.setForeground(setNameColor);
        Font font = nameLabel.getFont();
        nameLabel.setFont(new Font(font.getName(), font.getStyle(), font.getSize() + 6));
        
        upperPanel.add(nameLabel, BorderLayout.WEST);
        upperPanel.add(renameButton, BorderLayout.EAST);
        
        add(upperPanel, BorderLayout.NORTH);
        add(scrollArea, BorderLayout.CENTER);
        addBlock();
        
    }
    
	public void clearAll(){
		itemBlocks.clear();
		addBlock();
		setName = "New Item Set";
		nameLabel.setText(setName);
	}
	
    public void addBlock(){
        itemBlocks.add(new ItemSetBlock(itemListener, this));
        redrawBlocks();
    }
    
    public void removeBlock(ItemSetBlock block){
        if(itemBlocks.contains(block)){
            removeBlock(itemBlocks.indexOf(block));
        }
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
        for(int i=0; i<itemBlocks.size(); i++){
            main.add(itemBlocks.get(i).renderAsPanel());
        }
        // Draw "add" button
        main.add(addBlockButton);
        
        main.revalidate();
        main.repaint();
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
        return button;
    }
}
