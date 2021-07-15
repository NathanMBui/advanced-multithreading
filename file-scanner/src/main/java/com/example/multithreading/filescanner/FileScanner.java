package com.example.multithreading.filescanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class FileScanner {

    private static final ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public static int countFile(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isRegularFile).count();
    }

    public static int countFolder(Path path) throws IOException {
        return (int) Files.list(path).filter(Files::isDirectory).count();
    }

    public static Future<Long> sumSizeFuture(Path path) {
        if (path == null)
            throw new NullPointerException("path can not be null");
        return fjp.submit(new SumSizeTask(path));
    }

    private static class SumSizeTask extends RecursiveTask<Long> {

        private final Path path;

        public SumSizeTask(Path folder) {
            this.path = folder;
        }

        @Override
        protected Long compute() {
            try {
                if (!Files.isReadable(path)) {
                    return 0L;
                }
                if (Files.isDirectory(path)) {
                    Collection<SumSizeTask> filesTask = Files.list(path).map(SumSizeTask::new).collect(Collectors.toList());
                    return invokeAll(filesTask).stream().mapToLong(ForkJoinTask::join).sum();
                } else {
                    return Files.size(path);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return 0L;
            }
        }
    }
}
