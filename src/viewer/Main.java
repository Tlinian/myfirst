package viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        
        primaryStage.setTitle("Picture Viewer");
        this.primaryStage.getIcons().add(new Image("file:resources/images/title.png"));
        initRootLayout();
        showPictureOverview();
    }

    /**
     * description: 初始化外层骨架
     * @param
     * @return void
     */
    public void initRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane)loader.load();

            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add("myCSS.css");
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(637);
            primaryStage.setMinWidth(1057);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * description: 装载内部细节
     * @param
     * @return void
     */
    public void showPictureOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PictureOverview.fxml"));
            AnchorPane pictureOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(pictureOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
