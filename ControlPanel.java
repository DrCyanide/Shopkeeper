
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ControlPanel extends JPanel implements ActionListener{
    JButton openButton, saveButton, newButton, editButton;
    Color backgroundColor;
    
    public ControlPanel(Color backgroundColor){
        this.backgroundColor = backgroundColor;
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
            System.out.println("Open!");
        }
    }
    
}
