package Fat;

import java.util.*;

public class FatValidator {
    private final int CLUSTER_COUNT = 50;
    private FatTable fatTable;
    private List<FileChain> files;

    public FatValidator(FatTable fatTable, List<FileChain> files) {
        this.fatTable = fatTable;
        this.files = files;
    }

    public void checkValidity() {
        for (int i = 0; i < files.size(); ) {
            FileChain chain = files.get(i);
            List<Integer> clusters = chain.getClusters();
            int lastCluster = clusters.get(clusters.size() - 1);
            if (lastCluster != ClusterState.EOF.getValue()) {
                System.out.println("Файл с началом " + chain.getStart() + " не валиден, будет изменен");
                //clusters.set(clusters.size() - 1, ClusterState.EOF.getValue());
            } else {
                i++;
            }
        }
    }

    public void findLostChains() {
        // Шаг 1: Собираем все используемые кластеры
        Set<Integer> usedClusters = new HashSet<>();
        for (FileChain chain : files) {
            usedClusters.addAll(chain.getClusters());
        }

        // Шаг 2: Ищем потерянные цепочки
        for (int i = 0; i < CLUSTER_COUNT; i++) {
            int value = fatTable.getCluster(i);
            if (!usedClusters.contains(i) &&
                    value != ClusterState.FREE.getValue() //&&
                //value != ClusterState.EOF.getValue() &&
                /*value != ClusterState.BAD.getValue()*/) {
                // Проверяем, является ли это началом (никто не ссылается на i)
                boolean isStart = true;
                for (int j = 0; j < CLUSTER_COUNT; j++) {
                    if (j != i && fatTable.getCluster(j) == i) { // Исключаем самоссылку
                        isStart = false;
                        break;
                    }
                }
                if (isStart) {
                    FileChain potentialChain = new FileChain(i, fatTable);
                    List<Integer> clusters = potentialChain.getClusters();
                    if (clusters.getLast() == ClusterState.EOF.getValue()) {
                        System.out.println("Найден потерянный файл с началом " + i);
                        files.add(potentialChain);
                        usedClusters.addAll(clusters);

                    }
                    if (clusters.getLast() == ClusterState.BAD.getValue() && clusters.size() != 2) {
                        files.add(potentialChain);
                        usedClusters.addAll(clusters);
                        fatTable.setCluster(clusters.get(clusters.size() - 3), ClusterState.EOF.getValue());
                    }
                }
            }
        }
    }

    public void fixIntersections() {
        // Шаг 1: Собираем все ссылки
        Map<Integer, List<Integer>> references = new HashMap<>();
        for (int i = 0; i < CLUSTER_COUNT; i++) {
            int next = fatTable.getCluster(i);
            if (next != ClusterState.FREE.getValue() &&
                    next != ClusterState.EOF.getValue() &&
                    next != ClusterState.BAD.getValue()) {
                references.computeIfAbsent(next, k -> new ArrayList<>()).add(i);
            }
        }

        // Шаг 2: Обрабатываем пересечения
        for (Map.Entry<Integer, List<Integer>> entry : references.entrySet()) {
            List<Integer> sources = entry.getValue();
            if (sources.size() > 1) { // Есть пересечение
                int target = entry.getKey();
                System.out.println("Пересечение по адресу " + target + " от " + sources);

                // Оставляем первую цепочку, для остальных создаём новые
                for (int i = 1; i < sources.size(); i++) {
                    int source = sources.get(i);
                    int length = countChainLength(target);
                    makeNewChain(source, length);
                }
            }
        }

        // Шаг 3: Обновляем цепочки
        updateChains();
    }

    private int countChainLength(int start) {
        int length = 0;
        int next = start;
        while (next != ClusterState.FREE.getValue() &&
                next != ClusterState.EOF.getValue() &&
                next != ClusterState.BAD.getValue()) {

            length++;
            next = fatTable.getCluster(next);
        }
        return length;
    }

    private int findFreeCluster() {
        for (int i = 0; i < CLUSTER_COUNT; i++) {
            if (fatTable.getCluster(i) == ClusterState.FREE.getValue()) {
                return i;
            }
        }
        return -1; // Нет свободных кластеров
    }

    private void makeNewChain(int start, int length) {
        int current = start;
        for (int i = 0; i < length; i++) {
            int freeCluster = findFreeCluster();
            if (freeCluster == -1) {
                System.out.println("Нет свободных кластеров для исправления пересечения!");
                return;
            }
            fatTable.setCluster(current, freeCluster);
            current = freeCluster;
        }
        fatTable.setCluster(current, ClusterState.EOF.getValue());
    }

    private void updateChains() {
        List<Integer> starts = new ArrayList<>();
        for (FileChain chain : files) {
            starts.add(chain.getStart());
        }
        files.clear();
        for (int start : starts) {
            files.add(new FileChain(start, fatTable));
        }
    }

    public List<FileChain> getFiles() {
        return new ArrayList<>(files);
    }
}