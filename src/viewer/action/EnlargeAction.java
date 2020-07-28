package viewer.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EnlargeAction
{

	File imageFile;
	ImageView imageView = new ImageView();
	int imageWidth;
	int imageHeight;
	double ratio;
	int centerX;
	int centerY;
	double width;
	double height;
	double scrollLength = 1;
	boolean buttonFlag = false;
	boolean scrollFlag = true;
	Slider slider = new Slider(0.5, 3, scrollLength);
	public static int SCENE_HIGHT = 558;
	public static int SCENE_WIGHT = 775;
	Scene scene;
	public static AnchorPane anchorPane;
	public static Stage primaryStage;

	public static Button button1 = new Button("缩放");

	public EnlargeAction(File imageFile)
	{
		button1.setId("zoom");
		button1.setPrefSize(50, 40);
		this.imageFile = imageFile;

		anchorPane = new AnchorPane();
		anchorPane.setId("anchorpane");
		scene = new Scene(anchorPane, SCENE_WIGHT, SCENE_HIGHT);
		scene.getStylesheets().add("myCSS.css");
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.setTitle("缩放");
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				// TODO Auto-generated method stub
				buttonFlag = false;
				scrollFlag = true;
				scrollLength = 1;
			}
		});
		try
		{

			imageView.setImage(new Image(imageFile.toURI().toString()));

		} catch (Exception e)
		{
			// TODO: handle exception
		}
		button1.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (buttonFlag)
				{
					buttonFlag = false;
					scrollFlag = false;
					button1.setText("显示");
					slider.setVisible(buttonFlag);
				} else
				{
					buttonFlag = true;
					scrollFlag = false;
					button1.setText("隐藏");
					slider.setVisible(buttonFlag);
				}
				// TODO Auto-generated method stub
				if (getSlider() != null)
				{
					anchorPane.getChildren().remove(getSlider());
				}
				slider.setPrefWidth(200.0);
				slider.setShowTickMarks(true);
				slider.setShowTickLabels(true);
				slider.setMajorTickUnit(0.5);
				slider.setId("slider");
				AnchorPane.setLeftAnchor(slider, 50.0);
				AnchorPane.setTopAnchor(slider, 60.0);
				anchorPane.getChildren().addAll(slider);
				try
				{
					BufferedImage curImage = ImageIO.read(imageFile);
					imageWidth = curImage.getWidth();
					imageHeight = curImage.getHeight();
					ratio = (double) imageWidth / imageHeight;

				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageView.setPreserveRatio(true);
				imageView.setFitWidth(SCENE_WIGHT / 3 * scrollLength);
				imageView.setFitHeight(SCENE_HIGHT / 3 * scrollLength);
				if (ratio > 1)
				{
					width = MainAction.SCENE_WIGHT / 3;
					height = width / ratio;

				} else
				{
					height = MainAction.SCENE_HIGHT / 3;
					width = height * ratio;

				}
				AnchorPane.setLeftAnchor(imageView, SCENE_WIGHT / 2 - scrollLength * width / 2);
				AnchorPane.setTopAnchor(imageView, SCENE_HIGHT / 2 - scrollLength * height / 2);

				enlargeImage();
			}
		});

		anchorPane.setOnScroll(new EventHandler<ScrollEvent>()
		{

			@Override
			public void handle(ScrollEvent event)
			{
				// TODO Auto-generated method stub
				if (buttonFlag && !scrollFlag)
				{
					buttonFlag = false;
					button1.setText("显示");
				} else
				{
					buttonFlag = true;
					scrollFlag = true;
					button1.setText("隐藏");
					slider.setVisible(buttonFlag);
				}
				if (getSlider() != null)
				{
					anchorPane.getChildren().remove(getSlider());
				}

				slider.setValue(scrollLength);
				slider.setPrefWidth(200.0);
				slider.setShowTickMarks(true);
				slider.setShowTickLabels(true);
				slider.setMajorTickUnit(0.5);
				AnchorPane.setLeftAnchor(slider, 50.0);
				AnchorPane.setTopAnchor(slider, 60.0);
				anchorPane.getChildren().addAll(slider);
				enlargeImage();
				try
				{
					BufferedImage curImage = ImageIO.read(imageFile);
					imageWidth = curImage.getWidth();
					imageHeight = curImage.getHeight();
					ratio = (double) imageWidth / imageHeight;

				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageView.setPreserveRatio(true);
				imageView.setFitWidth(SCENE_WIGHT / 3);
				imageView.setFitHeight(SCENE_HIGHT / 3);
				if (ratio > 1)
				{
					width = SCENE_WIGHT / 3;
					height = width / ratio;
				} else
				{
					height = SCENE_HIGHT / 3;
					width = height * ratio;
				}

				if (event.getDeltaY() > 0)
				{
					scrollLength = scrollLength + 0.2;
					if (scrollLength > 3)
						scrollLength = 3;
				} else
				{
					scrollLength = scrollLength - 0.2;
					if (scrollLength < 0.5)
						scrollLength = 0.5;
				}
				toShowImage(imageView, scrollLength);
			}
		});
		try
		{
			BufferedImage curImage = ImageIO.read(imageFile);
			imageWidth = curImage.getWidth();
			imageHeight = curImage.getHeight();
			ratio = (double) imageWidth / imageHeight;
		} catch (IOException e)
		{

			e.printStackTrace();
		}
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(SCENE_WIGHT / 3);
		imageView.setFitHeight(SCENE_HIGHT / 3);
		if (ratio > 1)
		{
			width = SCENE_WIGHT / 3;
			height = width / ratio;
		} else
		{
			height = SCENE_HIGHT / 3;
			width = height * ratio;
		}
		AnchorPane.setLeftAnchor(button1, 100.0);
		AnchorPane.setLeftAnchor(imageView, SCENE_WIGHT / 2 - width / 2);
		AnchorPane.setTopAnchor(imageView, SCENE_HIGHT / 2 - height / 2);
		anchorPane.getChildren().addAll(button1, imageView);
	}

	public void toShowImage(ImageView imageView, Number multiple)
	{
		if (imageView != null)
		{
			anchorPane.getChildren().remove(imageView);
		}

		imageView.setPreserveRatio(true);
		imageView.setFitWidth(multiple.doubleValue() * SCENE_WIGHT / 3);
		imageView.setFitHeight(multiple.doubleValue() * SCENE_HIGHT / 3);
		AnchorPane.setLeftAnchor(imageView, SCENE_WIGHT / 2 - multiple.doubleValue() * width / 2);
		AnchorPane.setTopAnchor(imageView, SCENE_HIGHT / 2 - multiple.doubleValue() * height / 2);

		anchorPane.getChildren().add(imageView);
	}

	public void enlargeImage()
	{

		if (slider != null)
		{
			slider.valueProperty().addListener(new ChangeListener<Number>()
			{

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
				{

					scrollLength = newValue.doubleValue();
					toShowImage(imageView, newValue);
				}
			});
		}
	}

	public Button getButton1()
	{
		return button1;
	}

	public Slider getSlider()
	{
		return this.slider;
	}
}
