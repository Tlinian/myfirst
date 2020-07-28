package viewer.action;

import javafx.beans.property.SimpleListProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import viewer.model.ImagePreViewItem;
import viewer.service.impl.slideServiceImpl;

public class SlideAction
{
	public static int index = 0;
	public static int yindex = 0;
	public static int first = 0;
	public static ImageView[] imageviews;
	public static boolean convert = false;
	public static boolean convert2 = false;
	public static ImageView image;
	public static int lastClicks = 0;
	public static my m;
	public static your y;
	public static int imageTime = 4000;
	private Button button1 = new Button("start");
	private Button button2 = new Button("end");
	
	private AnchorPane anchorPane;
	private SimpleListProperty<ImagePreViewItem> imagePreViewItems;
	private Stage parentStage;

	public void pptView(ImageView[] imageviews)
	{

		AnchorPane.setLeftAnchor(button1, 100.0);
		AnchorPane.setLeftAnchor(button2, 300.0);
		anchorPane.getChildren().addAll(button1, button2);
		anchorPane.setId("anchorpane");
	}

	// constructor -------------------------------------------------------

	// 初始化传入要展示的图片列表 和 对应的 anchorPane
	public SlideAction(Stage parentStage, AnchorPane anchorPane, SimpleListProperty<ImagePreViewItem> imagePreViewItems)
	{
		this.anchorPane = anchorPane;
		this.imagePreViewItems = imagePreViewItems;
		this.parentStage = parentStage;
		button1.setId("start_btn");
		button1.setPrefSize(70, 40);
		button2.setPrefSize(70, 40);
		button2.setId("end_btn");
		if (imagePreViewItems.size() == 0)
		{
			return;
		}

		imageviews = new ImageView[imagePreViewItems.size()];

		for (int i = 0; i < imagePreViewItems.size(); i++)
		{
			ImagePreViewItem item = imagePreViewItems.get(i);
			imageviews[i] = new ImageView(new Image(item.getImageFile().toURI().toString()));
			imageviews[i].setFitWidth(item.getImage().getWidth());
			imageviews[i].setFitHeight(item.getImage().getHeight());
		}

		AnchorPane.setTopAnchor(button1, 20.0);
		AnchorPane.setTopAnchor(button2, 20.0);

		parentStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{

			@Override
			public void handle(WindowEvent event)
			{

				// TODO Auto-generated method stub
				if (lastClicks == 1 || lastClicks == 2)
				{
					if (lastClicks == 1)
					{
						m.cancel();
						y.cancel();
					}
					anchorPane.getChildren().removeAll(button1, button2);
					setConvert(false);
					setConvert2(false);
					setFirst(0);
					setImageTime(4000);
					setIndex(0);
					setYIndex(0);
					lastClicks = 0;
				}
			}
		});

		anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if (event.getCode() == KeyCode.ESCAPE)
				{
					if (lastClicks == 1 || lastClicks == 2)
					{
						if (lastClicks == 1)
						{
							m.cancel();
							y.cancel();
						}
						anchorPane.getChildren().removeAll(button1, button2);
						setConvert(false);
						setConvert2(false);
						setFirst(0);
						setImageTime(4000);
						setIndex(0);
						setYIndex(0);
						lastClicks = 0;
					}
					parentStage.close();
				}
			}
		});

		pptView(imageviews);

		// 监听事件

		button1.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (lastClicks == 2 || lastClicks == 0)
				{
					m = new my(imageviews);
					m.setDelay(Duration.millis(0));
					m.setPeriod(Duration.millis(getImageTime()));
					m.start();
				}

				lastClicks = 1;
			}
		});

		button2.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{

				if (lastClicks == 1)
				{
					if (getYIndex() != 24)
					{
						SlideAction.setConvert(true);

					}
					m.cancel();
					getYour().cancel();
				}
				lastClicks = 2;
			}
		});

	}
	// getter & setter ----------------------------------------------------
	public static boolean getConvert()
	{
		return convert;
	}

	public static void setConvert(boolean b)
	{
		convert = b;
	}

	public static boolean getConvert2()
	{
		return convert2;
	}

	public static void setConvert2(boolean b)
	{
		convert2 = b;
	}

	public static your getYour()
	{
		return y;
	}
	public static void setYour(your r)
	{
		y = r;
	}

	public static my getMy()
	{
		return m;
	}

	public static void setMy(my r)
	{
		m = r;
	}

	public static int getYIndex()
	{
		return yindex;
	}

	public static void setYIndex(int x)
	{
		yindex = x;
	}

	public static int getImageTime()
	{
		return imageTime;
	}

	public static void setImageTime(int x)
	{
		imageTime = x;
	}

	public static int getIndex()
	{
		return index;
	}

	public static void setIndex(int x)
	{
		index = x;
	}

	public static ImageView[] getImageView()
	{
		return imageviews;
	}

	public static ImageView getImage()
	{
		return image;
	}

	public static void setImage(ImageView i)
	{
		image = i;
	}

	public static int getFirst()
	{
		return first;
	}

	public static void setFirst(int aaaa)
	{
		first = aaaa;
	}

	public static void setImageView(ImageView[] aaaa)
	{
		imageviews = aaaa;
	}

	class my extends ScheduledService<Integer>
	{
		int i;

		public my(ImageView[] imageview)
		{
			SlideAction.setImageView(imageview);
			if (SlideAction.convert2)
			{
				SlideAction.setConvert2(false);
				i = SlideAction.getIndex();

			} else if (SlideAction.getIndex() != 0)
			{
				i = SlideAction.getIndex() - 1;
			} else
			{
				i = imageview.length - 1;
			}
		}

		@Override
		protected Task<Integer> createTask()
		{
			Task task = new Task<Integer>()
			{
				@Override
				protected Integer call() throws Exception
				{
					if (i == SlideAction.getImageView().length - 1)
					{
						i = -1;
					}
					SlideAction.setFirst(SlideAction.getFirst() + 1);
					return i = i + 1;
				}

				@Override
				protected void updateValue(Integer value)
				{
					super.updateValue(value);
					SlideAction.setIndex(value);

					if (SlideAction.getFirst() > 1)
					{

						anchorPane.getChildren().remove(2);
						SlideAction.getYour().cancel();
					}
					SlideAction.setYour(new your(SlideAction.getImageView()[value]));
					SlideAction.getYour().setDelay(Duration.millis(0));
					SlideAction.getYour().setPeriod(Duration.millis(SlideAction.getImageTime() / 24));// ͼƬ�л���ʱ���1/100
					SlideAction.getYour().start();
					anchorPane.getChildren().add(SlideAction.getImageView()[value]);
				}

			};
			return task;
		}
	}
	class your extends ScheduledService<Integer>
	{
		int i;
		ImageView imageView;

		public your(ImageView iw)
		{
			SlideAction.setImage(iw);
			imageView = iw;
			i = SlideAction.getYIndex();
		}

		@Override
		protected Task<Integer> createTask()
		{
			Task task = new Task<Integer>()
			{
				@Override
				protected Integer call() throws Exception
				{
					if (i == 24)
					{
						i = 0;
					}
					return i = i + 1;
				}

				@Override
				protected void updateValue(Integer value)
				{
					super.updateValue(value);
					SlideAction.setYIndex(value);

					double opacity;
                
					opacity = (double) value /24;
                   

					imageView.setPreserveRatio(true);
					imageView.setFitHeight(375.5);
					imageView.setFitWidth(450.0);
					imageView.setOpacity(opacity);
				
					AnchorPane.setTopAnchor(imageView, slideServiceImpl.slideViewStage.getHeight()/2-imageView.getFitHeight()/2);
					AnchorPane.setLeftAnchor(imageView, slideServiceImpl.slideViewStage.getWidth()/2-imageView.getFitWidth()/2);
					slideServiceImpl.slideViewStage.widthProperty().addListener((a) -> {
						imageView.setScaleX(1.0);
						imageView.setScaleY(1.0);
					});
					slideServiceImpl.slideViewStage.heightProperty().addListener((a) -> {
						imageView.setScaleX(1.0);
						imageView.setScaleY(1.0);
					});

					if (SlideAction.getConvert() && value == 24)
					{
						SlideAction.getMy().cancel();
						SlideAction.setConvert(false);
						SlideAction.setConvert2(true);
						SlideAction.setMy(new my(SlideAction.getImageView()));
						SlideAction.getMy().setDelay(Duration.millis(0));
						SlideAction.getMy().setPeriod(Duration.millis(SlideAction.getImageTime()));// �л�ʱ��İٷ�֮һ
						SlideAction.getMy().start();
					}
				}

			};
			return task;
		}
	}
}
