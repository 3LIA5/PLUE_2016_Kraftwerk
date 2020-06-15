package application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Generator;
import model.KaplanTurbine;
import model.KraftwerkException;
import model.Windrad;

public class DialogGenerator extends Stage
{
	private Scene scene;
	private GridPane rootGridPane;


	private Generator generator;
	private boolean uebernehmenOk;
	
	private Label   	laNennleistung, laStandort, laNenndrehzahl;
	private CheckBox 	cbEin;
	private Slider 		slNennleistung, slNenndrehzahl;
	private TextField 	tfStandort;
	
	private Button btUebernehmen, btAbbrechen;
		
	public DialogGenerator()
	{
		initComponents();
		setComponentsVisible(false);
		addComponents();
		addHandler();
	}
	private void initComponents()
	{
		initModality(Modality.APPLICATION_MODAL);
		rootGridPane = new GridPane();
		scene = new Scene(rootGridPane, 410, 180);
		setScene(scene);
		rootGridPane.setHgap(10);
		rootGridPane.setVgap(10);
		rootGridPane.setPadding(new Insets(10));
		setResizable(false);
		
		uebernehmenOk = false;
		
		laStandort 		= new Label("Standort");
		laNennleistung 	= new Label("Nennleistung");
			GridPane.setHalignment(laNennleistung, HPos.CENTER);
		laNenndrehzahl 	= new Label("Nenndrehzahl");
			GridPane.setHalignment(laNenndrehzahl, HPos.CENTER);
		tfStandort 		= new TextField();
		
		cbEin 			= new CheckBox("ist aktiv");
		
		slNennleistung  = new Slider();
			slNennleistung.setSnapToTicks(true);
			slNennleistung.setShowTickLabels(true);
			slNennleistung.setShowTickMarks(true);
		slNenndrehzahl  = new Slider();
			slNenndrehzahl.setSnapToTicks(true);
			slNenndrehzahl.setMajorTickUnit(10);
			slNenndrehzahl.setShowTickLabels(true);
			slNenndrehzahl.setShowTickMarks(true);
			
		btAbbrechen 	= new Button("Abbrechen");
		btUebernehmen 	= new Button("Übernehmen");
	}
	private void setComponentsVisible(boolean visible)
	{
		laNenndrehzahl.setVisible(visible);
		slNenndrehzahl.setVisible(visible);
	}
	private void addComponents()
	{				
		rootGridPane.add(laStandort,		0,1);
		rootGridPane.add(tfStandort,		1,1);
		rootGridPane.add(cbEin,				2,1);
		
		rootGridPane.add(laNennleistung,	0,2,2,1);
		rootGridPane.add(slNennleistung,	0,3,2,1);
		
		rootGridPane.add(laNenndrehzahl,	2,2,2,1);		
		rootGridPane.add(slNenndrehzahl,	2,3,2,1);
		
		rootGridPane.add(btAbbrechen, 		2,5);
		rootGridPane.add(btUebernehmen, 	3,5);
	}
	public boolean getUebernehmenOK()
	{
		return uebernehmenOk;
	}
	public void updateAndShow(Generator generator)
	{
		setComponentsVisible(false);
		uebernehmenOk = false;
		
		this.generator=generator;
		setTitle(generator.getClass().getSimpleName());
		tfStandort.setText(generator.getStandort());
		cbEin.setSelected(generator.isRunning());
		if (generator instanceof KaplanTurbine)
		{
			setComponentsVisible(true);
			slNennleistung.setValue(generator.getNennLeistung());
			slNennleistung.setMajorTickUnit(5);
			slNennleistung.setMinorTickCount(1);
			slNennleistung.setMin(5);
			slNennleistung.setMax(100);
			slNenndrehzahl.setValue(((KaplanTurbine) generator).getNennDrehzahl());
			slNenndrehzahl.setMin(50);
			slNenndrehzahl.setMax(150);	
		}
		else
			if( generator instanceof Windrad)
			{
				setComponentsVisible(false);
				slNennleistung.setValue(generator.getNennLeistung());
				slNennleistung.setMajorTickUnit(2);
				slNennleistung.setMinorTickCount(1);
				slNennleistung.setMin(.1f);
				slNennleistung.setMax(10);				
			}
			else
				Main.showAlert(AlertType.CONFIRMATION, "Dieser Generatorentyp kann derzeit nicht bearbeitet werden!");
		showAndWait();
	}
	private void addHandler()
	{
		btAbbrechen.setOnAction( event -> close() );
		btUebernehmen.setOnAction( event -> uebernehmen() );
	}
	private void uebernehmen()
	{
		try
		{
			generator.setStandort(tfStandort.getText());			
			generator.setRunning(cbEin.isSelected());
			if (generator instanceof Windrad) 
			{
				((Windrad)generator).setNennLeistung(Math.round(((slNennleistung.getValue())*100)/100));
				uebernehmenOk=true;
				close();
			}
			else
				if (generator instanceof KaplanTurbine)
				{
					((KaplanTurbine)generator).setNennLeistung(Math.round(((slNennleistung.getValue())*100)/100));
					((KaplanTurbine) generator).setNennDrehzahl((int)slNenndrehzahl.getValue());
					uebernehmenOk=true;
					close();
				}
				else
				{
					Main.showAlert(AlertType.INFORMATION, "Dieser Generatorentyp kann nicht übernommen werden!");
				}
		} 
		catch (KraftwerkException e)
		{
			Main.showAlert(AlertType.ERROR, e.getMessage(), "ERROR-Übernehmen");
		}
	}

}
