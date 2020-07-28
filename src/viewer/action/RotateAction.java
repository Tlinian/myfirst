package viewer.action;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class RotateAction {
	public  int lastclicks=0;
	public static Button button1;
	public static Button button2;
	public static Button button3;
	public static Button button4;
	public static Rotate r =new Rotate();
	public static	MirrorFlip m =new MirrorFlip();	
	public File lastImagefile=null;
	public File lastLastImagefile = null;
	public int clicks=0;	
	public Stage primaryStage;
	public RotateAction() {
		primaryStage=MainAction.primaryStage;
		 lastclicks=this.getLastclicks();
		button1 = new Button("旋转");
		button1.setId("rotate");
		button1.setScaleX(2);
		button1.setScaleY(2);
		
		 button2 = new Button("镜像");
		 button2.setScaleX(2);
			button2.setScaleY(2);
		 button2.setId("mirror");
		 button3 = new Button ("缩放");
		 button3.setId("zoom");
		 button3.setScaleX(2);
			button3.setScaleY(2);
		 button4 = new Button("保存");
		 button4.setId("save");
		 button4.setScaleX(2);
		button4.setScaleY(2);
		 lastImagefile=this.getLastImagefile();
		 lastLastImagefile = this.getLastLastImagefile();
		 clicks=this.getLastclicks();
	}
	public int getLastclicks() {
		return this.lastclicks;
	}
	
	public void setLastclicks(int l) {
		this.lastclicks=l;
	}
	
	public int getClicks() {
		return this.clicks;
	}
	
	public void setClicks(int c) {
		this.clicks=c;
	}
	public Button getButton1() {
		return button1;
	}
	public Button getButton2() {
		return button2;
	}
	
	public  File getLastImagefile()
	{
		return this.lastImagefile;
		
	}
   public void setLastImagefile(File f) {
		this.lastImagefile=f;
	}
   public File getLastLastImagefile()
	{
		return this.lastLastImagefile;
		
	}
   public void setLastLastImagefile(File f) {
		this.lastLastImagefile=f;
	}	
   public void initScene(File image) {	   
	ImageView imageView = new ImageView();
		try {
			   imageView.setImage(new Image(image.toURI().toString()));
	}
		  catch(Exception e) {
		   e.printStackTrace();
		   }
		
	imageView.setPreserveRatio(true);
		imageView.setFitWidth(400.0);//
		imageView.setFitHeight(350.0);
		button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// TODO Auto-generated method stub	
				if(mouseEvent.getClickCount() == 1||mouseEvent.getClickCount() == 2)
				{		
					r.rotate(image);		   
				}
			}
		});

		button2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				// TODO Auto-generated method stub
				
				if(mouseEvent.getClickCount() == 1||mouseEvent.getClickCount() == 2)
				{
					m.mirrorFlip(image);
				}
			}
		});	
		button3.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				// TODO Auto-generated method stub
				new EnlargeAction(image);
			}
		});	
		
	    AnchorPane.setLeftAnchor(imageView,250.0);
		AnchorPane.setBottomAnchor(imageView, 100.0);	
		AnchorPane.setLeftAnchor(button1, 100.0);
		AnchorPane.setTopAnchor(button1, 20.0);
		AnchorPane.setLeftAnchor(button2, 250.0);
		AnchorPane.setTopAnchor(button2, 20.0);
		AnchorPane.setLeftAnchor(button3, 350.0);
		AnchorPane.setTopAnchor(button3, 20.0);
		AnchorPane.setLeftAnchor(button4, 500.0);
		AnchorPane.setTopAnchor(button4, 20.0);
		
		MainAction.anchorPane.getChildren().addAll(button1,button2,button3,button4,imageView);    
		MainAction.anchorPane.setId("anchorpane");
	    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {		
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				if(Rotate.action.getLastImagefile()!=image&&Rotate.action.getLastImagefile()!=null) {
					Rotate.action.getLastImagefile().delete();
					Rotate.action.setLastImagefile(null);
					Rotate.action.setLastLastImagefile(null);
					Rotate.action.setClicks(0);
					Rotate.action.setLastclicks(0);
				}
				
				
			}
		});
	    
   }
}
