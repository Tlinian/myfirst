package viewer.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import viewer.controller.PictureOverviewController;
import viewer.model.ImagePreViewItem;
import viewer.action.FilterAction;

public class FilterviewController  implements Initializable
{
	//主界面图片
	@FXML
	private ImageView imageview;
	
	//滤镜效果图片
	@FXML
	private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
	//放大缩小滚动条
	@FXML
	private Slider slider;
	@FXML
	Button SaveButton;//保存副本
	@FXML
	Button saveOther;//保存
	//按钮
	@FXML
	private Button test;
	@FXML
	private ProgressIndicator indicator;
	@FXML
	private Label savelabel;
	@FXML
	private AnchorPane Existpane;
	@FXML
	private BorderPane borderpane;
	
	@FXML
	private Button big, small;

	@FXML
	private void Origin(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.0);
		imageview.setEffect(null);
	}

	@FXML
	private void Overlay(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		Blend blend = new Blend();
		//设置滤镜，blend为一种两种输入混合的效果
		blend.setMode(BlendMode.OVERLAY);	
		imageview.setEffect(blend);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			blend.setOpacity(new_val.doubleValue());
		});
	}
	
	@FXML
	private void Sepiatone(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		//SepiaTone 为一种类似古色古香的色调效果
		SepiaTone effect = new SepiaTone(0.5);
		imageview.setEffect(effect);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			effect.setLevel(new_val.doubleValue());
		});
	}
	@FXML
	private void Glow(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		//Bloom为使输入图像的较亮部分显示为辉光的效果
		Glow glow = new Glow(0.8);
		imageview.setEffect(glow);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			glow.setLevel(new_val.doubleValue());
		});
	}

	@FXML
	private void Mercury(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		ColorAdjust colorAdjust = new ColorAdjust();
		 colorAdjust.setContrast(0.1);
		 colorAdjust.setHue(-0.05);
		 colorAdjust.setBrightness(0.1);
		 colorAdjust.setSaturation(0.2);
		imageview.setEffect(colorAdjust);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			colorAdjust.setSaturation(new_val.doubleValue());
		});
	}

	@FXML
	private void Exclusion(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		Blend blend = new Blend();
		blend.setMode(BlendMode.EXCLUSION);
		blend.setOpacity(0.5);
		imageview.setEffect(blend);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			blend.setOpacity(new_val.doubleValue());
		});
	}
	@FXML
	private void Arctic(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		ColorAdjust color = new ColorAdjust();
		color.setHue(-0.6);
		Blend blend = new Blend();
		blend.setMode(BlendMode.OVERLAY);
		color.setInput(blend);
		imageview.setEffect(color);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			blend.setOpacity(new_val.doubleValue());
		});
	}

	@FXML
	private void Denim(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		ColorAdjust color = new ColorAdjust();
		color.setSaturation(-1.0);
		Blend blend = new Blend();
		blend.setMode(BlendMode.OVERLAY);
		color.setInput(blend);
		imageview.setEffect(color);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			blend.setOpacity(new_val.doubleValue());
		});
	}

	@FXML
	private void Neo(ActionEvent e) {
		if (test != null) {
			test.setStyle("-fx-border-color: #515456;-fx-background-color:  #515456;");
		}
		test = ((Button) e.getSource());
		((Button) e.getSource()).setStyle("-fx-border-color:  #42ff51;-fx-background-color:  #515456;");
		slider.setOpacity(0.5);
		slider.setValue(0.5);
		ColorAdjust color = new ColorAdjust();
		color.setHue(0.6);
		Blend blend = new Blend();
		blend.setMode(BlendMode.OVERLAY);
		color.setInput(blend);
		imageview.setEffect(color);
		slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
			blend.setOpacity(new_val.doubleValue());
		});
	}
	
	public void setImageViewImage(ImageView image) {
		image.setImage(FilterAction.imageview.getImage());
		image.setEffect(FilterAction.imageview.getEffect());
		image.setViewport(FilterAction.imageview.getViewport());
		image.setNodeOrientation(FilterAction.imageview.getNodeOrientation());
		image.setRotate(FilterAction.imageview.getRotate());
	}

	private void setImageViewEffect() {
		this.setImageViewImage(image1);
		this.setImageViewImage(image2);
		this.setImageViewImage(image3);
		this.setImageViewImage(image4);
		this.setImageViewImage(image5);
		this.setImageViewImage(image6);
		this.setImageViewImage(image7);
		this.setImageViewImage(image8);
		this.setImageViewImage(image9);
		image1.setEffect(null);
		Blend blend = new Blend();
		blend.setMode(BlendMode.OVERLAY);
		blend.setOpacity(0.5);
		image2.setEffect(blend);
		SepiaTone sep = new SepiaTone(0.5);
		image3.setEffect(sep);
		Glow glow = new Glow(0.8);
		image4.setEffect(glow);
		ColorAdjust colorAdjust = new ColorAdjust();
		 colorAdjust.setContrast(0.1);
		 colorAdjust.setHue(-0.05);
		 colorAdjust.setBrightness(0.1);
		 colorAdjust.setSaturation(0.2);
		image5.setEffect(colorAdjust);
		Blend blend2 = new Blend();
		blend2.setMode(BlendMode.EXCLUSION);
		blend2.setOpacity(0.5);
		image6.setEffect(blend2);
		ColorAdjust color2 = new ColorAdjust();
		color2.setHue(-0.6);
		color2.setInput(blend);
		image7.setEffect(color2);
		ColorAdjust color3 = new ColorAdjust();
		color3.setSaturation(-1.0);
		color3.setInput(blend);
		image8.setEffect(color3);
		ColorAdjust color4 = new ColorAdjust();
		color4.setHue(0.6);
		color4.setInput(blend);
		image9.setEffect(color4);
	}

	public void setImage() {
		imageview.setImage(FilterAction.imageview.getImage());
		imageview.setEffect(FilterAction.imageview.getEffect());
		imageview.setViewport(FilterAction.imageview.getViewport());
		imageview.setNodeOrientation(FilterAction.imageview.getNodeOrientation());
		imageview.setRotate(FilterAction.imageview.getRotate());
		setImageViewEffect();
		
	}
	
	@FXML
	private void Close(ActionEvent e) {
		FilterAction.stage.close();
	}

	//显示保存框
	public class SaveTask extends Task<Integer> {

		@Override
		protected Integer call() throws Exception {

			for (int i = 0; i < 250; i++) {
				updateProgress(i, 250);
				Thread.sleep(5);
			}
			return 1;
		}
	}

	//保存副本
	@FXML
	private void Copy(ActionEvent event) {
		File file = FilterAction.file;
		System.out.println(file.getParentFile().getAbsolutePath());
		//列出当前目录的所有文件
        File[] images = (new File(file.getParentFile().getAbsolutePath())).listFiles(
                pathname -> {
                    if (pathname.isFile()) {
                        String name = pathname.getName().toLowerCase();
                        if (name.endsWith(".jpg") || name.endsWith(".jpge") || name.endsWith(".gif")
                                || name.endsWith(".png") || name.endsWith("bmp")) {
                            return true;
                        }
                    }
                    return false;
                }
        );
		if (file.exists()) {
			Task<Integer> task = new SaveTask();
			savelabel.visibleProperty().bind(task.runningProperty());
			indicator.visibleProperty().bind(task.runningProperty());
			new Thread(task).start();
			WritableImage image = imageview.snapshot(new SnapshotParameters(), null);
			String copyfilepath = null;
			String filename = file.getName();
			String fileParentPath = file.getParent();
			String name1 = filename.substring(0, filename.lastIndexOf("."));
			System.out.println(name1);
			int a = name1.lastIndexOf("(");
			int b = name1.lastIndexOf(")");
			int n;
			//创建名字
			if (a != -1 && b != -1) {
				String index = name1.substring(name1.lastIndexOf("(") + 1, name1.lastIndexOf(")"));
				if (index != "" && index != null) {
					n = Integer.valueOf(index);
					n++;
					copyfilepath = fileParentPath + "\\" + name1 + "(" + n + ").jpg";
					while(findSameName(copyfilepath,images))
					{
						n++;
						copyfilepath = fileParentPath + "\\" + name1 + "(" + n + ").jpg";
					}	
				}
			} else {
				n=1;
				copyfilepath = fileParentPath + "\\" + name1 + "(" + n + ").jpg";
				while(findSameName(copyfilepath,images))
				{
					n++;
					copyfilepath = fileParentPath + "\\" + name1 + "(" + n + ").jpg";
				}
			}
			File files = new File(copyfilepath);
			//把文件添加到相应目录中
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", files);
				FilterAction.flowpane.getChildren().add(new ImagePreViewItem(files, new PictureOverviewController()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			FilterAction.imageview=imageview;
		} else {
			Existpane.setVisible(true);
		}
	}

	//判断是否重名
	private boolean findSameName(String name, File[] files) {
        for (File item:files) {
            if (name.equals(item.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }
	//保存
	@FXML
	private void Save(ActionEvent event) {
		File file = FilterAction.file;
		if (file.exists()) {
			Task<Integer> task = new SaveTask();
			FilterAction.flowpane.getChildren().remove(FilterAction.imagepreview);
			savelabel.visibleProperty().bind(task.runningProperty());
			indicator.visibleProperty().bind(task.runningProperty());
			new Thread(task).start();
			WritableImage image = imageview.snapshot(new SnapshotParameters(), null);
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				
				FilterAction.imageview=imageview;
				FilterAction.flowpane.getChildren().add(new ImagePreViewItem(file, new PictureOverviewController()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Existpane.setVisible(true);
		}
	}

	//便于鼠标调控面板来控制图片大小
	@FXML
	private void Scroll(ScrollEvent e) {
		if (imageview.getBoundsInParent().getWidth() >= imageview.getFitWidth() * 2.5) {
			big.setDisable(true);
			big.setOpacity(0.6);
			if (e.getDeltaY() < 0) {
				scrollcount += (int) (e.getDeltaY() / 26);
				big.setDisable(false);
				big.setOpacity(1.0);
				imageview.setScaleX(1 + count * 0.1 + scrollcount * 0.1);
				imageview.setScaleY(1 + count * 0.1 + scrollcount * 0.1);
			}
		} else if (imageview.getBoundsInParent().getWidth() < slider.getWidth()) {
			small.setDisable(true);
			small.setOpacity(0.6);
			if (e.getDeltaY() > 0) {
				scrollcount += (int) (e.getDeltaY() / 26);
				small.setDisable(false);
				small.setOpacity(1.0);
				imageview.setScaleX(1 + count * 0.1 + scrollcount * 0.1);
				imageview.setScaleY(1 + count * 0.1 + scrollcount * 0.1);
			}
		} else {
			scrollcount += (int) (e.getDeltaY() / 26);
			imageview.setScaleX(1 + count * 0.1 + scrollcount * 0.1);
			imageview.setScaleY(1 + count * 0.1 + scrollcount * 0.1);
		}
	}

	private int count = 0;
	private int scrollcount = 0;
	
	//放大
	@FXML
	private void Big(ActionEvent e) {
		if (imageview.getBoundsInParent().getWidth() >= imageview.getFitWidth() * 2.5) {
			big.setDisable(true);
			big.setOpacity(0.6);
		} else {
			count++;
			small.setDisable(false);
			small.setOpacity(1.0);
			imageview.setScaleX(1 + count * 0.1 + scrollcount * 0.1);
			imageview.setScaleY(1 + count * 0.1 + scrollcount * 0.1);
		}
	}

	//缩小
	@FXML
	private void Small(ActionEvent e) {
		if (imageview.getBoundsInParent().getWidth() < slider.getWidth()) {
			small.setDisable(true);
			small.setOpacity(0.6);
		} else {
			count--;
			imageview.setScaleX(1 + count * 0.1 + scrollcount * 0.1);
			imageview.setScaleY(1 + count * 0.1 + scrollcount * 0.1);
			big.setDisable(false);
			big.setOpacity(1.0);
		}
	}

	@FXML
	private AnchorPane leftpane, rightpane;
	@FXML
	private HBox toppane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.setImage();
		FilterAction.stage.widthProperty().addListener((a) -> {
			imageview.setScaleX(1.0);
			imageview.setScaleY(1.0);
		});
		FilterAction.stage.heightProperty().addListener((a) -> {
			imageview.setScaleX(1.0);
			imageview.setScaleY(1.0);
		});
		imageview.fitWidthProperty()
		.bind(FilterAction.stage.widthProperty().subtract(rightpane.widthProperty()).divide(4).multiply(3));
      imageview.fitHeightProperty()
		.bind(FilterAction.stage.heightProperty().subtract(toppane.heightProperty()).divide(4).multiply(3));
      slider.prefWidthProperty().bind(imageview.fitWidthProperty().divide(4).multiply(3));
	}
}
