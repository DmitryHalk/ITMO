package itmo.lab6.console;

import java.util.Scanner;

public class MainConsole implements Console {
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);


    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    @Override
    public String readln() {
        return (fileScanner != null?fileScanner:defScanner).nextLine();
    }

    @Override
    public boolean isCanReadln() {
        return (fileScanner != null) ? fileScanner.hasNextLine() : defScanner.hasNextLine();
    }

    @Override
    public void printTable() {
    }

    @Override
    public void selectFileScanner(Scanner sc) {
        this.fileScanner = sc;
    }

    @Override
    public void selectConsoleScanner() {
        this.fileScanner = null;
    }
}
