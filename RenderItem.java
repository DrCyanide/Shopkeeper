
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

class RenderItem{
    static JPanel RenderItem(Item item, MouseListener listener){
        if(item == null){
            return null;
        }
        int pad = 3;
        JPanel panel = new JPanel();
        panel.setBackground(new Color(153,153,0));
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(pad,pad,pad,pad));
        
        JPanel icon = identifiedImage(item);
        JLabel gold = new JLabel("" + item.cost_total, SwingConstants.CENTER);
        
        panel.add(icon, BorderLayout.CENTER);
		panel.add(gold, BorderLayout.SOUTH);
        
        panel.addMouseListener(listener);
        // new JPanel to pad the created one in the grid layout
		JPanel output = new JPanel();
		output.add(panel);
        return output;
    }
    
    
    static JPanel RenderItemIconOnly(Item item, MouseListener listener){
        if(item == null){
            return null;
        }
        JPanel panel = identifiedImage(item);
        panel.addMouseListener(listener);
        return panel;
    }
    
    
    private static JPanel identifiedImage(Item item){
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0,0,0,0));
        panel.setOpaque(false);
        JLabel iconLabel = new JLabel(new ImageIcon(item.itemicon));
        JLabel idLabel = new JLabel("itemId:"+item.id);
		idLabel.setVisible(false);
		
		panel.add(iconLabel);
		panel.add(idLabel);
        return panel;
    }
}
