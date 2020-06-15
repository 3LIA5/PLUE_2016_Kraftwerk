package application;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import model.Generator;
import model.KaplanTurbine;
import model.KraftwerkException;
import model.Kraftwerksanlage;
import model.Windrad;

public class RootBorderPane extends BorderPane
{
	private Kraftwerksanlage kraftwerk;
	private ListViewGeneratoren listViewGeneratoren;
	private DialogGenerator dialogGeneratoren;

	private MenuBar 	menuBar;
	private Menu 		menuDatei, menuAnlage, menuSetRunning, menuGeneratoren, menuHinzufügen;
	private MenuItem 	menuItemLaden, menuItemSpeichern, menuItemImportieren, menuItemExportieren, menuItemBeenden,
						menuItemEin, menuItemAus,
						menuItemLoeschen, menuItemHinzWindrad, menuItemHinzKaplan, menuItemBearbeiten;
	
	private FlowPane 		rightFlowPane, bottomFlowPane;
		private Button 		btSortierenLeistung, btSortierenStandort, btBeenden, btUebersichtBeenden;
		private RadioButton rbtSpeichern, rbtNichtSpeichern;
		private ToggleGroup tggSpeichern;
	
	public RootBorderPane()
	{
		initComponents();
		addComponents();
		setComponentsDisabled(true);
		setComponentsVisible(false);
		addHandler();
	}
	private void initComponents()
	{
		dialogGeneratoren = new DialogGenerator();
		kraftwerk = new Kraftwerksanlage();
		listViewGeneratoren = new ListViewGeneratoren(this);
			listViewGeneratoren.setVisible(false);
		
		menuBar 			= new MenuBar();
			menuDatei 			= new Menu("Datei");
			menuAnlage 			= new Menu("Anlage");
				menuSetRunning 	= new Menu("Set running");
			menuGeneratoren	 	= new Menu("Generatoren");
				menuHinzufügen 	= new Menu("Hinzufügen");
			
			menuItemLaden 		= new MenuItem("Laden");
			menuItemSpeichern 	= new MenuItem("Speichern");
			menuItemImportieren = new MenuItem("Importieren");
			menuItemExportieren = new MenuItem("Exportieren");
			menuItemBeenden 	= new MenuItem("Beenden");
			
			menuItemEin 		= new MenuItem("Ein");
			menuItemAus 		= new MenuItem("Aus");
			
			menuItemLoeschen 	= new MenuItem("Löschen");			
			menuItemBearbeiten  = new MenuItem("Bearbeiten");
			menuItemHinzWindrad = new MenuItem("Windrad");
			menuItemHinzKaplan  = new MenuItem("Kaplan Turbine");
			
		
		rightFlowPane = new FlowPane();
			rightFlowPane.setOrientation(Orientation.VERTICAL);
			rightFlowPane.setMinWidth(150);
			rightFlowPane.setMaxWidth(150);
			btSortierenLeistung  = new Button("Sortieren nach Leistung");
			btSortierenStandort  = new Button("Sortieren nach Standort");
			btBeenden 			 = new Button("Beenden");
			btUebersichtBeenden  = new Button("Übersicht beenden");
				btSortierenLeistung.setPrefSize(150, 33);
				btSortierenStandort.setPrefSize(150, 33);
				btBeenden		   .setPrefSize(150, 33);
				btUebersichtBeenden.setPrefSize(150, 33);
		
		bottomFlowPane = new FlowPane();
			bottomFlowPane.setHgap(10);
			bottomFlowPane.setPadding(new Insets(10, 0, 10, 0));
			bottomFlowPane.setAlignment(Pos.BOTTOM_CENTER);
			rbtSpeichern 	  = new RadioButton("speichern?");
			rbtNichtSpeichern = new RadioButton("nicht speichern?");
			tggSpeichern 	  = new ToggleGroup();
	}
	private void addComponents()
	{
		setTop	  (menuBar);
		setCenter (listViewGeneratoren);
		setRight  (rightFlowPane);
		setBottom (bottomFlowPane);
		
		menuBar.getMenus().addAll(menuDatei,
								  menuAnlage,
								  menuGeneratoren);
		menuDatei.getItems().addAll(menuItemLaden, 
									menuItemSpeichern, 
									new SeparatorMenuItem(), 
									menuItemImportieren, 
									menuItemExportieren,
									new SeparatorMenuItem(), 
									menuItemBeenden);
		menuAnlage.getItems().addAll(        menuSetRunning);
			menuSetRunning.getItems().addAll(menuItemEin, 
											 menuItemAus);
		menuGeneratoren.getItems().addAll(menuItemLoeschen, 
										  new SeparatorMenuItem(),
										  menuItemBearbeiten,
										  menuHinzufügen);
			menuHinzufügen.getItems().addAll(menuItemHinzKaplan, 
											 menuItemHinzWindrad);
		
		rightFlowPane.getChildren().addAll(btSortierenLeistung, 
										   btSortierenStandort, 
										   btBeenden, 
										   btUebersichtBeenden);
		bottomFlowPane.getChildren().addAll(new Label("Datei beim Beenden: "),rbtSpeichern, rbtNichtSpeichern);
		tggSpeichern.getToggles().addAll(rbtSpeichern, rbtNichtSpeichern);
		
	}
	private void setComponentsDisabled(boolean disable)
	{
		menuItemSpeichern.	setDisable(disable);
		menuItemExportieren.setDisable(disable);
		menuItemLoeschen.	setDisable(disable);
		menuSetRunning.		setDisable(disable);
		menuItemBearbeiten.	setDisable(disable);
	}
	private void setComponentsVisible(boolean visible)
	{
		rbtSpeichern.		setSelected(visible);
		rightFlowPane.		setVisible(visible);
		bottomFlowPane.		setVisible(visible);
		listViewGeneratoren.setVisible(visible);
	}	
	private void addHandler()
	{
		menuItemBeenden		.setOnAction(event -> beenden());
		menuItemLaden		.setOnAction(event -> laden());
		menuItemSpeichern	.setOnAction(event -> speichern());
		
		btSortierenLeistung .setOnAction(event -> sortierenLeistung());
		btSortierenStandort .setOnAction(event -> sortierenStandort());
		btBeenden			.setOnAction(event -> beenden());
		btUebersichtBeenden .setOnAction(event -> setComponentsVisible(false));
		menuItemEin			.setOnAction(event -> {kraftwerk.setRunning(true); listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());});
		menuItemAus 		.setOnAction(event -> {kraftwerk.setRunning(false); listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());});
		menuItemLoeschen	.setOnAction(event -> loeschen());
		menuItemBearbeiten	.setOnAction(event -> bearbeiten());
		menuItemHinzWindrad .setOnAction(event -> hinzufügenWindrad());
		menuItemHinzKaplan  .setOnAction(event -> hinzufügenKaplan());
		
		
	}
//	--------------- Handler Methoden ----------------------
	private void beenden()
	{
		if (rbtSpeichern.isSelected())
		{
			speichern();
			Platform.exit();
		}
		else
			Platform.exit();
	}
	private void laden()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Generatoren laden");
		fileChooser.setInitialDirectory(new File("C:\\scratch"));
		File generator = fileChooser.showOpenDialog(null);
		if(generator!=null)
			try
			{
				kraftwerk.loadGeneratoren(generator.getAbsolutePath());
				listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
				setComponentsDisabled(false);
				setComponentsVisible(true);				
			} 
			catch (KraftwerkException e)
			{
				Main.showAlert(AlertType.ERROR, e.getMessage());
			}
		else
			Main.showAlert(AlertType.WARNING, "Abbruch durch Benutzer");
	}
	private void speichern()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Generatoren speichern");
		fileChooser.setInitialDirectory(new File("C:\\scratch"));
		File generator = fileChooser.showSaveDialog(null);
		if(generator!=null)
			try
			{
				kraftwerk.saveGeneratoren(generator.getAbsolutePath());
			} 
			catch (KraftwerkException e)
			{
				Main.showAlert(AlertType.ERROR, e.getMessage());
			}
		else
			Main.showAlert(AlertType.WARNING, "Abbruch durch Benutzer");
	}
	private void sortierenLeistung()
	{
		kraftwerk.sortLeistung();
		listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
	}
	private void sortierenStandort()
	{
		kraftwerk.sortStandort();
		listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
	}
	public void loeschen()
	{
		kraftwerk.getGeneratoren().removeAll(listViewGeneratoren.getSelectionModel().getSelectedItems());
		listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
	}
	public void bearbeiten()
	{
		List<Generator>generatoren=listViewGeneratoren.getSelectionModel().getSelectedItems();
		if(generatoren.size()==1)
		{
			dialogGeneratoren.updateAndShow(generatoren.get(0));
			listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
		}
		else
			Main.showAlert(AlertType.INFORMATION, "Bitte genau einen Generator zur bearbeitung auswählen!");
	}
	private void hinzufügenWindrad()
	{
		try
		{
			Windrad generator = new Windrad("Standort eintragen", 1);
			dialogGeneratoren.updateAndShow(generator);
			if (dialogGeneratoren.getUebernehmenOK())
			{
				kraftwerk.addGenerator(generator);
				listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
				setComponentsDisabled(false);
				setComponentsVisible(true);	
			}
			else
				Main.showAlert(AlertType.INFORMATION, "Abbruch durch Benutzer", "Abbruch Hinzufügen");
		} 
		catch (KraftwerkException e)
		{
			Main.showAlert(AlertType.ERROR, e.getMessage());
		}		
	}
	private void hinzufügenKaplan()
	{
		try
		{
			KaplanTurbine generator = new KaplanTurbine("Standort eintragen", 50, 50);
			dialogGeneratoren.updateAndShow(generator);
			if (dialogGeneratoren.getUebernehmenOK())
			{
				kraftwerk.addGenerator(generator);
				listViewGeneratoren.updateAndShow(kraftwerk.getGeneratoren());
				setComponentsDisabled(false);
				setComponentsVisible(true);	
			}
			else
				Main.showAlert(AlertType.INFORMATION, "Abbruch durch Benutzer", "Abbruch Hinzufügen");
		} 
		catch (KraftwerkException e)
		{
			Main.showAlert(AlertType.ERROR, e.getMessage());
		}		
	}
}
