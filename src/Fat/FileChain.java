package Fat;

import java.util.ArrayList;
import java.util.List;

public class FileChain {
    private int start;
    private FatTable fatTable;
    private List<Integer> clusters;

    public FileChain(int start, FatTable fatTable) {
        this.start = start;
        this.fatTable = fatTable;
        this.clusters = new ArrayList<>();
        buildChain();
    }

    private void buildChain() {
        clusters.add(start);
        int next = fatTable.getCluster(start);
        while (next != ClusterState.FREE.getValue() &&
                next != ClusterState.EOF.getValue() &&
                next != ClusterState.BAD.getValue()) {
            clusters.add(next);
            next = fatTable.getCluster(next);
        }
        clusters.add(next); // Последний кластер
    }

    public void display(int fileNumber) {
        System.out.print(fileNumber + " файл: ");
        for (int cluster : clusters) {
            if (cluster == ClusterState.FREE.getValue()) {
                System.out.print("  ");
            } else if (cluster == ClusterState.EOF.getValue()) {
                System.out.print("EOF ");
            } else if (cluster == ClusterState.BAD.getValue()) {
                System.out.print("BAD ");
            } else {
                System.out.print(cluster + " ");
            }
        }
        System.out.println();
    }

    public List<Integer> getClusters() {
        return clusters;
    }

    public int getStart() {
        return start;
    }

}