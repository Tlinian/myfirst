package viewer.service.impl;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import viewer.model.ImagePreViewItem;
import viewer.service.FileOperationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by PanD
 */

public class FileOperationServiceImpl implements FileOperationService {

    @Override
    public void rename(ObservableList<ImagePreViewItem> selectedImageList) {
        if (selectedImageList.size() == 1) {
            ImagePreViewItem imagefile = selectedImageList.get(0);
            String extName = imagefile.getImageFile().getName().substring(imagefile.getImageFile().getName().lastIndexOf('.'));

            TextInputDialog dialog = new TextInputDialog(null);
            dialog.setHeaderText(null);
            dialog.setTitle("重命名");
            dialog.setContentText("新文件名(不需要扩展名):");

            File[] images = new File(imagefile.getImageFile().getParent()).listFiles(
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
            List<File> fileList = new ArrayList<>();
            Collections.addAll(fileList, images);

            dialog.showAndWait().ifPresent(response -> {

                if (findSameName(response + extName, fileList)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示信息");
                    alert.setHeaderText(null);
                    alert.setContentText("图片文件重命名失败，请检查是否有同名文件！");
                    alert.showAndWait();
                } else {
                    imagefile.rename(response + extName);
                }

            });
        } else {
            Dialog<Pair<String, Pair<Integer, Integer>>> dialog = new Dialog<>();
            dialog.setTitle("批量重命名");
            dialog.setHeaderText(null);

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField textFieldFilename = new TextField("NewImage");
            Spinner<Integer> spinnerStartNumber = new Spinner<>(1, 100, 1);
            Spinner<Integer> spinnerNumberWidth = new Spinner<>(1, 8, 3);

            Platform.runLater(() -> textFieldFilename.requestFocus());

            grid.add(new Label("统一文件名:"), 0, 0);
            grid.add(textFieldFilename, 1, 0);
            grid.add(new Label("起始序号:"), 0, 1);
            grid.add(spinnerStartNumber, 1, 1);
            grid.add(new Label("序号位数:"), 0, 2);
            grid.add(spinnerNumberWidth, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            textFieldFilename.textProperty().addListener((observable, oldValue, newValue) -> {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.trim().isEmpty());
            });

            dialog.setResultConverter(new Callback<ButtonType, Pair<String, Pair<Integer, Integer>>>() {
                @Override
                public Pair<String, Pair<Integer, Integer>> call(ButtonType buttonType) {
                    if (buttonType == ButtonType.OK) {
                        Integer start = spinnerStartNumber.getValue();
                        Integer width = spinnerNumberWidth.getValue();
                        String newName = textFieldFilename.getText();
                        return new Pair<String, Pair<Integer, Integer>>(newName,
                                new Pair<Integer, Integer>(start, width));
                    }
                    return null;
                }
            });

            dialog.showAndWait().ifPresent(renameValue -> {
                String newName = renameValue.getKey();
                Integer start = renameValue.getValue().getKey();
                Integer width = renameValue.getValue().getValue();
                int successful = 0;
                int failure = 0;

                for (ImagePreViewItem ipItem : selectedImageList) {
                    String extName = ipItem.getImageFile().getName().substring(ipItem.getImageFile().getName().lastIndexOf('.'));
                    String newFilename = String.format(newName + "%0" + width + "d" + extName, start);
                    start++;
                    if (ipItem.rename(newFilename)) {
                        successful++;
                    } else {
                        failure++;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示信息");
                alert.setHeaderText(null);
                alert.setContentText(String.format("批量图片文件完成, %d个成功, %d个失败！", successful, failure));

                alert.showAndWait();
            });
        }
    }

    @Override
    public void copy(List<File> fileList) {
        if(fileList.size() <=0) {
            return;
        }
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboard.clear();
        clipboardContent.putFiles(fileList);
        clipboard.setContent(clipboardContent);
    }

    @Override
    public List<File> paste(String path) {
        //获取剪贴板的内容
        List<File> clipboardFile = getClipboardContent();
        if (clipboardFile.size() <= 0){
            return null;
        }

        //列出当前目录的所有文件
        File[] images = (new File(path)).listFiles(
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
        List<File> fileList = new ArrayList<>();
        Collections.addAll(fileList, images);

        for (File oldFile : clipboardFile) {
            String name = pasteRename(oldFile.getName(), fileList);
            File newFile = new File(path + File.separator + name);
            System.out.println(newFile.getPath());
            try {
                newFile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (newFile.exists()) {
                try {
                    copyFile(oldFile, newFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        
        return clipboardFile;
    }

    @Override
    public void cut(List<File> fileList) {
        copy(fileList);
    }

    @Override
    public boolean delete(List<File> fileList) {
        if(this.showDeleteAlert("是否删除选中的图片？", "")) {
            for(File file: fileList) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    /**
     * description: 将文件列表载入剪切板
     * @param fileList
     * @return void
     */
    private void putFileInClipBoard(List<File> fileList) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboard.clear();
        clipboardContent.putFiles(fileList);
        clipboard.setContent(clipboardContent);
    }

    /**
     * description: 获取剪切板的内容
     * @param
     * @return java.util.List<java.io.File>
     */
    private List<File> getClipboardContent() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return (List<File>) (clipboard.getContent(DataFormat.FILES));
    }

    /**
     * description: 删除时显示的对话框
     * @param header
     * @param message
     * @return boolean
     */
    private boolean showDeleteAlert(String header,String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, message,
                new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));

        alert.setTitle("删除");
        alert.setHeaderText(header);
        Optional<ButtonType> buttonType = alert.showAndWait();

        //根据按键选择结果返回
        if(buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        } else {
            return false;
        }
    }

    private String pasteRename(String name, List<File> fileList) {
        while (findSameName(name, fileList)) {
            int end = name.lastIndexOf(".");
            name = name.substring(0, end) + "_副本" + name.substring(end);
        }
        return name;
    }

    private boolean findSameName(String name, List<File> fileList) {
        for (File file:fileList) {
            if (name.equals(file.getName())) {
                return true;
            }
        }
        return false;
    }

    private void copyFile(File fromFile, File toFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(fromFile);
        FileOutputStream outputStream = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int byteRead;
        while ((byteRead = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, byteRead);
        }
        inputStream.close();
        outputStream.close();
    }
}
