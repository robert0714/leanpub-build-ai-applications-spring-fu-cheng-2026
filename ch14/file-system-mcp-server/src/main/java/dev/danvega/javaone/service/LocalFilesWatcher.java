package dev.danvega.javaone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Watches a directory for file changes and notifies a listener.
 */
public class LocalFilesWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFilesWatcher.class);

    private final FileChangedNotifier notifier;
    private WatchService watchService;
    private ExecutorService executor;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Path currentPath;

    public LocalFilesWatcher(FileChangedNotifier notifier) {
        this.notifier = notifier;
    }

    /**
     * Start watching the specified directory for changes.
     * 
     * @param path the directory to watch
     * @throws IOException if the watch service cannot be created
     */
    public void watch(Path path) throws IOException {
        // Stop any existing watcher
        stop();

        this.currentPath = path;
        this.watchService = FileSystems.getDefault().newWatchService();
        
        // Register the directory with the watch service
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        running.set(true);
        executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "file-watcher");
            t.setDaemon(true);
            return t;
        });

        executor.submit(this::processEvents);
        LOGGER.info("Started watching directory: {}", path);
    }

    private void processEvents() {
        while (running.get()) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (ClosedWatchServiceException e) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path fileName = pathEvent.context();
                Path fullPath = currentPath.resolve(fileName);
                File file = fullPath.toFile();

                try {
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        LOGGER.debug("File created: {}", fullPath);
                        notifier.notifyFileCreated(file);
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        LOGGER.debug("File modified: {}", fullPath);
                        notifier.notifyFileChanged(file);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        LOGGER.debug("File deleted: {}", fullPath);
                        notifier.notifyFileDeleted(file);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error handling file event for: {}", fullPath, e);
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                LOGGER.warn("Watch key is no longer valid, stopping watcher");
                break;
            }
        }
    }

    /**
     * Stop watching for file changes.
     */
    public void stop() {
        running.set(false);
        
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                LOGGER.error("Error closing watch service", e);
            }
            watchService = null;
        }

        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }
}
