package viewer.action;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import viewer.controller.FilterviewController;
import viewer.Main;
import viewer.model.ImagePreViewItem;
public class FilterAction
{
	public static File file;
	public static ImageView imageview;
	public static Stage stage = new Stage();
	public static List<File> imagePreviewList;
	public static FlowPane flowpane;
	public static ImagePreViewItem imagepreview;
	public FilterAction(File file,FlowPane flowpane,ImagePreViewItem imagePreViewItem) throws MalformedURLException
	{
		this.imagepreview = imagePreViewItem;
		this.flowpane = flowpane;
		//this.imagePreviewList=imagePreviewList;
		this.file=file;
		this.imageview=new ImageView(new Image(file.toURI().toURL().toString()));
		FilterviewController filterviewControllor = new FilterviewController();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Filterset.fxml"));
			Parent root = (Parent) loader.load();
			loader.setController(filterviewControllor);
			Scene scene = new Scene(root);
			scene.getStylesheets().add("myCSS.css");
			stage.setScene(scene);
			stage.setTitle("美化");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
