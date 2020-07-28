package viewer.service.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import viewer.Main;
import viewer.constants.ImageVIewStageConstant;
import viewer.controller.ImageViewController;
import viewer.model.ImagePreViewItem;
import viewer.service.ImageViewSerivce;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by PanD
 */

public class ImageViewSerivceImpl implements ImageViewSerivce {

    @Override
    public void openImageViewStage(List<File> imageList, File firstFile,FlowPane flowpane,ImagePreViewItem imagePreViewItem) {
        //stage属性设置
        Stage imagerViewStage = new Stage();
        imagerViewStage.setTitle("Picture Viewer");
        imagerViewStage.getIcons().add(new Image("file:resources/images/title.png"));
        imagerViewStage.setMinWidth(ImageVIewStageConstant.STAGE_MIN_WIDTH);
        imagerViewStage.setMinHeight(ImageVIewStageConstant.STAGE_MIN_HEIGHT);        imagerViewStage.setWidth(ImageVIewStageConstant.STAGE_PRE_WIDTH);
        imagerViewStage.setHeight(ImageVIewStageConstant.STAGE_PRE_HEIGHT);

        //加载控制器
        ImageViewController imageViewController = new ImageViewController(imageList, firstFile,flowpane,imagePreViewItem);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ImageView.fxml"));

            loader.setController(imageViewController);
            AnchorPane pictureOverview = (AnchorPane) loader.load();

            Scene scene = new Scene(pictureOverview);
            scene.getStylesheets().add("myCSS.css");
            imagerViewStage.setScene(scene);

            imageViewController.setParentStage(imagerViewStage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagerViewStage.show();
    }
}
