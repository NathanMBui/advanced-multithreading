package com.example.multithreading.filescanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class FileScanner {

    private static final ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public static int countFile(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isRegularFile).count();
    }

    public static int countFolder(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isDirectory).count();
    }

    public static Future<Long> sumSizeFuture(Path path) {
        return fjp.submit(new SumSizeFolderTask(path));
    }

    private static class SumSizeFolderTask extends RecursiveTask<Long> {

        private final Path path;

        public SumSizeFolderTask(Path folder) {
            this.path = folder;
        }

        @Override
        protected Long compute() {
            long sum;
            try {
                int fileCount = (int) Files.list(path).count();
                if (fileCount == 0) {
                    return 0L;
                }
                sum = Files.list(path).map(p -> {
                    long size;
                    try {
                        if (Files.isRegularFile(p)) {
                            size = Files.size(p);
                        } else {
                            size = fjp.invoke(new SumSizeFolderTask(p));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        size = 0;
                    }
                    return size;
                }).reduce(0L, Long::sum);

            } catch (IOException e) {
                e.printStackTrace();
                return 0L;
            }
            return sum;
        }
    }
}
