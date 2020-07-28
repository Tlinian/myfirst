package viewer.action;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class Save {
	public static RotateAction action = Rotate.action;
	public static int flag=0;
	public static String inputName=null;
	public static String OldFileName=null;
	public boolean reName(String name,File f) {
		
		String path  = f.getAbsolutePath();
			int inx = path.lastIndexOf('.');
	 		String suffix= path.substring(inx+1);
			
			int index = path.lastIndexOf('\\');
	 		String oldName= path.substring(0,index+1);
	 		String newPath = oldName+name;
	 		System.out.println(newPath);
	 		try {
	 			
	 			BufferedImage curImage = ImageIO.read(f);
	 			File newf = new File(newPath);
	 			if(!newf.exists())
	 			{
	 				newf.createNewFile();
	 			}
	 			if(newf.exists())
	 			{
	 				ImageIO.write(curImage,suffix,newf);
	 			}
	 			if(name.equals(OldFileName)) {
	 	
	 				return false;
	 			}
	 			
	 		}
	 		catch (Exception e) {
				// TODO: handle exception
			}  
		
		
		return true;
	}
	
	public void save(File lastFile,File imageFile) {
		
		ImageView temp =(ImageView)  MainAction.anchorPane.getChildren().get(4);//
   	 
		MainAction.anchorPane.getChildren().remove(4);	//
		MainAction.anchorPane.getChildren().removeAll(action.button1,action.button2,action.button3);
		ImageView imageView =temp;
		
		OldFileName= imageFile.getName();
		if(lastFile!=null&&lastFile!=imageFile)
		{
			Label label1 = new Label("请输入文件名");
			label1.setId("label_btn");
			AnchorPane.setLeftAnchor(label1,300.0);
			AnchorPane.setTopAnchor(label1,40.0);
			MainAction.anchorPane.getChildren().add(label1);
			
			TextField textField = new TextField ();
			AnchorPane.setLeftAnchor(textField,300.0);
			AnchorPane.setTopAnchor(textField,65.0);
			MainAction.anchorPane.getChildren().add(textField);
		  
		    Button bu = new Button("保存");
		    
		    AnchorPane.setLeftAnchor(bu,500.0);
			AnchorPane.setTopAnchor(bu,65.0);
			MainAction.anchorPane.getChildren().add(bu);
		    	bu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						inputName = textField.getText();
			if( reName(inputName,lastFile)) {
				MainAction.anchorPane.getChildren().removeAll(action.button4,bu,textField,label1);		
			imageFile.delete();
			lastFile.delete();
			}
			else
			{
				MainAction.anchorPane.getChildren().removeAll(action.button1,action.button2,action.button4,bu,textField,label1);		
				lastFile.delete();
			}
			Label label2 = new Label("保存成功");
			label2.setId("label_btn");
			AnchorPane.setLeftAnchor(label2,300.0);
			AnchorPane.setTopAnchor(label2,65.0);
			MainAction.anchorPane.getChildren().add(label2);
					}
				});	  
		}
		
		imageView.setPreserveRatio(true);
 		imageView.setFitWidth(400.0);//
 		imageView.setFitHeight(350.0);
 		
 		 AnchorPane.setLeftAnchor(imageView,250.0);
 		AnchorPane.setBottomAnchor(imageView, 100.0);
		MainAction.anchorPane.getChildren().add(imageView);
		 action.setLastclicks(3);
		
	}
	
	
}

