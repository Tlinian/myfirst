package viewer.service;

import javafx.beans.property.SimpleListProperty;
import viewer.model.ImagePreViewItem;

/**
 * Created by PanD
 */

public interface SlideService {

    void openPPTView(SimpleListProperty<ImagePreViewItem> imageList);
}
