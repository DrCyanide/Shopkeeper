
import javax.swing.*;
import java.awt.*;

class ItemDetailsPanel extends JPanel{
	JLabel name, icon;
	JEditorPane description;
	public ItemDetailsPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		name = new JLabel("No Item");
		icon = new JLabel();
		
		description = new JEditorPane();
		description.setEditable(false);
		//description.setLineWrap(true);
		//description.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(200, 200));
		
		add(name);
		add(icon);
		//add(description);
		add(scroll);
		setMaximumSize(new Dimension(150,400));
	}
	
	public void updateItem(Item item){
		name.setText(item.name);
		icon.setIcon(new ImageIcon(item.itemicon));
		
		addDescriptionText(item.description.trim());
	}
	
	private void addDescriptionText(String text){
		description.setText("");
		//description.setText("<html><body>" + text + "</body></html>");
		description.setText(cleanText(text));
	}
	
	private String cleanText(String text){
		text = text.replace("<br>","\n");
		text = text.replace("<stats>","Stats:\n");
		text = text.replace("</stats>","");
		text = text.replace("<unique>","");
		text = text.replace("</unique>","");
		text = text.replace("<active>","");
		text = text.replace("</active>","");
		text = text.replace("<mana>","");
		text = text.replace("</mana>","");
		text = text.replace("<groupLimit>","");
		text = text.replace("</groupLimit>","");
		text = text.replace("<rules>","");
		text = text.replace("</rules>","");
		text = text.replace("<passive>","");
		text = text.replace("</passive>","");
		text = text.replace("<font color=","");
		text = text.replace("</font>","");
		
		return text;
	}
}