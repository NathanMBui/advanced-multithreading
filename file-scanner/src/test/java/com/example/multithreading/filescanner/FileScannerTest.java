package com.example.multithreading.filescanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FileScannerTest {

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
    public void testSumFileSize(@TempDir Path path) throws IOException, ExecutionException, InterruptedException {
        Files.createFile(path.resolve("file1"));
        Files.createFile(path.resolve("file2"));
        Files.createFile(path.resolve("file3"));
        Future<Long> sumSizeFuture = FileScanner.sumSizeFuture(path);
        assertNotNull(sumSizeFuture);
        long size = sumSizeFuture.get();
        assertTrue(size > 0);
    }
}
