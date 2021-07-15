package com.example.multithreading.filescanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FileScanner {
    public static int countFile(Path path) throws IOException {
        List<Path> allChildren = Files.list(path).collect(Collectors.toList());
        return (int) allChildren.stream().filter(Files::isRegularFile).count();
    }

    public static int countFolder(Path path) throws IOException {
        List<Path> allChildren = Files.list(path).collect(Collectors.toList());
        return (int) allChildren.stream().filter(Files::isDirectory).count();
    }

    public static Future<Long> sumSizeFuture(Path path) {
        return CompletableFuture.completedFuture(1L);
    }
}
