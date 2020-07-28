package viewer.service.impl;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import viewer.action.MainAction;
import viewer.action.SlideAction;
import viewer.model.ImagePreViewItem;
import viewer.service.RotateService;

public class RotateServiceImpl implements RotateService {

	@Override
	public void openRotateView(File imageFile) {
		// TODO Auto-generated method stub
		
       // SlideAction slideAction = new SlideAction(rotateViewStage, anchorpane, imageFileList);
        
        
        MainAction a = new MainAction(imageFile);
        
       
	}

}
