package Fat;

public class FatTable {
    private final int CLUSTER_COUNT = 50;
    private int[] fat;

    public FatTable(int[] fat) {
        if (fat.length != CLUSTER_COUNT) {
            throw new IllegalArgumentException("Размер таблицы FAT должен быть " + CLUSTER_COUNT);
        }
        this.fat = fat;
    }

    public int getCluster(int index) {
        if (index >= 0 && index < CLUSTER_COUNT) {
            return fat[index];
        }
        throw new IllegalArgumentException("Неверный индекс: " + index);
    }

    public void setCluster(int index, int value) {
        if (index >= 0 && index < CLUSTER_COUNT) {
            fat[index] = value;
        } else {
            throw new IllegalArgumentException("Неверный индекс: " + index);
        }
    }

    public void display() {
        System.out.println("FAT");
        for (int i = 0; i < 10; i++) {
            System.out.printf("\t%d[%3s]   %d[%3s]   %d[%3s]   %d[%3s]   %d[%3s]%n",
                    i, formatCluster(fat[i]),
                    10 + i, formatCluster(fat[10 + i]),
                    20 + i, formatCluster(fat[20 + i]),
                    30 + i, formatCluster(fat[30 + i]),
                    40 + i, formatCluster(fat[40 + i]));
        }
    }

    private String formatCluster(int value) {
        if (value == ClusterState.FREE.getValue()) return " ";
        if (value == ClusterState.EOF.getValue()) return "EOF";
        if (value == ClusterState.BAD.getValue()) return "BAD";
        return String.valueOf(value);
    }
}