
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ControlPanel extends JPanel implements ActionListener{
    JButton openButton, saveButton, newButton, editButton;
    Color backgroundColor;
    View viewPanel;
    
    public ControlPanel(Color backgroundColor, View viewPanel){
        this.backgroundColor = backgroundColor;
        this.viewPanel = viewPanel;
        setBackground(backgroundColor);
        
        openButton = new JButton("Open");
        openButton.addActionListener(this);
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        
        newButton = new JButton("New");
        newButton.addActionListener(this);
        
        editButton = new JButton("Edit");
        editButton.addActionListener(this);
        
        add(newButton);
        add(openButton);
        add(saveButton);
        add(editButton);
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == openButton){
            System.out.println("Open! - ... show list of already built item sets");
            
        }
        else if(e.getSource() == editButton){
            System.out.println("Edit! - ... open a dialog showing champions this set is for");
        }
        else if(e.getSource() == saveButton){
            System.out.println("Save! - ... save the item set");
            Save();
        }
        else if(e.getSource() == newButton){
            System.out.println("New! - ... create a new item set (prompt before nuking everything)");
        }
    }
    
    
    private void Save(){
        // Convert to String
		String baseIndent = "  ";
        String jsonString = "{\n";
        jsonString += baseIndent + pair("title", viewPanel.itemSetPanel.setName, true);
        jsonString += baseIndent +pair("type", "custom", true);
        jsonString += baseIndent +pair("map", "any", true);
        jsonString += baseIndent +pair("mode", "any", true);
        //jsonString += baseIndent +pair("priority", false, true); // Optional
        //jsonString += baseIndent +pair("sortrank", 0, true); // Optional
        
        jsonString += baseIndent + "\"blocks\": [\n";
        jsonString += getBlocks();
        jsonString += "\n" + baseIndent + "]\n}";
        System.out.println(jsonString);
		
		// Save that string according to the settings
    }
    
    private String pair(String key, Object value, Boolean comma){
        String response = "\"" + key + "\": ";
        if(value.getClass().toString().contains("String")){
            response +=  "\"" + value + "\"";
        } else {
            response += "" + value;
        }
        
        if(comma){
            response += ",";
        }
        response += "\n";
        return response;
    }
    
    private String getBlocks(){
        String allBlocks = "";
        ArrayList<ItemSetBlock> blocks = viewPanel.itemSetPanel.itemBlocks;
		String blockIndent = "    ";
		String blockContentIndent = "      ";
		String itemIndent = "        ";
		String itemContentIndent = "          ";
        for(int i = 0; i < blocks.size(); i++){
            if(i > 0){
                allBlocks += ",\n";
            }
            ItemSetBlock block = blocks.get(i);
            String blockString = blockIndent + "{\n";
			
            blockString += blockContentIndent + pair("type", block.blockName, true);
            blockString += blockContentIndent + "\"items\": [\n";
            
            for(int j = 0; j < block.items.size(); j++){
                if(j > 0){
                    blockString += ",\n";
                }
                blockString += itemIndent + "{\n";
                Item item = block.items.get(j);
                blockString += itemContentIndent + pair("id",item.id, true);
                blockString += itemContentIndent + pair("count", 1, false);
                blockString += itemIndent + "}";
            }
            
            blockString += "\n" + blockContentIndent + "]\n";
            allBlocks += blockString + blockIndent + "}";
        }
        return allBlocks;
    }
    
}
