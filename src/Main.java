import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static char[][] map;
    public static final int SIZE = 5;
    public static final int DOTS_TO_WIN = 4;
    public static final int DOTS_TO_BLOCK = 2; // начинает блокировать ход человека если находит 2 соседних DOT_X в любых направлениях
    public static final char DOT_EMPTY = '·';
    public static final char DOT_X = 'X';
    public static final char DOT_O = 'O';

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initMap();
        printMap();
        boolean humanWin;
        boolean aiWin = false;
        do{
            humanTurn();
            printMap();
            humanWin = checkWin(DOT_X);
            if (humanWin) {
                break;
            }
            aiBlockTurn(DOT_X);
            printMap();
            aiWin = checkWin(DOT_O);
            if (aiWin) {
                break;
            }
        } while (!mapIsFull());
        if(humanWin){
            System.out.println("Победил человек");
        }
        if(aiWin){
            System.out.println("Победил компьютер");
        }
        if(!humanWin && !aiWin){
            System.out.println("Ничья");
        }
    }

    public static void aiBlockTurn(char symbol) {
        Random random = new Random();
        int positionBlockX = 0;
        int positionBlockX1 = 0;
        int positionBlockX2 = 0;
        int positionBlockY = 0;
        int positionBlockY1 = 0;
        int positionBlockY2 = 0;
        boolean flag = false;


        // в строке есть нужное количество символов подряд
        for (int i = 0; i < SIZE; i++) {
            int findSymbolInRow = 0;
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == symbol) {
                    findSymbolInRow++;
                } else if (findSymbolInRow > 0) {
                    findSymbolInRow = 0;
                }
                if (findSymbolInRow >= DOTS_TO_BLOCK) {
                    if (j == SIZE - 1) {
                        positionBlockX = i;
                        positionBlockY = j - findSymbolInRow;
                        if(isCellValid(positionBlockX, positionBlockY)){
                            map[positionBlockX][positionBlockY] = DOT_O;
                            flag = true;
                            break;
                        }
                    }
                    if (j == findSymbolInRow - 1) {
                        positionBlockX = i;
                        positionBlockY = findSymbolInRow;
                        if(isCellValid(positionBlockX, positionBlockY)){
                            map[positionBlockX][positionBlockY] = DOT_O;
                            flag = true;
                            break;
                        }
                    }
                    if (j >= findSymbolInRow) {
                        positionBlockX = i;
                        positionBlockY = j - findSymbolInRow;
                        positionBlockY1 = j - findSymbolInRow;
                        positionBlockY2 = j + 1;
                        if(isCellValid(positionBlockX, positionBlockY1) && isCellValid(positionBlockX, positionBlockY2)){
                            int randNum = random.nextInt(2);
                            if(randNum == 0){
                                positionBlockY = positionBlockY1;
                            } else {
                                positionBlockY = positionBlockY2;
                            }
                        } else if (isCellValid(positionBlockX, positionBlockY1) && !isCellValid(positionBlockX, positionBlockY2)) {
                            positionBlockY = positionBlockY1;
                        }  else if (!isCellValid(positionBlockX, positionBlockY1) && isCellValid(positionBlockX, positionBlockY2)) {
                            positionBlockY = positionBlockY2;
                        }
                        if(isCellValid(positionBlockX, positionBlockY)){
                            map[positionBlockX][positionBlockY] = DOT_O;
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag){
                    break;
                }
            }
        }

        // в ряду есть нужное количество символов подряд
        if (!flag) {
            for (int i = 0; i < SIZE; i++) {
                int findSymbolInRow = 0;
                for (int j = 0; j < SIZE; j++) {
                    if (map[j][i] == symbol) {
                        findSymbolInRow++;
                    } else if (findSymbolInRow > 0) {
                        findSymbolInRow = 0;
                    }
                    if (findSymbolInRow >= DOTS_TO_BLOCK) {
                        if (j == SIZE - 1) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockY = i;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j == findSymbolInRow - 1) {
                            positionBlockX = findSymbolInRow;
                            positionBlockY = i;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j >= findSymbolInRow) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockX1 =j - findSymbolInRow;
                            positionBlockX2 = j + 1;
                            positionBlockY = i;
                            if(isCellValid(positionBlockX1, positionBlockY) && isCellValid(positionBlockX2, positionBlockY)){
                                int randNum = random.nextInt(2);
                                if(randNum == 0){
                                    positionBlockX = positionBlockX1;
                                } else {
                                    positionBlockX = positionBlockX2;
                                }
                            } else if (isCellValid(positionBlockX1, positionBlockY) && !isCellValid(positionBlockX2, positionBlockY)) {
                                positionBlockX = positionBlockX1;
                            } else if (!isCellValid(positionBlockX1, positionBlockY) && isCellValid(positionBlockX2, positionBlockY)) {
                                positionBlockX = positionBlockX2;
                            }
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }

        // главная диагональ и все диагонали слева направо
        // X X X X 0
        // 0 X X X X
        // 0 0 X X X
        // 0 0 0 X X
        // 0 0 0 0 X
        if (!flag) {
            for (int i = 0; i < SIZE - 1; i++) {
                int buf = 0;
                int findSymbolInRow = 0;
                for (int j = 0; j < SIZE; j++) {
                    for (int k = i + j; k < SIZE; k++) {
                        if (map[j][k] == symbol) {
                            findSymbolInRow++;
                            buf = k;
                            break;
                        } else if (findSymbolInRow > 0) {
                            findSymbolInRow = 0;
                            break;
                        }
                        if (findSymbolInRow == 0) {
                            break;
                        }
                    }
                    if (findSymbolInRow >= DOTS_TO_BLOCK) {
                        if (j == SIZE - 1 - i) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockY = buf - findSymbolInRow;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j == findSymbolInRow - 1) {
                            positionBlockX = findSymbolInRow;
                            positionBlockY = buf + 1;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }

                        }
                        if (j >= findSymbolInRow) {
                            positionBlockX = 0;
                            positionBlockX1 = j - findSymbolInRow;
                            positionBlockX2 = j + 1;
                            positionBlockY = 0;
                            positionBlockY1 = buf - findSymbolInRow;
                            positionBlockY2 = buf + 1;
                            if(isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)){
                                int randNum = random.nextInt(2);
                                if(randNum == 0){
                                    positionBlockX = positionBlockX1;
                                    positionBlockY = positionBlockY1;
                                } else {
                                    positionBlockX = positionBlockX2;
                                    positionBlockY = positionBlockY2;
                                }
                            } else if (isCellValid(positionBlockX1, positionBlockY1) && !isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX1;
                                positionBlockY = positionBlockY1;
                            }  else if (!isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX2;
                                positionBlockY = positionBlockY2;
                            }
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }

        // все диагонали слева вниз без главной диагонали
        // 0 0 0 0 0
        // X 0 0 0 0
        // X X 0 0 0
        // X X X 0 0
        // 0 X X X 0

        if (!flag) {
            for (int i = 0; i < SIZE - 2; i++) {
                int buf = 0;
                int findSymbolInRow = 0;
                for (int j = i + 1; j < SIZE; j++) {
                    for (int k = j - i - 1; k < SIZE - 1; k++) {
                        if (map[j][k] == symbol) {
                            findSymbolInRow++;
                            buf = k;
                            break;
                        } else if (findSymbolInRow > 0) {
                            findSymbolInRow = 0;
                            break;
                        }
                        if (findSymbolInRow == 0) {
                            break;
                        }
                    }
                    if (findSymbolInRow >= DOTS_TO_BLOCK) {
                        if (j == SIZE - 1) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockY = buf - findSymbolInRow;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j == findSymbolInRow + i) {
                            positionBlockX = j + 1;
                            positionBlockY = buf + 1;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }

                        }
                        if (j >= findSymbolInRow) {
                            positionBlockX = 0;
                            positionBlockX1 = j - findSymbolInRow;
                            positionBlockX2 = j + 1;
                            positionBlockY = 0;
                            positionBlockY1 = buf - findSymbolInRow;
                            positionBlockY2 = buf + 1;
                            if(isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)){
                                int randNum = random.nextInt(2);
                                if(randNum == 0){
                                    positionBlockX = positionBlockX1;
                                    positionBlockY = positionBlockY1;
                                } else {
                                    positionBlockX = positionBlockX2;
                                    positionBlockY = positionBlockY2;
                                }
                            } else if (isCellValid(positionBlockX1, positionBlockY1) && !isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX1;
                                positionBlockY = positionBlockY1;
                            }  else if (!isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX2;
                                positionBlockY = positionBlockY2;
                            }
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }

        // второстепенная диагональ и все диагонали справа налево
        // 0 X X X X
        // X X X X 0
        // X X X 0 0
        // X X 0 0 0
        // X 0 0 0 0

        if (!flag) {
            for (int i = 0; i < SIZE - 1; i++) {
                int buf = 0;
                int findSymbolInRow = 0;
                for (int j = 0; j < SIZE; j++) {
                    for (int k = SIZE - 1 - i - j; k >= 0; k--) {
                        if (map[j][k] == symbol) {
                            findSymbolInRow++;
                            buf = k;
                            break;
                        } else if (findSymbolInRow > 0) {
                            findSymbolInRow = 0;
                            break;
                        }
                        if (findSymbolInRow == 0) {
                            break;
                        }
                    }
                    if (findSymbolInRow >= DOTS_TO_BLOCK) {
                        if (j == SIZE - 1 - i) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockY = buf + findSymbolInRow;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j == findSymbolInRow - 1) {
                            positionBlockX = findSymbolInRow;
                            positionBlockY = buf - 1;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }

                        }
                        if (j >= findSymbolInRow) {
                            positionBlockX = 0;
                            positionBlockX1 = j - findSymbolInRow;
                            positionBlockX2 = j + 1;
                            positionBlockY = 0;
                            positionBlockY1 = buf + findSymbolInRow;
                            positionBlockY2 = buf - 1;
                            if(isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)){
                                int randNum = random.nextInt(2);
                                if(randNum == 0){
                                    positionBlockX = positionBlockX1;
                                    positionBlockY = positionBlockY1;
                                } else {
                                    positionBlockX = positionBlockX2;
                                    positionBlockY = positionBlockY2;
                                }
                            } else if (isCellValid(positionBlockX1, positionBlockY1) && !isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX1;
                                positionBlockY = positionBlockY1;
                            }  else if (!isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX2;
                                positionBlockY = positionBlockY2;
                            }
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }

        // все диагонали справа вниз без второстепенной диагонали
        // 0 0 0 0 0
        // 0 0 0 0 X
        // 0 0 0 X X
        // 0 0 X X X
        // 0 X X X 0
        if (!flag) {
            for (int i = 0; i < SIZE - 2; i++) {
                int buf = 0;
                int findSymbolInRow = 0;
                for (int j = i + 1; j < SIZE; j++) {
                    for (int k = (SIZE - 1) - (j - i - 1); k >= 1; k--) {
                        if (map[j][k] == symbol) {
                            findSymbolInRow++;
                            buf = k;
                            break;
                        } else if (findSymbolInRow > 0) {
                            findSymbolInRow = 0;
                            break;
                        }
                        if (findSymbolInRow == 0) {
                            break;
                        }
                    }
                    if (findSymbolInRow >= DOTS_TO_BLOCK) {
                        if (j == SIZE - 1) {
                            positionBlockX = j - findSymbolInRow;
                            positionBlockY = buf + findSymbolInRow;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                        if (j == findSymbolInRow + i) {
                            positionBlockX = j + 1;
                            positionBlockY = buf - 1;
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }

                        }
                        if (j >= findSymbolInRow) {
                            positionBlockX = 0;
                            positionBlockX1 = j - findSymbolInRow;
                            positionBlockX2 = j + 1;
                            positionBlockY = 0;
                            positionBlockY1 = buf + findSymbolInRow;
                            positionBlockY2 = buf - 1;
                            if(isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)){
                                int randNum = random.nextInt(2);
                                if(randNum == 0){
                                    positionBlockX = positionBlockX1;
                                    positionBlockY = positionBlockY1;
                                } else {
                                    positionBlockX = positionBlockX2;
                                    positionBlockY = positionBlockY2;
                                }
                            } else if (isCellValid(positionBlockX1, positionBlockY1) && !isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX1;
                                positionBlockY = positionBlockY1;
                            }  else if (!isCellValid(positionBlockX1, positionBlockY1) && isCellValid(positionBlockX2, positionBlockY2)) {
                                positionBlockX = positionBlockX2;
                                positionBlockY = positionBlockY2;
                            }
                            if(isCellValid(positionBlockX, positionBlockY)){
                                map[positionBlockX][positionBlockY] = DOT_O;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }
        if(!flag){
            aiTurn();
        }

    }

    private static boolean checkWin(char symbol) {

        // в строке есть нужное количество символов подряд
        for (char[] row : map) {
            int quantitySymbolInRow = 0;
            for (int i = 0; i < SIZE; i++) {
                if(row[i] == symbol) {
                    quantitySymbolInRow++;
                } else if (quantitySymbolInRow > 0) {
                    quantitySymbolInRow = 0;
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }
        // в ряду есть нужное количество символов подряд
        for (int i = 0; i < SIZE; i++) {
            int quantitySymbolInRow = 0;
            for (int j = 0; j < SIZE; j++) {
                if(map[j][i] == symbol) {
                    quantitySymbolInRow++;
                } else if (quantitySymbolInRow > 0) {
                    quantitySymbolInRow = 0;
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }

        // главная диагональ и все диагонали слева направо
        // X X X X 0
        // 0 X X X X
        // 0 0 X X X
        // 0 0 0 X X
        // 0 0 0 0 X
        for (int i = 0; i < SIZE - 1; i++) {
            int quantitySymbolInRow = 0;
            for (int j = 0; j < SIZE; j++) {
                for (int k = i + j; k < SIZE; k++) {
                    if (map[j][k] == symbol) {
                        quantitySymbolInRow++;
                        break;
                    } else if (quantitySymbolInRow > 0) {
                        quantitySymbolInRow = 0;
                        break;
                    }
                    if (quantitySymbolInRow == 0) {
                        break;
                    }
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }

        // второстепенная диагональ и все диагонали справа налево
        // 0 X X X X
        // X X X X 0
        // X X X 0 0
        // X X 0 0 0
        // X 0 0 0 0
        for (int i = 0; i < SIZE - 1; i++) {
            int quantitySymbolInRow = 0;
            for (int j = 0; j < SIZE; j++) {
                for (int k = SIZE - 1 - i - j; k >= 0; k--) {
                    if (map[j][k] == symbol) {
                        quantitySymbolInRow++;
                        break;
                    } else if (quantitySymbolInRow > 0) {
                        quantitySymbolInRow = 0;
                        break;
                    }
                    if (quantitySymbolInRow == 0) {
                        break;
                    }
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }

        // все диагонали слева вниз без главной диагонали
        // 0 0 0 0 0
        // X 0 0 0 0
        // X X 0 0 0
        // X X X 0 0
        // 0 X X X 0
        for (int i = 0; i < SIZE - 2; i++) {
            int quantitySymbolInRow = 0;
            for (int j = i + 1; j < SIZE; j++) {
                for (int k = j - i - 1; k < SIZE - 1; k++) {
                    if (map[j][k] == symbol) {
                        quantitySymbolInRow++;
                        break;
                    } else if (quantitySymbolInRow > 0) {
                        quantitySymbolInRow = 0;
                        break;
                    }
                    if (quantitySymbolInRow == 0) {
                        break;
                    }
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }

        // все диагонали справа вниз без второстепенной диагонали
        // 0 0 0 0 0
        // 0 0 0 0 X
        // 0 0 0 X X
        // 0 0 X X X
        // 0 X X X 0
        for (int i = 0; i < SIZE - 2; i++) {
            int quantitySymbolInRow = 0;
            for (int j = i + 1; j < SIZE; j++) {
                for (int k = (SIZE - 1) - (j - i - 1); k >= 1; k--) {
                    if (map[j][k] == symbol) {
                        quantitySymbolInRow++;
                        break;
                    } else if (quantitySymbolInRow > 0) {
                        quantitySymbolInRow = 0;
                        break;
                    }
                    if (quantitySymbolInRow == 0) {
                        break;
                    }
                }
                if (quantitySymbolInRow == DOTS_TO_WIN) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean mapIsFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(map[i][j] == DOT_EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    private static void aiTurn() {
        int x,y;
        do {
            x = new Random().nextInt(SIZE);
            y = new Random().nextInt(SIZE);
        } while(!isCellValid(x, y));
        map[x][y] = DOT_O;
    }

    private static void humanTurn() {
        int x,y;
        do {
            System.out.println("Введите координаты");
            System.out.println("Введите X");
            x = scanner.nextInt() - 1;
            System.out.println("Введите Y");
            y = scanner.nextInt() - 1;
        } while(!isCellValid(x, y));
        map[x][y] = DOT_X;
    }

    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && map[x][y] == DOT_EMPTY;
    }

    private static void printMap() {
        for (int i = 0; i < SIZE; i++) {
            System.out.println();
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j]+" ");
            }
        }
        System.out.println();
    }

    private static void initMap() {
        map = new char[SIZE][SIZE];
        for (char[] rows : map) {
            Arrays.fill(rows, DOT_EMPTY);
        }
    }

}
