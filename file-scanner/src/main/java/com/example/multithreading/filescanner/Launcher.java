package com.example.multithreading.filescanner;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.concurrent.*;

public class Launcher {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean calculating = true;
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Path path = Path.of(inputPath());
        int cmd = inputCommand();
        exec(path, cmd);
    }

    private static void showProgress() {
        calculating = true;
        executorService.submit(() -> {
            while (calculating) {
                System.out.print(".");
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void stopProgress() {
        calculating = false;
        executorService.shutdown();
    }

    private static String inputPath() {
        System.out.println("input path:");
        return in.nextLine();
    }

    private static int inputCommand() {
        StringBuilder cmdBuilder = new StringBuilder("");
        cmdBuilder.append("\n");
        cmdBuilder.append("1. Count file");
        cmdBuilder.append("\n");
        cmdBuilder.append("2. Count folder");
        cmdBuilder.append("\n");
        cmdBuilder.append("3. Count size");

        System.out.println(cmdBuilder);
        System.out.print("SelectCommand: ");
        return Integer.parseInt(in.nextLine());
    }

    private static void exec(Path path, int cmd) throws IOException, ExecutionException, InterruptedException {
        switch (cmd) {
            case 1 -> System.out.println("File count: " + FileScanner.countFile(path));
            case 2 -> System.out.println("Folder count: " + FileScanner.countFolder(path));
            case 3 -> {
                showProgress();
                System.out.println("Sum size: " + FileScanner.sumSizeFuture(path).get());
                stopProgress();
            }
            default -> System.out.println("invalid command");
        }
        System.out.println("-----------------------");
    }
}
