package itmo;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.lang.IllegalStateException;

public class MainConsole implements Console{
    private Object obj;
    private static final String P = "$ ";
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
        return false;
    }


    @Override
    public void printTable() {

    }

    @Override
    public void prompt() {
        print(P);
    }

    @Override
    public String getPrompt() {
        return P;
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
