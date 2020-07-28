package viewer.action;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.sun.javafx.binding.StringFormatter;
import com.sun.javafx.geom.Rectangle;

import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MirrorFlip {
//	public File lastImagefile=null;
//	public File lastLastImagefile = null;
	Save s = new Save();
	public static RotateAction action = Rotate.action;
	
	
	
	
	
	
	
     public void mirrorFlip(File imageFile)
     {
    	   action.setClicks(action.getClicks()+1);

 		ImageView tempImageView =(ImageView) MainAction.anchorPane.getChildren().get(4);//
 	 
 		MainAction.anchorPane.getChildren().remove(4);	//
 	 
		ImageView imageView =tempImageView;
		
		File tempImage = imageFile;
		
		File curImageFile =imageFile;
		if(  action.getLastclicks()!=0) {
			if(   action.getLastImagefile()!= null) {
				//System.out.println(Action.button2clicks);
			curImageFile =   action.getLastImagefile();
			}
			
		}
 		
 		
 		
 		
 		
 		
 		//ImageView imageView =new ImageView();
// 		try {
// 			
// 			   imageView.setImage(new Image(image.toURI().toString()));
// 		}
// 		  catch(Exception e) {
// 			   e.printStackTrace();
// 		   }
// 		 
 		String newMirrorfilename;
 		String filepath =curImageFile.getAbsolutePath();
 		  File newMirrorImagefile =null;
 		int index = filepath.lastIndexOf('.');
 		String suffix= filepath.substring(index+1);//��׺��
 		
 		newMirrorfilename =filepath.substring(0,index)+"_"+action.getClicks()+filepath.substring(index);
 		if(action.getLastImagefile()!=null)
 			newMirrorfilename =filepath.substring(0,index-1)+action.getClicks()+filepath.substring(index);
 		
 		
 		
 		
 	// mirrorfilename =filepath.substring(0,index)+"_mirror"+filepath.substring(index);
 		//System.out.println(suffix);
 		  
 	        BufferedImage curImage  = null;
 	       BufferedImage newMirrorImage=null;
 	        try {
 	        	curImage = ImageIO.read(curImageFile);
 	           //curImage  = ImageIO.read(file);

 	            int width = curImage .getWidth();
 	            int height = curImage .getHeight();
 	           newMirrorImage=new BufferedImage( width,height,  BufferedImage.TYPE_INT_RGB);
 	            for (int j = 0; j < height; j++) {
 	                int i = 0 ;
 	                while (i < width - 1) {
 	                   
 	                    int pr = curImage .getRGB(width -1-i, j);

 	                   newMirrorImage .setRGB(i, j, pr);
 	               
                   
 	                    i++;
 	                   
 	                }
 	            }

 	            
 	            try{
 	            	newMirrorImagefile = new File(newMirrorfilename); 
	            	 if(!newMirrorImagefile.exists()) {
	            		 newMirrorImagefile.createNewFile();
	            		ImageIO.write(newMirrorImage,suffix,newMirrorImagefile);
	            	  }
 	            	  
 	            	 
 	            	  
 	                       	  
 	            	  ImageIO.write(newMirrorImage ,suffix, newMirrorImagefile);
 	            }
 	           catch (Exception e) {
 	 	            e.printStackTrace();
 	 	        }
 	          

 	        } catch (Exception e) {
 	            e.printStackTrace();
 	        }
 	    
 	       try {
			
 	 			   imageView.setImage(new Image(newMirrorImagefile.toURI().toString()));
 		}
	 		  catch(Exception e) {
 			   e.printStackTrace();
 	 		   }
 		///////////////////////////////////////////////////////
 	      action.setLastLastImagefile(action.getLastImagefile());
 	 		if(action.getLastImagefile()!=null) {
 	 			//System.out.println(action.getClicks()+"      "+action.getLastImagefile().toURI().toString());
 	 		}
 	 		
 	 	action.setLastImagefile(newMirrorImagefile);
 		imageView.setPreserveRatio(true);
 		imageView.setFitWidth(400.0);//
 		imageView.setFitHeight(350.0);
 		//imageView.setStyle("-fx-rotate:"+Double.toString(Action.angle));
 	    // imageView.setStyle("-fx-rotate:90");
 		AnchorPane.setLeftAnchor(imageView,200.0);
		AnchorPane.setBottomAnchor(imageView, 100.0); 
		MainAction.anchorPane.getChildren().add(imageView);
		RotateAction.button4.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				
				// TODO Auto-generated method stub
				
				
				if(mouseEvent.getClickCount() == 1&&(action.getLastclicks()==1||action.getLastclicks()==2))
				{
							 
		    		
					
					
		    	  s.save(action.getLastImagefile(),imageFile);							
		    	 
					
				}
			}
		});
		
 		if(  action.getLastclicks()!=3) {
 			if(action.getClicks()>1) {	
 				action.getLastLastImagefile().delete();				
 			}
 			
 		}
 		
 		action.setLastclicks(2);
 		//System.out.println("m"+action.getLastclicks());
     }	
}
