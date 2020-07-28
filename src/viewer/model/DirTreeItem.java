package viewer.model;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Created by PanD
 */

public class DirTreeItem extends TreeItem<File> {

    private boolean isRoot;

    private boolean isLoaded;

    public boolean isRoot() {
        return isRoot;
    }

    public DirTreeItem(File file, boolean isRoot) {
        super(file);
        this.isRoot = isRoot;
    }

    /**
     * description: 加载当前节点的子节点
     * @param
     * @return void
     */
    public void load() {
        if (!isLoaded) {
            isLoaded = true;

            File[] children = null;
            if (isRoot) {
                children = File.listRoots();
            } else {
                File dir = this.getValue();
                children = dir.listFiles();
            }

            if (children == null ) {
                return;
            }

            for (File child : children) {
                if (child.isDirectory() && (isRoot || !child.isHidden())) {
                    DirTreeItem item = new DirTreeItem(child,false);

                    item.addEventHandler(DirTreeItem.branchExpandedEvent(),
                            new EventHandler<TreeItem.TreeModificationEvent<File>>() {
                        @Override
                        public void handle(TreeModificationEvent<File> event) {
                            for (TreeItem<File> child : event.getSource().getChildren()) {
                                ((DirTreeItem) child).load();
                            }
                        }
                    });

                    this.getChildren().add(item);
                }
            }
        }
    }
}
