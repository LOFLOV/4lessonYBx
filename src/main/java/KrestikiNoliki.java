import com.sun.corba.se.impl.io.TypeMismatchException;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class KrestikiNoliki {


    /*Блок настроек игры */

    private static char[][] map;    //матрица игры
    private static int SIZE = 3;        //  размер поля

    private static final char DOT_EMPTY = '•';
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';

    private static Scanner scanner = new Scanner(System.in);
    public static Random rand = new Random();


    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";


    public static void main(String[] args) {

        chooseSizeMap();

        initMap();
        printMap();
        while (true) {
            humanTurn();    //  ход человека
            printMap();
            if (checkWin(DOT_X)) {
                System.out.println(ANSI_BLACK + ANSI_GREEN_BACKGROUND + "Победил ЧЕЛОВЕК" + ANSI_RESET);
                break;
            }
            if (isMapfull()) {
                System.out.println("Ничья");
                break;
            }
            computerTurn();     // ход компьютера
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println(ANSI_BLACK + ANSI_RED_BACKGROUND + "Победил КОМП" + ANSI_RESET);
                break;
            }
            if (isMapfull()) {
                System.out.println("Ничья");
            }
        }
        System.out.println("Игра закончена");
    }






    /* метод подготовки игрового поля */


    private static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printMap() {
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(ANSI_BLACK_BACKGROUND + " " + i + " " + ANSI_RESET);
        }
        System.out.print(" — " + ANSI_BLACK_BACKGROUND + " X " + ANSI_RESET);                        //указание на ось Х
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(ANSI_BLACK_BACKGROUND + " " + (i + 1) + " " + ANSI_RESET);
            for (int j = 0; j < SIZE; j++) {
                System.out.print(ANSI_BLACK + ANSI_WHITE_BACKGROUND + " " + map[i][j] + " " + ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println(" | ");
        System.out.println(ANSI_BLACK_BACKGROUND + " Y " + ANSI_RESET);                     //указание на ось Y
    }

    private static void humanTurn() {
        int x = -1;
        int y = -1;
        do {
            System.out.println("Введите координаты ячейки (через пробел Х и Y)");
            try {
                y = scanner.nextInt() - 1;
                x = scanner.nextInt() - 1;
            } catch (InputMismatchException exep) {
                System.out.println("Ввод символов");
                scanner.nextLine();
            }
        } while (!isCellValid(x, y));
        System.out.println();
        System.out.println(ANSI_BLACK + ANSI_GREEN_BACKGROUND + "Вы походили в точку       " + (y + 1) + " " + (x + 1) + " " + ANSI_RESET);
        map[x][y] = DOT_X;
    }


    private static void computerTurn() {
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!isCellValid(x, y));
        System.out.println();
        System.out.println(ANSI_BLACK + ANSI_RED_BACKGROUND + "КОМПЬЮТЕР походил в точку " + (y + 1) + " " + (x + 1) + " " + ANSI_RESET);
        map[x][y] = DOT_O;
    }


//     * Метод валидации запрашиваемой ячейки на корректность

    private static boolean isCellValid(int x, int y) {
        boolean result = true;
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            System.out.println("некорректные координаты");
            return false;
        } else if (map[x][y] != DOT_EMPTY) {
            result = false;
        }
        return result;
    }

    public static boolean isMapfull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }


    private static boolean checkWin(char symb) {
        if (firstDiagonal(symb)) return true;
        if (secondDiagonal(symb)) return true;
        if (rowChecker(symb)) return true;
        if (columnChecker(symb)) return true;
        return false;
    }

    private static boolean firstDiagonal(char symb) {
        int symbolCounter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (i == j && map[i][j] == symb) {
                    symbolCounter += 1;
                    if (symbolCounter == checkMapSize()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean secondDiagonal(char symb) {
        int symbolCounter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                int makeDiagonal = map.length - 1 - i;
                if (map[i][makeDiagonal] == symb) {
                    symbolCounter += 1;
                    if (symbolCounter == checkMapSize()) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }


    private static boolean rowChecker(char symb) {
        int symbolCounter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == symb) {
                    symbolCounter += 1;
                }
            }
            if (symbolCounter == checkMapSize()) {            // проверка рядка
                return true;
            } else {
                symbolCounter = 0;
            }
        }
        return false;
    }

    private static boolean columnChecker(char symb) {
        int symbolCounter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[j][i] == symb) {
                    symbolCounter += 1;
                }
            }
            if (symbolCounter == checkMapSize()) {
                return true;
            } else {
                symbolCounter = 0;
            }
        }
        return false;
    }

    private static int checkMapSize() {
        int moreThan3x3 = 0;
        if (SIZE > 4) {                      // если поле 5х5 и больше
            moreThan3x3 = SIZE - 1;          // количества фишек на 1 меньше
        } else {
            moreThan3x3 = SIZE;
        }
        return moreThan3x3;
    }

    private static void chooseSizeMap() {
        System.out.println(ANSI_YELLOW + "КРЕСТИКИ НОЛИКИ" + ANSI_RESET);
        int inputValue = 0;

        do {
            System.out.println("Выберие поле 3х3 - ведите (1) для поля 5х5 - ведите (2)");
            try {
                inputValue = scanner.nextInt();
            } catch (InputMismatchException excep) {
                System.out.println("Ввод не коректен");
                scanner.nextLine();
            }
        } while (inputValue < 1 || inputValue > 2);
        if (inputValue == 1) {
            SIZE = 3;
        } else {
            SIZE = 5;
        }
    }

}
