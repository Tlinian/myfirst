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

public class Rotate {
	Save s = new Save();
	public static RotateAction action = new RotateAction();
	public void rotate(File imageFile) {
		action.setClicks(action.getClicks() + 1);
		ImageView tempImageView = (ImageView) MainAction.anchorPane.getChildren().get(4);//
		MainAction.anchorPane.getChildren().remove(4);//
		ImageView imageView = tempImageView;
		File tempImage = imageFile;
		File curImageFile = imageFile;
		if (action.getLastclicks() != 0) {
			if (action.getLastImagefile() != null) {
				curImageFile = action.getLastImagefile();
			}
		}
		///////////////////////////////////////////////////////////////////
		String newRotatafilename;
		String filepath = curImageFile.getAbsolutePath();
		File newRotateImagefile = null;
		int index = filepath.lastIndexOf('.');
		String suffix = filepath.substring(index + 1);

		newRotatafilename = filepath.substring(0, index) + "_" + action.getClicks() + filepath.substring(index);
		if (action.getLastImagefile() != null)
			newRotatafilename = filepath.substring(0, index - 1) + action.getClicks() + filepath.substring(index);
		BufferedImage curImage = null;
		BufferedImage newRotateImage = null;
		try {

			curImage = ImageIO.read(curImageFile);
			int width = curImage.getWidth();
			int height = curImage.getHeight();
			newRotateImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < height; i++) {
				int j = 0;
				while (j < width - 1) {
					int pl = curImage.getRGB(j, height - 1 - i);
					newRotateImage.setRGB(i, j, pl);
					j++;
				}
			}
				newRotateImagefile = new File(newRotatafilename);
				if (!newRotateImagefile.exists()) {
					newRotateImagefile.createNewFile();
					ImageIO.write(newRotateImage, suffix, newRotateImagefile);
					//System.out.println("vyuniow"+newRotateImagefile.getAbsolutePath());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			imageView.setImage(new Image(newRotateImagefile.toURI().toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		action.setLastLastImagefile(action.getLastImagefile());
		if (action.getLastImagefile() != null) {
			//System.out.println(action.getClicks() + "      " + action.getLastImagefile().toURI().toString());
		}
		action.setLastImagefile(newRotateImagefile);
		//System.out.println(action.getClicks() + "      *" + newRotateImagefile.toURI().toString());
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(400.0);//
		imageView.setFitHeight(350.0);
		AnchorPane.setLeftAnchor(imageView, 200.0);
		AnchorPane.setBottomAnchor(imageView, 100.0);
		MainAction.anchorPane.getChildren().add(imageView);
		action.button4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				// TODO Auto-generated method stub
				if (mouseEvent.getClickCount() == 1 && (action.getLastclicks() == 1 || action.getLastclicks() == 2)) {

					s.save(action.getLastImagefile(), imageFile);
				}
			}
		});
		if (action.getLastclicks() != 3) {
			if (action.getClicks() > 1) {
				action.getLastLastImagefile().delete();
			}
		}
		action.setLastclicks(1);
	

	}
}
