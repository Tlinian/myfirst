package viewer.service;

import viewer.service.impl.FileOperationServiceImpl;
import viewer.service.impl.ImageViewSerivceImpl;
import viewer.service.impl.RotateServiceImpl;
import viewer.service.impl.slideServiceImpl;

/**
 * Created by PanD
 * Service工厂，用于提供Serivce服务
 */


public class ServiceFactory {

    private static FileOperationService fileOperationService = new FileOperationServiceImpl();

    private static ImageViewSerivce imageViewSerivce = new ImageViewSerivceImpl();

  private static SlideService slideService = new slideServiceImpl();  
  private static RotateService rotateService = new RotateServiceImpl();
    public static FileOperationService getFileOperationService() {
        return fileOperationService;
    }

    public static ImageViewSerivce getImageViewSerivce() {
        return imageViewSerivce;
    }

    public static SlideService getSlideService() {
        return slideService;
    }
    public static RotateService getRotateService() {
        return rotateService;
    }
    
}
