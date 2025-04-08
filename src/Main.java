import Fat.FatChecker;
import Menu.*;

import java.util.List;

public static void main(String[] args) {
    System.out.println("Программа студентов группы 434\n" +
            "Бахмеова Кирилла Алексеевича* и Гапоняко Фёдора Дмитриевича");
    int[] fat = {
            -1, -1, 3, 4, 5, 6, 111, -1, 9, 26, 222, 111, -1, -1, 16, 222, 26, -1, -1, -1,
            -1, 222, 23, 26, -1, -1, 27, 28, 111, -1, -1, -1, 111, 32, 35, 36, 37, 38, 111,
            -1, -1, -1, -1, 222, -1, -1, -1, 43, 47, 111
    };
    List<Integer> fileStarts = List.of(2, 14, 22, 8);

    FatChecker checker = new FatChecker(fat, fileStarts);
    //FatTable fatTable = checker.getFatTable();

    Menu menu = new Menu();

    menu.addOption(new CheckAndFixFat(checker));
    //menu.addOption(new EditClusterOption(checker, fatTable));

    menu.display();
}