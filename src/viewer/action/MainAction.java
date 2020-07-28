package viewer.action;

import java.io.File;


import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainAction {
	public static int SCENE_HIGHT = 558;
	  public static int SCENE_WIGHT =775;
	public static   AnchorPane anchorPane = new AnchorPane();
	public static Stage primaryStage;
	
	   public MainAction(File imageFile) {
		    primaryStage = new Stage();
		    setAnchorPane(new AnchorPane());
		   RotateAction a = new RotateAction();
		
		   a.initScene(imageFile);
		  
		  Scene scene = new Scene(anchorPane,SCENE_WIGHT,SCENE_HIGHT);
		  scene.getStylesheets().add("myCSS.css");
			 primaryStage.setScene(scene);
			 primaryStage.setTitle("旋转"); 
			 primaryStage.setResizable(false);
			 primaryStage.show(); 
	   }
	
	public AnchorPane getAnchorPane()
	{
		return anchorPane;
	}
	
	public void  setAnchorPane(AnchorPane anane)
	{
		 anchorPane =anane;
	}
	
	
}
