package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	public Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
	this.primaryStage.setTitle("Image Gallery");
	try {
        //Initialize stage
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("ImageView.fxml"));
        AnchorPane imageOverview = (AnchorPane) loader.load();
        
        
        //Connect to FXML
        ImageViewController controller = loader.getController();
        controller.setPrevStage(primaryStage);
        
        Scene scene = new Scene(imageOverview, 700, 600);
        primaryStage.setScene(scene);
			
		primaryStage.show();
	} catch(Exception e) {
		e.printStackTrace();
	}
}

public Stage getPrimaryStage() {
    return primaryStage;
}

public static void main(String[] args) {
	launch(args);
}
}
