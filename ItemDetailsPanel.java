
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.util.Map;

class ItemDetailsPanel extends JPanel{
	JLabel nameAndIcon;
	JEditorPane description;
	JPanel buildIntoPanel, buildFromPanel;
	
	Color backgroundColor;
	Color textColor = new Color(12, 232, 101);
	
	JScrollPane descriptionScroll;
	
	Map<String, Item> items;
	String currentItemId = "";
	
	int maxWidth, maxHeight;
	
	ItemListener itemListener;
	
	public ItemDetailsPanel(Map<String, Item> items, int maxWidth, int maxHeight, Color backgroundColor){
	    this.items = items;
	    this.maxWidth = maxWidth;
	    this.maxHeight = maxHeight;
	    this.backgroundColor = backgroundColor;
	    
	    itemListener = new ItemListener(this);
	    setBackground(backgroundColor);
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(maxWidth, maxHeight));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3,1));
		topPanel.setOpaque(false);
		topPanel.setPreferredSize(new Dimension(maxWidth, maxHeight/2));
		
		buildIntoPanel = new JPanel();
		buildIntoPanel.setBackground(backgroundColor);
		buildIntoPanel.setLayout(new GridLayout(1, 0));
		JScrollPane buildIntoScroll = new JScrollPane(buildIntoPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		buildIntoScroll.setPreferredSize(new Dimension(maxWidth, 60));
		
		nameAndIcon = new JLabel("No Item", SwingConstants.CENTER);
		nameAndIcon.setForeground(textColor);
		nameAndIcon.setHorizontalTextPosition(JLabel.CENTER);
		nameAndIcon.setVerticalTextPosition(JLabel.NORTH);
		
		
		buildFromPanel = new JPanel();
		buildFromPanel.setBackground(backgroundColor);
		buildFromPanel.setForeground(textColor);
		buildFromPanel.setLayout(new GridLayout(1, 0));
		JScrollPane buildFromScroll = new JScrollPane(buildFromPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		buildFromScroll.setPreferredSize(new Dimension(maxWidth, 60));
		
		description = new JEditorPane();
		description.setEditable(false);
		description.setContentType("text/html");
		description.setBackground(backgroundColor);
		
		String htmlTextColor = String.format("body { color: rgb(%d, %d, %d); } unique { font-weight: bold; }", textColor.getRed(), textColor.getGreen(), textColor.getBlue());
		
		((HTMLDocument)description.getDocument()).getStyleSheet().addRule(htmlTextColor);
		descriptionScroll = new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		descriptionScroll.setPreferredSize(new Dimension(maxWidth, 200));
		
		
		topPanel.add(buildIntoScroll);
		topPanel.add(nameAndIcon);
		topPanel.add(buildFromScroll);
		
		add(topPanel, BorderLayout.CENTER);
		//add(buildIntoScroll, BorderLayout.NORTH);
		//add(nameAndIcon, BorderLayout.CENTER);
		
		add(descriptionScroll, BorderLayout.SOUTH);
		setMaximumSize(new Dimension(maxWidth,400));
	}
	
	public void updateItem(String id){
	    Item item = items.get(id);
		nameAndIcon.setText(item.name);
		nameAndIcon.setIcon(new ImageIcon(item.itemicon));
		
		addDescriptionText(item.description.trim());

		buildIntoPanel.removeAll();
		buildIntoPanel.repaint();
		buildFromPanel.removeAll();
		buildFromPanel.repaint();
		if(item.into != null){
		    for(String toId: item.into){
		        JPanel itemPanel = RenderItem.RenderItemIconOnly(items.get(toId), itemListener);
		        if (itemPanel != null){
		            buildIntoPanel.add(itemPanel);
		        }
		    }
		}
		if(item.from != null){
		    for(String fromId: item.from){
		        JPanel itemPanel = RenderItem.RenderItemIconOnly(items.get(fromId), itemListener);
		        if (itemPanel != null){
		            buildFromPanel.add(itemPanel);
		            JLabel plus = new JLabel("+", JLabel.CENTER);
		            plus.setForeground(textColor);
		            buildFromPanel.add(plus);
		        }
		    }
		}
		JLabel combineCost = new JLabel("" + item.cost_combine + "g", JLabel.CENTER);
		combineCost.setForeground(textColor);
		buildFromPanel.add(combineCost);
	}
	
	private void addDescriptionText(String text){
		description.setText("");
		description.setText("<html><body>" + text + "</body></html>");
		description.setCaretPosition(0);
	}
}
