package viewer.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import viewer.constants.ImagePreviewConstant;
import viewer.controller.PictureOverviewController;
import viewer.utils.ConvertUtil;

import java.io.File;

/**
 * Created by PanD
 */

public class ImagePreViewItem extends VBox {

    private File imageFile;

    private Image image;

    private PictureOverviewController pictureOverviewController;

  //VBOX中的组件 -------------------------------------------------------------------
    private Canvas canvas;

    private Label nameLabel;

    private SimpleBooleanProperty isSelected;

  //缩略图大小 ----------------------------------------------------------------
    private SimpleDoubleProperty imageWidth;
    private SimpleDoubleProperty imageHeight;

    private SimpleDoubleProperty mediateWidth;
    private SimpleDoubleProperty mediateHeight;
  //初始化 -------------------------------------------------------------------
    public ImagePreViewItem(File imageFile, PictureOverviewController pictureOverviewController) {
        super();

        imageWidth = new SimpleDoubleProperty();
        imageHeight = new SimpleDoubleProperty();
        mediateWidth = new SimpleDoubleProperty();
        mediateHeight = new SimpleDoubleProperty();

        imageWidth.bind(mediateWidth);
        imageHeight.bind(mediateHeight);
        this.mediateHeight.setValue(ImagePreviewConstant.MEDIUM_HEIGHT);
        this.mediateWidth.setValue(ImagePreviewConstant.MEDIUM_WIDTH);

        this.imageFile = imageFile;
        this.pictureOverviewController = pictureOverviewController;

        nameLabel = new Label();
        isSelected = new SimpleBooleanProperty(false);

        initImagePreview();
        initMouseEvent();
        initPropertyListener();
    }

    /**
     * description: 初始化缩略图
     *
     * @param
     * @return void
     */
    private void initImagePreview() {
        canvas = new Canvas(imageWidth.getValue(), imageHeight.getValue());
        canvas.widthProperty().bind(imageWidth);
        canvas.heightProperty().bind(imageHeight);

        adjustSize(imageWidth.getValue(), imageHeight.getValue());

        nameLabel.setText(imageFile.getName());
        nameLabel.setPrefWidth(imageWidth.getValue());
        nameLabel.prefWidthProperty().bind(imageWidth);
        nameLabel.setPrefHeight(20);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTooltip(new Tooltip(imageFile.getName()));

        this.getChildren().add(canvas);
        this.getChildren().add(nameLabel);
    }

    /**
     * description: 为缩略图添加鼠标事件
     *
     * @param
     * @return void
     */
    private void initMouseEvent() {
    	//悬浮
        this.setOnMouseEntered(event -> {
        	 //内部类方法使用外部类，需要使用 外部类名.this 进行映射
            //渐变使用：ImagePreViewItem.this.setStyle("-fx-background-color:linear-gradient(to bottom,#000000 1%,  #ffffff 98%);");
           if (isIsSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:#E6E6E6;");
            }
        });

        //离开
        this.setOnMouseExited(event -> {
            if (isIsSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
            }
        });
        
        //选中
        this.setOnMouseClicked(event -> {
            PictureOverviewController parentController = ImagePreViewItem.this.pictureOverviewController;
          //左键
            if (event.getClickCount() == 1 && (event.getButton() == MouseButton.PRIMARY)) {
            	 //没有ctrl进行多选
            	
                if (event.isControlDown() == false&&!isIsSelected()) {//修改了已选单个图片重选无法取消已选的问题
                	
                          clearAllSelected();
                  
                }
              //单击，会取消掉其他的选择状态
                if (!isIsSelected() ) {
                    setIsSelected(true);
                   //
                    parentController.selectedImagePreviewListProperty().add(ImagePreViewItem.this);
                } else if (isIsSelected())  {
                    setIsSelected(false);
                    parentController.selectedImagePreviewListProperty().remove(ImagePreViewItem.this);
                }
            }

          //双击左键(打开当前目录全部图片
            if (event.getClickCount() == 2 && (event.getButton() == MouseButton.PRIMARY)) {
                parentController.imageViewSerivce.openImageViewStage(
                        ConvertUtil.simpleArrayListPropertyToList(parentController.imagePreviewListProperty()),
                                ImagePreViewItem.this.imageFile,pictureOverviewController.getPreviewPane(),this
                );
            }

          //右键
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {
            	 //右键点击的地方不是选中的文件则相当于 鼠标左键单击后右键打开菜单
                if (parentController.selectedImagePreviewListProperty().contains(event.getSource()) == false){
                    clearAllSelected();
                    setIsSelected(true);
                    parentController.selectedImagePreviewListProperty().add(ImagePreViewItem.this);
                }
            }
        });
    }

    /**
     * description: 对部分属性设置监听
     * @param
     * @return void
     */
    private void initPropertyListener() {
        //瀵规槸鍚﹁閫変腑杩涜鐩戝惉
        isSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isIsSelected() == true) {
                    ImagePreViewItem.this.setStyle("-fx-background-color:#c6c6c6;");
                } else {
                    ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
                }
            }
        });
    }

    /**
     * description: 娓呴櫎褰撳墠閫変腑鐨勫浘鐗�
     * @param
     * @return void
     */
    public void clearAllSelected() {//改成public
        //娓呯┖閫夋嫨
        ImagePreViewItem.this.pictureOverviewController
                .selectedImagePreviewListProperty().clear();
        //灏嗘墍鏈夊浘鐗囪缃负 鏈�変腑
        ImagePreViewItem.this.pictureOverviewController
                .getImagePreviewList().forEach(loadedImage -> {
            loadedImage.setIsSelected(false);
        });
    }

//鐗规�ф柟娉� ----------------------------------------------------------------------------

    /**
     * description: 閲嶅懡鍚�
     * @param newName
     * @return boolean
     */
    public boolean rename(String newName) {
        String absName = imageFile.getParent() + "/" + newName;
        File dest = new File(absName);
        if (imageFile.renameTo(dest)) {
            imageFile = dest;
            this.nameLabel.setText(imageFile.getName());

            return true;
        }
        return false;
    }

    /**
     * description: 璋冩暣缂╃暐鍥惧ぇ灏�
     * @param width
 * @param heigt
     * @return void
     */
    public void adjustSize(double width, double heigt) {
        this.mediateHeight.setValue(heigt);
        this.mediateWidth.setValue(width);
        canvas.getGraphicsContext2D().clearRect(0,0,width,heigt);
        image = new Image("file:" + imageFile.getPath(),
                imageWidth.getValue() - 10, imageHeight.getValue() - 10,
                true, true);
        canvas.getGraphicsContext2D().drawImage(image,
                (imageWidth.getValue() - image.getWidth()) / 2,
                (imageHeight.getValue() - image.getHeight()) / 2);
    }


//getter & setter -------------------------------------------------------------------
    public double getMediateWidth() {
    	return mediateWidth.doubleValue();
    }
    public double getMediateHeight() {
    	return mediateHeight.doubleValue();
    }
    
    
    
    
    public boolean isIsSelected() {
        return isSelected.get();
    }

    public SimpleBooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth.set(imageWidth);
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight.set(imageHeight);
    }

    public Image getImage() {
        return image;
    }
}
