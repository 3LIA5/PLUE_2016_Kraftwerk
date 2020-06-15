package application;

import java.util.List;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Generator;

public class ListViewGeneratoren extends ListView<Generator>
{
	private RootBorderPane rootPane;
	private MyContextMenu myContextMenue;
	
	public ListViewGeneratoren(RootBorderPane rootPane)
	{
		this.rootPane=rootPane;		
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		myContextMenue = new MyContextMenu();
		addHandler();
	}
	private void addHandler()
	{
		setOnMouseClicked(event -> checkMouseEvent(event));	
	}
	public void updateAndShow(List<Generator> col)
	{
		getItems().setAll(col);
	}
	private void checkMouseEvent(MouseEvent event)
	{
		if(event.isPopupTrigger())
			myContextMenue.show(this, event.getScreenX(), event.getScreenY());
		else
			if(event.getClickCount()>1)
				rootPane.bearbeiten();
	}
	public class MyContextMenu extends ContextMenu
	{
		private MenuItem loeschen, bearbeiten;
		public MyContextMenu()
		{
			initAndAddComponents();
			addMyMenuHandler();
		}
		private void initAndAddComponents()
		{
			loeschen = new MenuItem("löschen");
			bearbeiten = new MenuItem("bearbeiten");
			getItems().addAll(loeschen,bearbeiten);
		}
		private void addMyMenuHandler()
		{
			loeschen.setOnAction(event -> rootPane.loeschen());
		}
	}
}
