package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			RootBorderPane root = new RootBorderPane();
			Scene scene = new Scene(root,700,200);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Kraftwerks-Steuerung");
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			showAlert(AlertType.ERROR, e.getMessage(), "Fatal-ERROR!!");
		}
	}
	public static void showAlert(AlertType alertType, String message, String title)
	{
		Alert alert = new Alert(alertType, message, ButtonType.OK);
		if (title!=null)
			alert.setTitle(title);
		else
			alert.setTitle(alertType.name());
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	public static void showAlert(AlertType alertType, String message)
	{
		Main.showAlert(alertType, message, null);
	}
	public static void main(String[] args) {
		launch(args);
	}
}
