package Fat;

import java.util.ArrayList;
import java.util.List;

public class FatChecker {
    private FatTable fatTable;
    private List<FileChain> files;
    private List<Integer> fileStarts;

    public FatChecker(int[] fat, List<Integer> fileStarts) {
        this.fatTable = new FatTable(fat);
        this.files = new ArrayList<>();
        this.fileStarts = fileStarts;
        for (int start : fileStarts) {
            files.add(new FileChain(start, fatTable));
        }
    }

    public void check() {
        System.out.println(fileStarts);
        System.out.println("Исходное состояние:");
        display();

        FatValidator validator = new FatValidator(fatTable, files);
        System.out.println("Проверка валидности цепочек:");
        validator.checkValidity();
        files = validator.getFiles();

        System.out.println("Поиск потерянных цепочек:");
        validator.findLostChains();
        files = validator.getFiles();

        System.out.println("Исправление пересечений:");
        validator.fixIntersections();
        files = validator.getFiles();


        System.out.println("Конечное состояние:");
        display();
    }

    public void display() {
        System.out.println("Данные FAT\n");
        for (int i = 0; i < files.size(); i++) {
            files.get(i).display(i + 1);
        }
        fatTable.display();
    }

    public FatTable getFatTable() {
        return fatTable;
    }
}