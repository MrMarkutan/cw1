package control;

import control.model.Manufacturer;
import control.model.Souvenir;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {
    private final Map<Integer, Supplier<Menu>> menus;
    private static final Scanner scanner = new Scanner(System.in);
    private static final ApplicationController ac = new ApplicationController();

    public Main() {
        menus = new HashMap<>();
        menus.put(1, this::createSouvenirsMenu);
        menus.put(2, this::createManufacturersMenu);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        // initDummyData();
        menu();
    }

    private void initDummyData() {
        Manufacturer manufacturer = new Manufacturer("Manufacturer Title", "UA");
        Manufacturer manufacturer2 = new Manufacturer("New Manufacturer Title", "ZL");
        Manufacturer manufacturer3 = new Manufacturer("FAQ MAKA FO", "GB");

        Souvenir souvenir = new Souvenir("Souvenir",
                manufacturer,
                LocalDate.of(2022, Month.OCTOBER, 19),
                100.0);

        Souvenir souvenir2 = new Souvenir("New Souvenir1",
                manufacturer2,
                LocalDate.of(2023, Month.OCTOBER, 19),
                50.0);

        Souvenir souvenir3 = new Souvenir("New Souvenir2",
                manufacturer3,
                LocalDate.of(2023, Month.OCTOBER, 20),
                19.50);

        ac.addSouvenir(souvenir);
        ac.addSouvenir(souvenir2);
        ac.addSouvenir(souvenir3);
    }

    private void menu() {
        while (true) {
            System.out.println("Main Menu");
            List<String> menuList = List.of("Souvenirs", "Manufacturers");
            IntStream.range(0, menuList.size()).forEach(i-> System.out.println((i+1) + ". " + menuList.get(i)));
            System.out.println("0. Exit");
            int choice = userInput();
            if (choice == 0) {
                shutdown();
                return;
            }

            Supplier<Menu> menuSupplier = menus.get(choice);
            if (menuSupplier != null) {
                menuSupplier.get().run();
            } else {
                wrongInput();
            }
        }
    }

    private void printMenuOptions(String title, String... options) {
        System.out.println(title);
        IntStream.range(0, options.length).forEach(i -> System.out.println((i + 1) + ". " + options[i]));
        System.out.println("0. Back");
    }

    private Menu createMenu(String title, Runnable... actions) {
        return new Menu(title, actions);
    }

    private Menu createSouvenirsMenu() {
        return createMenu("Souvenirs:",
                this::addSouvenirMenu,
                this::editSouvenirMenu,
                this::listSouvenirsMenu
        );
    }

    private void addSouvenirMenu() {
        System.out.println("Add souvenir title, manufacturer title, manufacturer country, release date, price" +
                " (e.g., Cup, Ukraine, UA, 2000-02-23, 200).");
        String[] userSouvenir = getUserInputSplitByComma();
        ac.addSouvenir(userSouvenir[0].trim(),
                userSouvenir[1].trim(),
                userSouvenir[2].trim(),
                userSouvenir[3].trim(),
                Double.parseDouble(userSouvenir[4].trim()));
    }

    private void editSouvenirMenu() {
        System.out.print("Enter Souvenir title: ");
        String oldTitle = scanner.nextLine();

        System.out.println("\nEnter new title: (blanc to leave old title) ");
        String newTitle = scanner.nextLine();
        System.out.println("\nEnter new manufacturer title: (blanc to leave old manufacturer title) ");
        String manufacturerTitle = scanner.nextLine();
        System.out.println("\nEnter new manufacturer country: (blanc to leave old manufacturer country) ");
        String manufacturerCountry = scanner.nextLine();
        System.out.println("\nEnter new release date: (blanc to leave old release date) ");
        String newReleaseDate = scanner.nextLine();
        System.out.println("\nEnter new price: (blanc to leave old price) ");
        String newPrice = scanner.nextLine();
        ac.updateSouvenir(oldTitle.trim(),
                newTitle.trim(),
                manufacturerTitle.trim(),
                manufacturerCountry.trim(),
                newReleaseDate.trim(),
                newPrice.trim());
    }

    private void listSouvenirsMenu() {
        System.out.println("""
                Do you want to list
                1. All souvenirs
                2. Souvenirs by specific country
                3. Souvenirs by specific manufacturer
                4. Souvenirs by years
                5. Souvenir Manufacturers in year""");
        switch (userInput()) {
            case 1:
                ac.listAllSouvenirs();
                break;
            case 2:
                System.out.println("Enter the country");
                ac.listSouvenirsByCountry(scanner.nextLine());
                break;
            case 3:
                System.out.println("Enter the Manufacturer title");
                ac.listSouvenirsByManufacturerTitle(scanner.nextLine());
                break;
            case 4:
                ac.listAllSouvenirsByYears();
                break;
            case 5:
                System.out.println("Enter Souvenir title and year (e.g., Cup, 2020)");
                String[] userInput = getUserInputSplitByComma();
                ac.listManufacturersForSouvenirInYear(userInput[0].trim(), Integer.parseInt(userInput[1].trim()));
                break;
            default:
                wrongInput();
                break;
        }
    }

    private Menu createManufacturersMenu() {
        return createMenu("Manufacturers:",
                this::addManufacturerMenu,
                this::editManufacturerMenu,
                this::listManufacturersMenu,
                this::deleteManufacturerMenu
        );
    }

    private void addManufacturerMenu() {
        System.out.println("Add Manufacturer title and country (e.g., Ukraine, UA).");
        String[] userManufacturer = getUserInputSplitByComma();
        ac.addManufacturer(userManufacturer[0].trim(), userManufacturer[1].trim());
    }

    private void editManufacturerMenu() {
        System.out.print("Enter Manufacturer title: ");
        String oldTitle = scanner.nextLine();

        System.out.println("\nEnter new title: (blank to leave old title) ");
        String newTitle = scanner.nextLine();
        System.out.println("\nEnter new country: (blank to leave old country) ");
        String newCountry = scanner.nextLine();
        ac.updateManufacturer(oldTitle, newTitle, newCountry);
    }

    private void listManufacturersMenu() {
        System.out.println("""
                Do you want to list all manufacturers and their souvenirs
                1. Manufacturers and souvenirs
                2. Manufacturers only
                3. Manufacturers with the price lower than""");
        switch (userInput()) {
            case 1:
                ac.listAllManufacturersAndTheirsSouvenirs();
                break;
            case 2:
                ac.listAllManufacturers();
                break;
            case 3:
                System.out.println("Enter the price:");
                ac.listManufacturersWhichPricesLowerThan(Double.parseDouble(scanner.nextLine()));
                break;
            default:
                wrongInput();
                break;
        }
    }

    private void deleteManufacturerMenu() {
        System.out.print("Enter Manufacturer title to delete: ");
        ac.removeManufacturer(scanner.nextLine());
    }

    private void shutdown() {
        scanner.close();
        System.out.println("Shutting down the program.");
        System.out.println("Saving results...");
        ac.saveData();
        System.out.println("Bye!");
    }

    private void wrongInput() {
        System.out.println("Wrong action. Try again.");
    }

    private int userInput() {
        String s = scanner.nextLine();
        return isDigits(s) ? Integer.parseInt(s) : 999;
    }

    private static String[] getUserInputSplitByComma() {
        return scanner.nextLine().split(",");
    }

    private boolean isDigits(String s) {
        if (!s.isEmpty()) {
            if (s.length() == 1 || s.length() == 2) {
                for (char c : s.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private class Menu {
        private final String title;
        private final List<Runnable> actions;
        private final List<String> actionName = List.of("Add", "Edit", "List", "Remove");

        public Menu(String title, Runnable... actions) {
            this.title = title;
            this.actions = Arrays.asList(actions);
        }

        public void run() {
            while (true) {
                printMenuOptions(title, actionName.toArray(String[]::new));
                int choice = userInput();
                if (choice == 0) {
                    return;
                }

                if (choice > 0 && choice <= actions.size()) {
                    actions.get(choice - 1).run();
                } else {
                    wrongInput();
                }
            }
        }
    }
}
