package com.example.multithreading.filescanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.*;

public class Launcher {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static boolean calculating = true;
    private static final Scanner in = new Scanner(System.in);

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
                    //e.printStackTrace();
                }
            }
        });
    }

    private static void stopProgress() {
        calculating = false;
        executorService.shutdownNow();
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
                Future<Long> future = FileScanner.sumSizeFuture(path);
                executorService.submit(() -> {
                    System.out.println("Enter \"c\" to cancel");
                    String c = in.nextLine();
                    System.out.println("c=" + c);
                    if(c.contains("c")) {
                        future.cancel(true);
                    }
                });
                try {
                    System.out.println("Sum size: " + future.get());
                } catch (CancellationException ex) {
                    System.out.println("Task was cancelled");
                } finally {
                    stopProgress();
                }
            }
            default -> System.out.println("invalid command");
        }
        System.out.println("-----------------------");
    }
}
