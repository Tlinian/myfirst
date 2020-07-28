package viewer.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.StringConverter;
import viewer.constants.ImagePreviewConstant;
import viewer.model.DirTreeItem;
import viewer.model.ImagePreViewItem;
import viewer.model.SizeChoiceBoxItem;
import viewer.service.FileOperationService;
import viewer.service.ImageViewSerivce;
import viewer.service.RotateService;
import viewer.service.ServiceFactory;
import viewer.service.SlideService;
import viewer.utils.ConvertUtil;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * Created by PanD
 */

public class PictureOverviewController {
	//TODO 新粘贴的图片大小需要调整
    public FileOperationService fileOperationService = ServiceFactory.getFileOperationService();
    public ImageViewSerivce imageViewSerivce = ServiceFactory.getImageViewSerivce();
    public SlideService slideService = ServiceFactory.getSlideService();
    public RotateService rotateService  =  ServiceFactory.getRotateService();
    
    @FXML
    private ScrollPane scrollPane;
  //图片预览处的Pane
    @FXML
    private FlowPane previewPane;
    @FXML
    private AnchorPane previewContainer;
    @FXML
    private BorderPane scrollPaneContainer;
    //目录树
    @FXML
    private TreeView<File> dirTree;

  //上下文菜单
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem renameMenuItem;
    @FXML
    private MenuItem selectAllMenuItem;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem openMenuItem;

  //页面下方说明选中的Label
    @FXML
    private Label stateLabel;
  //路径导航栏的Label
    @FXML
    private Label pathLabel;
  //幻灯片播放
    @FXML
    private Button slidPlayButton;
  //返回上级目录
    @FXML
    private Button backToParentFileButton;
    @FXML
    private ChoiceBox<SizeChoiceBoxItem> sizeChoiceBox;

    private ObservableList<SizeChoiceBoxItem> sizeChoice;

  //被选中的目录
    private SimpleObjectProperty<File> selectedDir;

  //当前目录载入的缩略图
    private SimpleListProperty<ImagePreViewItem> imagePreviewList;
  //当前已经选择的图片
  private SimpleListProperty<ImagePreViewItem> selectedImagePreviewList;
//待剪切的图片
    private SimpleListProperty<ImagePreViewItem> cutedImageList;
    //拖拽产生的矩形框
    private Rectangle rectangle;
    private SimpleDoubleProperty rectangleStartX;
    private SimpleDoubleProperty rectangleStartY;
    private SimpleDoubleProperty rectangleLayoutX;
    private SimpleDoubleProperty rectangleLayoutY;
    private SimpleDoubleProperty rectangleWidth;
    private SimpleDoubleProperty rectangleHeight;
    
    
  //初始化-----------------------------------------------------------------------------------
    @FXML
    public void initialize() {

        this.imagePreviewList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedImagePreviewList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.cutedImageList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedDir = new SimpleObjectProperty<File>();
        this.rectangle = new Rectangle();
        this.rectangleStartX = new SimpleDoubleProperty();
        this.rectangleStartY = new SimpleDoubleProperty();
        this.rectangleLayoutX = new SimpleDoubleProperty();
        this.rectangleLayoutY = new SimpleDoubleProperty();
        this.rectangleWidth = new  SimpleDoubleProperty();
        this.rectangleHeight = new  SimpleDoubleProperty();
        this.sizeChoice = FXCollections.observableArrayList();
//        this.scrollPane.setMaxWidth(scrollPaneContainer.getWidth());

        initSizeChoiceBox();
        initDirTree();
        initPreview();
        initListener();

    }

    /**
     * description: 目录树的初始化
     * @param
     * @return void
     */
    public void initDirTree() {
        DirTreeItem root = new DirTreeItem(new File("root"), true);
        root.load();
        root.setExpanded(true);

        dirTree.setRoot(root);
        dirTree.setShowRoot(false);
        dirTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

      //加载根节点的子节点，例如C:、D:..
        for (TreeItem<File> item :  root.getChildren()) {
            ((DirTreeItem) item).load();
        }
        //设置初始TreeView单元样式---------------------------------------------------------------
        dirTree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        setFont(new Font("Microsoft YaHei", 14.0));
                        if (!empty) {
                            ImageView icon = null;
                            if (!this.getTreeItem().isExpanded()) {
                                if (((DirTreeItem) getTreeItem().getParent()).isRoot()) {
                                    icon = new ImageView(new Image("file:resources/images/portable-power-solid.png", 16, 16, true, true)); // 纾佺洏
                                } else {
                                    icon = new ImageView(new Image("file:resources/images/folder-solid.png", 16, 16, true, true)); // 纾佺洏
                                }
                            } else {
                                if (((DirTreeItem) getTreeItem().getParent()).isRoot()) {
                                    icon = new ImageView(new Image("file:resources/images/portable-power-solid (1).png", 16, 16, true, true)); // 纾佺洏
                                } else {
                                    icon = new ImageView(new Image("file:resources/images/folder-open-solid.png", 16, 16, true, true)); // 纾佺洏
                                }
                            }

                            setGraphic(icon);
                            String name = FileSystemView.getFileSystemView().getSystemDisplayName(item);
                            setText(name);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

      //选项选中事件----------------------------------------------------------------------
        dirTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue,
                                TreeItem<File> newValue) {
                if (newValue == null) {
                    return;
                }
              //observable.getValue().getValue()为选中的目录
                
                setSelectedDir(observable.getValue().getValue());
            }
        });

    }

    /**
     * description: 图片预览区域的初始化
     * @param
     * @return void
     */
    public void initPreview() {
        previewPane.prefWidthProperty().bind(scrollPane.widthProperty());
        pathLabel.setText("");
        stateLabel.setText("");
    }

    /**
     * description: 初始化选择预览图大小的choicebox
     * @param
     * @return void
     */
    private void initSizeChoiceBox() {
        sizeChoice.add(new SizeChoiceBoxItem("大图标", ImagePreviewConstant.BIG_WIDTH, ImagePreviewConstant.BIG_HEIGHT));
        sizeChoice.add(new SizeChoiceBoxItem("中等图标", ImagePreviewConstant.MEDIUM_WIDTH, ImagePreviewConstant.MEDIUM_HEIGHT));
        sizeChoice.add(new SizeChoiceBoxItem("小图标", ImagePreviewConstant.SMALL_WIDTH, ImagePreviewConstant.SMALL_HEIGHT));
        sizeChoiceBox.getItems().addAll(sizeChoice);
        sizeChoiceBox.getSelectionModel().select(1);
        sizeChoiceBox.converterProperty().set(new StringConverter<SizeChoiceBoxItem>() {
            @Override
            public String toString(SizeChoiceBoxItem object) {
                return object.getName();
            }

            @Override
            public SizeChoiceBoxItem fromString(String string) {
                return null;
            }
        });
    }

    /**
     * description: 载入当前目录的图片的缩略图
     * @param images
     * @return void
     */
    private void loadPicture(File[] images) {
        imagePreviewList.clear();
        for (File image : images) {
            ImagePreViewItem ipItem = new ImagePreViewItem(image, this);
            imagePreviewList.add(ipItem);

            //在页面载入缩略图
            this.getPreviewPane().getChildren().add(ipItem);
        }
    }

  //监听 ------------------------------------------------------------------------------------

    /**
     * description: 负责大部分监听的初始化
     * @param
     * @return void
     */
    private void initListener() {
        pathLabelListener();
        selectImageListener();
        rectangeListener();
        sizeChoiceBoxListener();
    }

    /**
     * description: 对路径进行监听
     * @param
     * @return void
     */
    private void pathLabelListener() {
        selectedDir.addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                refresh();
            }
        });
    }

    /**
     * description: 对预览图片区域鼠标事件的监听
     * @param
     * @return void
     */
    private void selectImageListener() {

        previewPane.setOnMouseClicked(event -> {

            if (event.getPickResult().getIntersectedNode() == previewPane) {
            	//点击空白位置
                //点击左键取消掉所有选中
                if (event.getButton() == MouseButton.PRIMARY){
                    clearSelected();
                }
              //点击右键打开对应的上下文菜单
                if (event.getButton() == MouseButton.SECONDARY){
                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(pasteMenuItem, selectAllMenuItem);
                    contextMenu.show(previewPane, event.getScreenX(), event.getScreenY());
                }
            } else {
                if (event.getButton() == MouseButton.SECONDARY){
                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(openMenuItem, copyMenuItem, cutMenuItem, renameMenuItem, deleteMenuItem, selectAllMenuItem);
                    contextMenu.show(previewPane, event.getScreenX(), event.getScreenY());
                }
            }

        });

      //监听选中列表，改变左下角 statLabel 的值
        selectedImagePreviewList.addListener(new ChangeListener<ObservableList<ImagePreViewItem>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<ImagePreViewItem>> observable, ObservableList<ImagePreViewItem> oldValue, ObservableList<ImagePreViewItem> newValue) {
                int selected = PictureOverviewController.this.selectedImagePreviewListProperty().size();
                if (selected == 0){
                    stateLabel.setText(String.format("共 %d 张图片 |",PictureOverviewController.this.imagePreviewListProperty().size()));
                } else {
                    stateLabel.setText(String.format("共 %d 张图片 | %d 张被选中 |",PictureOverviewController.this.imagePreviewListProperty().size(), selected));
                }
            }
        });
    }

    /**
     * description: 拖拽产生的矩形框相关的监听
     * @param
     * @return void
     */
    private void rectangeListener() {
        previewContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().name().equals(MouseButton.PRIMARY.name())) {
                    
                    rectangleStartX.set(mouseEvent.getX());
                    rectangleStartY.set(mouseEvent.getY());
                    rectangle.setWidth(0);
                    rectangle.setHeight(0);
                    previewContainer.getChildren().add(rectangle);
                }
            }
        });

        previewContainer.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	
            	 
            	 
                if (event.getButton() == MouseButton.PRIMARY) {
                	 selectedImagePreviewList.clear();
            	 for(int i = 0;i<imagePreviewList.size();i++) {
            		 imagePreviewList.get(i).setIsSelected(false);
            	 }
                	
                	
                    if (event.getX() > rectangleStartX.doubleValue() && event.getY() > rectangleStartY.doubleValue()) {
                    	rectangleLayoutX.set(rectangleStartX.doubleValue());
                        rectangleLayoutY.set(rectangleStartY.doubleValue());
                        AnchorPane.setLeftAnchor(rectangle, rectangleStartX.doubleValue());
                        AnchorPane.setTopAnchor(rectangle, rectangleStartY.doubleValue());
                    } else if (event.getX() > rectangleStartX.doubleValue() && event.getY() < rectangleStartY.doubleValue()) {
                    	rectangleLayoutX.set( rectangleStartX.doubleValue());
                         rectangleLayoutY.set(event.getY());
                        AnchorPane.setLeftAnchor(rectangle, rectangleStartX.doubleValue());
                        AnchorPane.setTopAnchor(rectangle, event.getY());
                    } else if (event.getX() < rectangleStartX.doubleValue() && event.getY() > rectangleStartY.doubleValue()) {
                    	rectangleLayoutX.set(event.getX());
                        rectangleLayoutY.set(rectangleStartY.doubleValue());
                        AnchorPane.setLeftAnchor(rectangle, event.getX());
                        AnchorPane.setTopAnchor(rectangle, rectangleStartY.doubleValue());
                    } else if (event.getX() < rectangleStartX.doubleValue() && event.getY() < rectangleStartY.doubleValue()) {
                    	rectangleLayoutX.set(event.getX());
                        rectangleLayoutY.set(event.getY());
                        AnchorPane.setLeftAnchor(rectangle, event.getX());
                        AnchorPane.setTopAnchor(rectangle, event.getY());
                    }

                    if (event.getX() != rectangleStartX.doubleValue() && event.getY() != rectangleStartY.doubleValue()) {
                    	rectangleWidth.set(Math.abs(event.getX() - rectangleStartX.doubleValue()));
                    	rectangleHeight.set(Math.abs(event.getY() - rectangleStartY.doubleValue()));
                        rectangle.setWidth(Math.abs(event.getX() - rectangleStartX.doubleValue()));
                        rectangle.setHeight(Math.abs(event.getY() - rectangleStartY.doubleValue()));
                        rectangle.setFill(Paint.valueOf("#AEEEEE"));
                        rectangle.setStroke(Paint.valueOf("#DC143C"));
                        rectangle.setOpacity(0.5);
                    }
                       
                   for(int i=0;i<imagePreviewList.size();i++) {
                	  
                	   ImagePreViewItem   item =imagePreviewList.get(i);
                	   
                	     double itemx1 = item.getLayoutX();
                	     double itemy1 = item.getLayoutY();
                	     double itemx2 =item.getLayoutX()+item.getMediateWidth();
                	     double itemy2 = item.getLayoutY();
                	     double itemx3 = item.getLayoutX();
                	     double itemy3 = item.getLayoutY()+item.getMediateWidth();
                	     double itemx4 = item.getLayoutX()+item.getMediateWidth();
                	     double itemy4 = item.getLayoutY()+item.getMediateHeight();
                	     double recx1=rectangleLayoutX.doubleValue();
                	     double recy1 = rectangleLayoutY.doubleValue();
                	     double recx2 = rectangleLayoutX.doubleValue()+rectangleWidth.doubleValue();
                	     double recy2=	rectangleLayoutY.doubleValue();	 
                	     double recx3= rectangleLayoutX.doubleValue();
                	     double recy3 = rectangleLayoutY.doubleValue()+rectangleHeight.doubleValue();     
                	     double recx4 = rectangleLayoutX.doubleValue()+rectangleWidth.doubleValue();
                	     double recy4 = rectangleLayoutY.doubleValue()+rectangleHeight.doubleValue();
                	     if(isInRectangleMid4(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx4, recy4)&&isInRectangleMid4(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx3, recy3)&&isInRectangleMid3(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx1, recy1)&&(isInRectangleMid3(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx2, recy2))||(isInRectangleMid2(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx4, recy4)&&isInRectangleMid2(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx2, recy2)&&isInRectangleMid(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx3, recy3)&&isInRectangleMid(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx1, recy1))||isInRectangle(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx1, recy1)||isInRectangle(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx2, recy2)||isInRectangle(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx3, recy3)||isInRectangle(itemx1, itemy1, itemx2, itemy2, itemx3, itemy3, itemx4, itemy4, recx4, recy4)||(itemx1<recx1&&itemy1<recy1&&itemx2>recx2&&itemy2<recy2&&itemx3<recx3&&itemy3>recy3&&itemx4>recx4&&itemy4>recy4)||(itemx1>recx1&&itemy1>recy1&&itemx1<recx4&&itemy1<recy4)||(itemx2<recx2&&itemy2>recy2&&itemx2>recx3&&itemy2<recy3)||(itemx3<recx2&&itemy3>recy2&&itemx3>recx3&&itemy3<recy3)||(itemx4>recx1&&itemy4>recy1&&itemx4<recx4&&itemy4<recy4)) {
                	    			
                	    		  if( !item.isIsSelected())
                	    		  {
                	    			  selectedImagePreviewListProperty().add(item);
                	    			  item.setIsSelected(true);
                	    		  }
                	    		
                	     }               	   
                   } 		             
                }
            }
        });

        previewContainer.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	
                if (rectangle != null) {
                	 
                    previewContainer.getChildren().removeAll(rectangle);
                }
            }
        });
    }

    /**
     * description: 监听尺寸选择
     * @param
     * @return void
     */
    private void sizeChoiceBoxListener() {
        sizeChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SizeChoiceBoxItem>() {
            @Override
            public void changed(ObservableValue<? extends SizeChoiceBoxItem> observable, SizeChoiceBoxItem oldValue, SizeChoiceBoxItem newValue) {
                for (ImagePreViewItem image : imagePreviewList) {
                    image.adjustSize(newValue.getWidth(), newValue.getHeight());
                }
            }
        });
    }

  //按钮/菜单Action (为 public 以便 fxml 能够读取) ------------------------------------------------------
    /**
     * description: 按钮：返回上级目录
     * @param
     * @return void
     */
    public boolean isInRectangle(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x,double y) {
    	
  if(x>x1&&x<x2&&x>x3&&x<x4&&y>y1&&y>y2&&y<y3&&y<y4)
    	return true;
  else
	  return false;
    }
    
    public boolean isInRectangleMid(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x,double y) {
    	
    	  if(x<x1&&x<x2&&x<x3&&x<x4&&y>y1&&y>y2&&y<y3&&y<y4)
    	    	return true;
    	  else
    		  return false;
    	    }
    
    public boolean isInRectangleMid2(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x,double y) {
    	
  	  if(x>x1&&x>x2&&x>x3&&x>x4&&y>y1&&y>y2&&y<y3&&y<y4)
  	    	return true;
  	  else
  		  return false;
  	    }
  
    public boolean isInRectangleMid3(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x,double y) {
    	
    	  if(x>x1&&x<x2&&x>x3&&x<x4&&y<y1&&y<y2&&y<y3&&y<y4)
    	    	return true;
    	  else
    		  return false;
    	    }
    public boolean isInRectangleMid4(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x,double y) {
    	
  	  if(x>x1&&x<x2&&x>x3&&x<x4&&y>y1&&y>y2&&y>y3&&y>y4)
  	    	return true;
  	  else
  		  return false;
  	    }
    
    
    
    
    public void backToParentDirectory() {
    	//没有选择目录时返回上级
        if (selectedDir == null || selectedDir.getValue() == null) {
            return;
        }
      //选择目录返回上级时
        if (selectedDir.getValue().getParentFile() != null) {
            setSelectedDir(selectedDir.getValue().getParentFile());
        }
    }

    /**
     * description: 右键菜单：全选
     * @param
     * @return void
     */
    public void menuItemOfSelectAll() {
        clearCuted();
        selectedImagePreviewList.clear();
        imagePreviewList.forEach(imagePreViewItem -> {
            imagePreViewItem.setIsSelected(true);
            selectedImagePreviewList.add(imagePreViewItem);
        });
    }

    /**
     * description: 打开选中的文件
     * @param
     * @return
     */
    public void menuItemOfOpen() {
        clearCuted();

        List<File> selectedFiles = ConvertUtil.simpleArrayListPropertyToList(selectedImagePreviewListProperty());
        imageViewSerivce.openImageViewStage(selectedFiles, selectedFiles.get(0),this.previewPane,selectedImagePreviewList.get(0));
    }

    /**
     * description: 重命名文件
     * @param
     * @return void
     */
    public void menuItemOfRename() {
        clearCuted();
        fileOperationService.rename(selectedImagePreviewList);
    }

    /**
     * description: 复制文件
     * @param
     * @return void
     */
    public void menuItemOfCopy() {
        clearCuted();

        fileOperationService.copy(ConvertUtil.simpleArrayListPropertyToList(selectedImagePreviewList));

        clearSelected();
    }

    /**
     * description: 粘贴文件
     * @param
     * @return void
     */
    public void menuItemOfPaste() {
    	if(cutedImageList.size()!=0)
    	if(cutedImageList.get(0).getImageFile().getParentFile().getAbsolutePath().equals(selectedDir.getValue().getPath())) {
        	refresh();
            clearCuted();
            clearSelected();
            return;
        }
        List<File> pasteFiles = fileOperationService.paste(selectedDir.getValue().getPath());
        if (pasteFiles != null) {
            pasteFiles.forEach(file -> {
                ImagePreViewItem ipItem = new ImagePreViewItem(file, this);
                imagePreviewList.add(ipItem);
              //在页面载入缩略图
                this.getPreviewPane().getChildren().add(ipItem);
            });
        }
        
        if (cutedImageList.size()!=0) {
            cutedImageList.forEach(oldImage -> {
                oldImage.getImageFile().delete();
            });
            clearCuted();
        }
        refresh();

        clearCuted();
        clearSelected();
    }

    /**
     * description: 剪切文件
     * @param
     * @return void
     */
    public void menuItemOfCut() {
    	//剪切当前被选中的图片
        clearCuted();

        selectedImagePreviewList.forEach(image -> {
            cutedImageList.getValue().add(image);
        });
        cutedImageList.forEach(cutedImage -> {
            cutedImage.setEffect(new ColorAdjust(0, 0, 0.5, 0));
        });
        fileOperationService.cut(ConvertUtil.simpleArrayListPropertyToList(cutedImageList));
    }

    /**
     * description: 删除文件
     * @param
     * @return void
     */
    public void menuItemOfDelete() {
        clearCuted();
        if (selectedImagePreviewList != null && selectedImagePreviewList.size() != 0) {
            if (fileOperationService.delete(ConvertUtil.simpleArrayListPropertyToList(selectedImagePreviewList))) {
            	//删除成功就移除
                this.previewPane.getChildren().removeAll(selectedImagePreviewList);
                selectedImagePreviewList.clear();
            }
        }

        clearSelected();
    }

  //幻灯片播方按钮
    public void pptAction() {
       
            slideService.openPPTView(imagePreviewList);
       
    }

  //私有方法 ---------------------------------------------------------------------------------
    /**
     * description: 刷新页面
     * @param
     * @return void
     */
    private void refresh() {
    	//上方导航栏初变化
        pathLabel.setText(selectedDir.getValue().getPath());
      //样式选择 choiceBox 的边话
        sizeChoiceBox.getSelectionModel().select(1);
      //筛选对应的图片文件
        File[] images = selectedDir.getValue().listFiles(
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
        if (images != null) {
            stateLabel.setText(String.format("共 %d 张图片 |", images.length));
            previewPane.getChildren().clear();

          //加载图片
            loadPicture(images);
        }
    }

    /**
     * description: 清除选中的图片
     * @param
     * @return void
     */
    private void clearSelected() {
        PictureOverviewController.this.getImagePreviewList().forEach(image -> {
            image.setIsSelected(false);
        });
        PictureOverviewController.this.getSelectedImagePreviewList().clear();
    }

    /**
     * description: 清除剪切文件的样式
     * @param
     * @return void
     */
    private void clearCuted() {
        cutedImageList.forEach(cutedImage -> {
            cutedImage.setEffect(null);
        });
        cutedImageList.clear();
    }

//getter & setter ------------------------------------------------------------------------
    public void setSelectedDir(File selectedDir) {
        this.selectedDir.set(selectedDir);
    }

    public FlowPane getPreviewPane() {
        return previewPane;
    }
    
    public double getRectangleLayoutX() {
    	return rectangleLayoutX.doubleValue();
    }
    public void setRectangleLayoutX(double value) {
    	rectangleLayoutX.set(value);
    }
    public double getRectangleLayoutY() {
    	return rectangleLayoutY.doubleValue();
    }
    public void setRectangleLayoutY(double value) {
    	rectangleLayoutY.set(value);
    }
    public double getRectangleWidth() {
    	return rectangleWidth.doubleValue();
    }
    public void setRectangleWidth(double value) {
    	rectangleWidth.set(value);
    }
    public double getRectangleHeight() {
    	return rectangleHeight.doubleValue();
    }
    public void setRectangleHeight(double value) {
    	rectangleHeight.set(value);
    }
    
    public ObservableList<ImagePreViewItem> getImagePreviewList() {
        return imagePreviewList.get();
    }

    public SimpleListProperty<ImagePreViewItem> imagePreviewListProperty() {
        return imagePreviewList;
    }

    public void setImagePreviewList(ObservableList<ImagePreViewItem> imagePreviewList) {
        this.imagePreviewList.set(imagePreviewList);
    }

    public ObservableList<ImagePreViewItem> getSelectedImagePreviewList() {
        return selectedImagePreviewList.get();
    }

    public SimpleListProperty<ImagePreViewItem> selectedImagePreviewListProperty() {
        return selectedImagePreviewList;
    }

    public void setSelectedImagePreviewList(ObservableList<ImagePreViewItem> selectedImagePreviewList) {
        this.selectedImagePreviewList.set(selectedImagePreviewList);
    }
}
