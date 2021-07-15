package com.example.multithreading.filescanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class FileScannerTest {

    @Test
    public void testEmpty(@TempDir Path path) throws IOException {
        int fileCount = FileScanner.countFile(path);
        assertEquals(0, fileCount);
    }

    @Test
    public void testCountOneFile(@TempDir Path path) throws IOException {
        Files.createFile(path.resolve("file1"));
        int fileCount = FileScanner.countFile(path);
        assertEquals(1, fileCount);
    }

    @Test
    public void testCountTwoFile(@TempDir Path path) throws IOException {
        Files.createFile(path.resolve("file1"));
        Files.createFile(path.resolve("file2"));
        int fileCount = FileScanner.countFile(path);
        assertEquals(2, fileCount);
    }

    @Test
    public void testCountFolder(@TempDir Path path) throws IOException {
        Files.createDirectory(path.resolve("folder1"));
        Files.createDirectory(path.resolve("folder2"));
        int folderCount = FileScanner.countFolder(path);
        assertEquals(2, folderCount);
    }


    @Test
    public void testCountFileAndFolder(@TempDir Path path) throws IOException {
        Files.createFile(path.resolve("file1"));
        Files.createDirectory(path.resolve("folder1"));
        int fileCount = FileScanner.countFile(path);
        int folderCount = FileScanner.countFolder(path);
        assertEquals(1, fileCount);
        assertEquals(1, folderCount);
    }


    @Test
    public void testSumFileSizeEmptyFolder(@TempDir Path path) throws ExecutionException, InterruptedException {
        Future<Long> sumSizeFuture = FileScanner.sumSizeFuture(path);
        assertNotNull(sumSizeFuture);
        long size = sumSizeFuture.get();
        assertEquals(0, size);
    }

    @Test
    public void testSumFileSize(@TempDir Path path) throws IOException, ExecutionException, InterruptedException {
        Path p1 = Files.createFile(path.resolve("file1"));
        Files.writeString(p1, "test");
        Future<Long> sumSizeFuture = FileScanner.sumSizeFuture(path);
        assertNotNull(sumSizeFuture);
        long size = sumSizeFuture.get();
        assertTrue(size > 0);
    }

    @Test
    public void testSumFileSizeSubFolder(@TempDir Path path) throws IOException, ExecutionException, InterruptedException {
        Path p1 = Files.createFile(path.resolve("file1"));
        Files.writeString(p1, "test");
        long size1 = Files.size(p1);

        Path subFolder = Files.createDirectory(path.resolve("sub"));
        Path p2 = Files.createFile(subFolder.resolve("file2"));
        Files.writeString(p2, "test2");
        long size2 = Files.size(p2);

        Future<Long> sumSizeFuture = FileScanner.sumSizeFuture(path);
        assertNotNull(sumSizeFuture);
        long size = sumSizeFuture.get();
        assertEquals(size1 + size2, size);
    }
}
