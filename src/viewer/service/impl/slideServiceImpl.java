package viewer.service.impl;

import javafx.beans.property.SimpleListProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import viewer.action.SlideAction;
import viewer.model.ImagePreViewItem;
import viewer.service.SlideService;

/**
 * Created by PanD
 */

public class slideServiceImpl implements SlideService {
 public static Stage slideViewStage = new Stage();
    @Override
    public void openPPTView(SimpleListProperty<ImagePreViewItem> imageList) {
       
        //slideViewStage.setResizable(false);
        slideViewStage.setTitle("Picture Viewer");
        slideViewStage.getIcons().add(new Image("file:resources/images/title.png"));
      // slideViewStage.setFullScreen(true);
        //System.out.println("PPT");
        AnchorPane anchorpane = new AnchorPane();
        
        SlideAction slideAction = new SlideAction(slideViewStage, anchorpane, imageList);
        //Scene scene = new Scene(anchorpane);
        Scene scene = new Scene(anchorpane,600,600);
        scene.getStylesheets().add("myCSS.css");
        slideViewStage.setScene(scene);
        slideViewStage.show();   
        
    }
	
}
