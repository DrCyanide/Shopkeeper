
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class View{
	Map<String, BufferedImage> champions;
	Map<String, BufferedImage> items;
	Map<String, BufferedImage> summoners;
	
    public View(){
        JFrame frame = new JFrame("Shopkeeper - Item Sets for League of Legends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		loadBufferedImages();
        
        JPanel allContent = new JPanel();
        
        Item item = new Item("3751");
        
		allContent.add(item.getSummaryPanel(items.get(item.sprite)));
        
        frame.getContentPane().add(allContent);
        frame.pack();
        frame.setVisible(true);
    }
	
	private void loadBufferedImages(){
		FileManager fileManager = new FileManager();
		champions = fileManager.loadImages("champion");
		items = fileManager.loadImages("item");
		summoners = fileManager.loadImages("summoner");
	}
}
