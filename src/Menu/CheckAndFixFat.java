package Menu;

import Fat.FatChecker;

public class CheckAndFixFat implements MenuOption {
    private FatChecker fatChecker;

    public CheckAndFixFat(FatChecker fatChecker) {
        this.fatChecker = fatChecker;
    }

    @Override
    public void action() {
        fatChecker.check();
    }

    @Override
    public String getDescription() {
        return "Проверить и исправить FAT";
    }
}
