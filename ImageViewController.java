package application;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ImageViewController implements Initializable  {
	
	//Initialize previous stage
	Stage prevStage;
	private int rotateImage;
	private double brightness;
	private double saturationValue;
	
	
    // Private fields for components 
    @FXML
    private ImageView imageView;
    
    @FXML
    private ScrollPane scroll = new ScrollPane();
    
    @FXML
    private GridPane grid;
    
    @FXML
    private BorderPane border;
    
    @FXML
    private CheckBox box1;
    
    @FXML
    private CheckBox box2;
    
    @FXML
    private CheckBox box3;
    
    //activate the scrollPane 
    public void initialize(URL url, ResourceBundle resource){
    	initScrollPane();
    }
    
 
	

	//Display new image
    @FXML
    private void handleNew()throws IOException{
    	
    	int imageColumn = 0;
    	int imageRow = 0;
    	
    	TextField directoryField = new TextField();
    	grid.getChildren().clear();
    	
    	FileChooser fileChooser = new FileChooser();
    	
    	//set extension filter
    	ExtensionFilter exFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg", "*.jpeg", "*.tif", "*.bmp");
    	
    	fileChooser.getExtensionFilters().add(exFilter);
    	
    	//adding list variable
    	List<File> list = fileChooser.showOpenMultipleDialog(null);
    	ObservableList<File> imageFiles = FXCollections.observableArrayList();
    	
    	if(list != null){
    		for(File file : list){
    			
    			//Image image = new Image(file.toURI().toString());
    			final Image image = new Image(new FileInputStream(file), 150, 150, true, false);
    		ImageView imageView = new ImageView();
    		imageView = createImageView(image);
    		
    		//Vbox to display GridPane
    		VBox vbox1 = new VBox();
    		vbox1.getChildren().addAll(imageView);
    		
    		//selected checkbox
    		if (box1.isSelected()){
    			
    			//Change from image to list
    		ListView<File> imageFilesList = new ListView<>(imageFiles);
    		imageFilesList.setCellFactory (listView -> new ListCell<File>(){
    			private final ImageView imageView = new ImageView();
    			{
    				imageView.setFitHeight(0);
    				imageView.setFitWidth(80);
    				imageView.setPreserveRatio(true);
    			}
    			
    			//Getting image path
    			@Override
    			public void updateItem(File file, boolean empty){
    				super.updateItem(file, empty);
    				if (empty){
    					setText(null);
    					setGraphic(null);
    				} else{
    					setText(file.getAbsolutePath().toString());
    					imageView.setImage(new Image(file.getAbsolutePath().toString(), true));
    					setGraphic(imageView);
    				}
    			}

				
				});
    	
    		
    		//put settings to grid
    		grid.add(vbox1, imageColumn, imageRow);
    		
    		imageColumn++;
    		if(imageColumn > 0){
    			imageColumn = 0;
    			imageRow++;
    		}
    		
    		}else{
    			//settings for thumbnail images
    			grid.setAlignment(Pos.CENTER);
    			grid.setPadding(new Insets(150, 15, 15, 15));
    			
    			grid.setHgap(300);
    			grid.setVgap(300);
    			
    			ColumnConstraints columnConstraints = new ColumnConstraints();
    			columnConstraints.setFillWidth(true);
    			columnConstraints.setHgrow(Priority.ALWAYS);
    			grid.getColumnConstraints().add(columnConstraints);
    			
    			imageView.setFitHeight(250);
    			imageView.setFitWidth(250);
    			
    			grid.add(vbox1, imageColumn, imageRow);
    			
    				imageColumn++;
    			//prevent the boundary from surpassing the window size
    				if(imageColumn > 1){
    					imageColumn = 0;
    					imageRow++;
    				}
    				}
    		}
    	}
    }
    


     
    //Create image and Click through
    private ImageView createImageView(Image image) throws IOException{
    	// TODO Auto-generated method stub
    	ImageView imageView = null;
    	
    	imageView = new ImageView(image);
    	
    	//If image is clicked
    	imageView.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		
    		@Override
    		public void handle(MouseEvent mouseEvent){
    			
    			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
    
    			if(mouseEvent.getClickCount()== 1){
    				
    				try{
    					//show new options
    					rotateImage = 0;
    					ColorAdjust colorAdjust = new ColorAdjust();
    					brightness = 0;
    					saturationValue = 0;
    					
    					//new stage
    					Stage newStage = new Stage();
    					newStage.setTitle("Image Gallery");
    					newStage.setWidth(prevStage.getWidth());
    					newStage.setHeight(prevStage.getHeight());
    					
    				//BorderPane is created to show ImageView
    				BorderPane border = new BorderPane();
    				ImageView imageView = ImageView();
    				
    				MenuBar menuB = new MenuBar();
    				Menu menu1 = new Menu ("Edit");
    				Menu menu2 = new Menu ("Saturation");
    				Menu menu3 = new Menu ("Brighten");
    				
    				//Initialize menu
    				MenuItem RotateImage = new MenuItem("Rotate");
    				MenuItem SaveImage = new MenuItem("Save Image");
    				
    				MenuItem menuItem2 = new MenuItem("Increase");
    				MenuItem menuItem3 = new MenuItem("Decrease");
    				
    				MenuItem menuItem4 = new MenuItem("Increase");
    				MenuItem menuItem5 = new MenuItem("Decrease");
    				
    				menu1.getItems().addAll(RotateImage, SaveImage);
    				menu2.getItems().addAll(menuItem2,menuItem3);
    				menu3.getItems().addAll(menuItem4,menuItem5);
    				
    				menuB.getMenus().addAll(menu1,menu2,menu3);
    				
    				//menu settings
    				border.setTop(menuB);
    					imageView.setImage(image);
    					imageView.setStyle("-fx-background-colour: Black");
    					imageView.setFitHeight(prevStage.getHeight()- 10);
    					imageView.setPreserveRatio(true);
    					imageView.setSmooth(false);
    					
    					imageView.setCache(true);
    					border.setCenter(imageView);
    					border.setStyle("-fx-background-color: Black");
    					
    					//to save an image
    					SaveImage.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							//savetoFile(image);
    							FileChooser filechooser = new FileChooser();
    						filechooser.setTitle("Save Image");
    						
    						File file = filechooser.showSaveDialog(null);
    						if (file != null){
    							try{
    								ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(),null), "png", file);
    							} catch (IOException ex){
    								Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
    							}
    						}
    						}
    					});
    					
    					//Function to rotate image
    					RotateImage.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							rotateImage +=90;
    							imageView.setRotate(rotateImage);
    							
    						}
    					});
    					
    					
    					//Increase brightness
    					menuItem2.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							brightness +=0.1;
    							colorAdjust.setBrightness(brightness);
    							imageView.setEffect(colorAdjust);
    						}
    					});
    					
    					//Reduce Brightness
    					menuItem3.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							brightness -= 0.1;
    							colorAdjust.setBrightness(brightness);
    							imageView.setEffect(colorAdjust);
    						}
    					});
    					
    					//Increase Saturation
    					menuItem4.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							saturationValue += 0.1;
    							colorAdjust.setSaturation(saturationValue);
    							imageView.setEffect(colorAdjust);
    	
    						}
    					});
    					
    					//Decrease saturation
    					menuItem5.setOnAction(new EventHandler<ActionEvent>(){
    						@Override
    						public void handle(ActionEvent event){
    							saturationValue -= 0.1;
    							colorAdjust.setSaturation(saturationValue);
    							imageView.setEffect(colorAdjust);
    						}
    					});
    					
    					Scene scene = new Scene(border, 700, 600);
    					newStage.setScene(scene);
    					newStage.show();
    					
    				} catch (Exception e){
    					e.printStackTrace();
    					
    				}
    			}
    			}
    		}

			private ImageView ImageView() {
				// TODO Auto-generated method stub
				return null;
			}
    	});
    	return imageView;
    }
    


public void initScrollPane(){
	scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
	scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	scroll.setContent(grid);
}

//Exit
@FXML
private void handleExit(){
	System.exit(0);
	
}

//About 
@FXML
private void handleAbout(){
	
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Image Gallery");
	alert.setHeaderText("About");
	alert.setContentText("Author: Brandon Pillay");
	
	alert.showAndWait();
}




public void setPrevStage(Stage primaryStage) {
	// TODO Auto-generated method stub
	
}
}

			

    					
    					
    						
    					
    					
    				
    			
   
