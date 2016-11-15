
import javax.swing.*;
import java.awt.*;

class ItemDetailsPanel extends JPanel{
	JLabel name, icon;
	JEditorPane description;
	public ItemDetailsPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		name = new JLabel("No Item", SwingConstants.CENTER);
		icon = new JLabel("", SwingConstants.CENTER);
		
		description = new JEditorPane();
		description.setEditable(false);
		description.setContentType("text/html");
		description.setBackground(Color.GRAY);
		//description.setLineWrap(true);
		//description.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(250, 200));
		
		add(name);
		add(icon);
		//add(description);
		add(scroll);
		setMaximumSize(new Dimension(250,400));
	}
	
	public void updateItem(Item item){
		name.setText(item.name);
		icon.setIcon(new ImageIcon(item.itemicon));
		
		addDescriptionText(item.description.trim());
	}
	
	private void addDescriptionText(String text){
		description.setText("");
		description.setText("<html><body>" + text + "</body></html>");
	}
}