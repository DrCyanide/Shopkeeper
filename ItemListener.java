
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ItemListener implements MouseListener{

    ItemDetailsPanel itemDetails;
    public ItemListener(ItemDetailsPanel itemDetails){
        this.itemDetails = itemDetails;
    }

    private String getItemId(MouseEvent e){
		if(e.getComponent().getClass().getName() == "javax.swing.JPanel"){
			for(Component c: ((JPanel)e.getComponent()).getComponents()){
				if(!c.isVisible() && c.getClass().getName() == "javax.swing.JLabel"){
					String text = ((JLabel)c).getText();
					if(text.contains("itemId:")){
						return text.substring(text.indexOf(":")+1,text.length());
					}
				}
			}
		}
		return "";
	}

    public void mouseClicked(MouseEvent e){
		//System.out.println("Clicked!");
		String id = getItemId(e);
		itemDetails.updateItem(id);
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
