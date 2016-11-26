
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

class ItemListener implements MouseListener{

    ItemDetailsPanel itemDetails;
    String lastFoundId = "";
    Map<String, Item> items;
    
    public ItemListener(ItemDetailsPanel itemDetails, Map<String, Item> items){
        this.itemDetails = itemDetails;
        this.items = items;
    }

    private String getItemId(MouseEvent e){
        // (panel-panel-label) or (panel-label), depending on how it was made
        Component currentComponent = e.getComponent();  
		
		return searchPanelForId(currentComponent);
	}
	
	private String searchPanelForId(Component e){
	    for(Component c: ((JPanel)e).getComponents()){
			if(!c.isVisible() && c.getClass().getName() == "javax.swing.JLabel"){
				String text = ((JLabel)c).getText();
				if(text.contains("itemId:")){
				    lastFoundId = text.substring(text.indexOf(":")+1,text.length());
					return lastFoundId;
				}
			} else if (c.getClass().getName() == "javax.swing.JPanel"){
			    String result = searchPanelForId(c);
			    if(!result.equals("")){
			        return result;
			    }
			}
		}
		return "";
	}
	
	public Item getLastItem(){
	    if(lastFoundId.length() > 0){
	        return items.get(lastFoundId);
	    }else{
	        return null;
	    }
	}

    public void mouseClicked(MouseEvent e){
		//System.out.println("Clicked!");
		lastFoundId = getItemId(e);
		itemDetails.updateItem(lastFoundId);
	}
	public void mousePressed(MouseEvent e){
		//System.out.println("Pressed");
	}
	public void mouseReleased(MouseEvent e){
		//System.out.println("Released");
	}
	public void mouseEntered(MouseEvent e){
		//System.out.println("Entered");
	}
	public void mouseExited(MouseEvent e){
		//System.out.println("Exited");
	}
}
