package com.example.multithreading.filescanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class FileScanner {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static int countFile(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isRegularFile).count();
    }

    public static int countFolder(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isDirectory).count();
    }

    public static Future<Long> sumSizeFuture(Path path) throws IOException {
        int fileCount = (int) Files.list(path).count();
        if (fileCount == 0) {
            return CompletableFuture.completedFuture(0L);
        }
        return executor.submit(() -> (long) Files.list(path).map(p -> {
            long size = 0;
            try {
                if (Files.isRegularFile(p)) {
                    size = Files.size(p);
                } else {
                    size = sumSizeFuture(p).get();
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
                size = -1;
            }
            return size;
        }).reduce(0L, Long::sum));
    }
}
