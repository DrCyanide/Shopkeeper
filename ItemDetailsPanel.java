
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.util.Map;

class ItemDetailsPanel extends JPanel{
	JLabel nameAndIcon;
	JEditorPane description;
	JPanel buildIntoPanel, buildFromPanel;
	
	Color backgroundColor = Color.DARK_GRAY;
	Color textColor = new Color(12, 232, 101);
	
	JScrollPane descriptionScroll;
	
	Map<String, Item> items;
	
	public ItemDetailsPanel(Map<String, Item> items){
	    this.items = items;
	    setBackground(backgroundColor);
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		
		buildIntoPanel = new JPanel();
		buildIntoPanel.setBackground(backgroundColor);
		buildIntoPanel.setLayout(new GridLayout(1, 0));
		JScrollPane buildIntoScroll = new JScrollPane(buildIntoPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		nameAndIcon = new JLabel("No Item", SwingConstants.CENTER);
		nameAndIcon.setForeground(textColor);
		nameAndIcon.setHorizontalTextPosition(JLabel.CENTER);
		nameAndIcon.setVerticalTextPosition(JLabel.NORTH);
		
		
		buildFromPanel = new JPanel();
		buildFromPanel.setBackground(backgroundColor);
		buildFromPanel.setLayout(new GridLayout(1, 0));
		
		description = new JEditorPane();
		description.setEditable(false);
		description.setContentType("text/html");
		description.setBackground(backgroundColor);
		
		String htmlTextColor = String.format("body { color: rgb(%d, %d, %d); } unique { font-weight: bold; }", textColor.getRed(), textColor.getGreen(), textColor.getBlue());
		
		((HTMLDocument)description.getDocument()).getStyleSheet().addRule(htmlTextColor);
		//description.setLineWrap(true);
		//description.setWrapStyleWord(true);
		descriptionScroll = new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		descriptionScroll.setPreferredSize(new Dimension(250, 200));
		
		
		add(buildIntoScroll, BorderLayout.NORTH);
		//add(name);
		//add(icon);
		add(nameAndIcon, BorderLayout.CENTER);
		
		//add(scroll);
		add(descriptionScroll, BorderLayout.SOUTH);
		setMaximumSize(new Dimension(250,400));
	}
	
	public void updateItem(Item item){
		nameAndIcon.setText(item.name);
		nameAndIcon.setIcon(new ImageIcon(item.itemicon));
		
		addDescriptionText(item.description.trim());
		// It'd be nice to auto scroll to the top...
		
		buildIntoPanel.removeAll();
		for(String id: item.into){
		    buildIntoPanel.add(new JLabel(new ImageIcon(items.get(id).itemicon)));
		}
	}
	
	private void addDescriptionText(String text){
		description.setText("");
		description.setText("<html><body>" + text + "</body></html>");
	}
}
