
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

class RenderItem{
    
    private static int pad = 3;
    private static Color backgroundColor = new Color(153,153,0);

    static JPanel RenderItem(Item item, MouseListener listener){
        if(item == null){
            return null;
        }
        /*
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(pad,pad,pad,pad));
        */
        JPanel icon = identifiedImage(item);
        return wrapContent(icon, ""+item.cost_total, backgroundColor, listener);
        /*
        JLabel gold = new JLabel("" + item.cost_total, SwingConstants.CENTER);
        
        panel.add(icon, BorderLayout.CENTER);
		panel.add(gold, BorderLayout.SOUTH);
        
        panel.addMouseListener(listener);
        // new JPanel to pad the created one in the grid layout
		JPanel output = new JPanel();
		output.add(panel);
        return output;*/
    }
    
    
    static JPanel wrapButton(JButton button){
        // Make the JButton the same size as the RenderItem output 
        JPanel panel = new JPanel();
        panel.add(button);
        return wrapContent(panel, " ", null, null);
    }
    
    private static JPanel wrapContent(Component toWrap, String labelText, Color color, MouseListener listener){
        JPanel output = new JPanel();
        
        JPanel panel = new JPanel();
        if(color != null){
            panel.setBackground(color);
        }
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(pad,pad,pad,pad));
		
		panel.add(toWrap, BorderLayout.CENTER);
		panel.add(new JLabel(labelText, SwingConstants.CENTER), BorderLayout.SOUTH);
        
        if(listener != null){
            panel.addMouseListener(listener);
        }
        
        output.add(panel);
        output.setOpaque(false);
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
