//package Menu;
//
//import Fat.ClusterState;
//import Fat.FatChecker;
//import Fat.FatTable;
//import Utils.Input;
//
//public class EditClusterOption implements MenuOption {
//    private final int CLUSTER_COUNT = 50;
//    private FatChecker fatChecker;
//    private FatTable fatTable;
//
//    public EditClusterOption(FatChecker fatChecker, FatTable fatTable) {
//        this.fatChecker = fatChecker;
//        this.fatTable = fatTable;
//    }
//
//
//    @Override
//    public void action() {
//        fatChecker.display();
//        int index = Input.getInt("Введите номер кластера ( 0 - 49 ):", 0, 49);
//
//        System.out.print("Введите новое значение (-1 для FREE, 111 для EOF, 222 для BAD, или 0-49 для ссылки): ");
//        int value = Input.getInt("Введите новое значение (-1 для FREE, 111 для EOF, 222 для BAD, или 0-49 для ссылки):",
//                -1, 222);
//
//        if (value == ClusterState.FREE.getValue() ||
//                value == ClusterState.EOF.getValue() ||
//                value == ClusterState.BAD.getValue() ||
//                (value >= 0 && value < CLUSTER_COUNT)) {
//            fatTable.setCluster(index, value);
//            System.out.println("Кластер " + index + " изменён на " + formatValue(value));
//            fatChecker.display();
//        } else {
//            System.out.println("Неверное значение!");
//        }
//    }
//
//    @Override
//    public String getDescription() {
//        return "Изменить кластер";
//    }
//
//    private String formatValue(int value) {
//        if (value == ClusterState.FREE.getValue()) return "FREE";
//        if (value == ClusterState.EOF.getValue()) return "EOF";
//        if (value == ClusterState.BAD.getValue()) return "BAD";
//        return String.valueOf(value);
//    }
//
//}
