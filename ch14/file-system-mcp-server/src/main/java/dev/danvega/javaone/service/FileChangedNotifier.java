package dev.danvega.javaone.service;

import java.io.File;

/**
 * Interface for receiving file change notifications.
 */
public interface FileChangedNotifier {

    /**
     * Called when a new file is created.
     * 
     * @param file the created file
     */
    void notifyFileCreated(File file);

    /**
     * Called when a file is modified.
     * 
     * @param file the modified file
     */
    void notifyFileChanged(File file);

    /**
     * Called when a file is deleted.
     * 
     * @param file the deleted file
     */
    void notifyFileDeleted(File file);
}
