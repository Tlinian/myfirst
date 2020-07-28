package viewer.service;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import viewer.model.ImagePreViewItem;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 */

public interface FileOperationService {

    void rename(ObservableList<ImagePreViewItem> selectedImageList);

    void copy(List<File> fileList);

    List<File> paste(String path);

    void cut(List<File> fileList);

    boolean delete(List<File> fileList);
}
