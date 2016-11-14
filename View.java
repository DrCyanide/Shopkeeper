
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class View{
    public View(){
        JFrame frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        JPanel allContent = new JPanel();
        
        Item item = new Item("3751");
        //allContent.add();
        
        frame.getContentPane().add(allContent);
        frame.pack();
        frame.setVisible(true);
    }
}
