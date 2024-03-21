import java.util.*;

class User {
    private String username;
    private String password;
    private int age;
    private double weight;
    private double height;

    public User(String username, String password, int age, double weight, double height) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public String getUsername() {
        return username;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public void register() {
        System.out.println("User registered successfully.");
    }
}

abstract class Item {
    protected String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void displayInfo();
}

class FoodItem extends Item {
    private int calories;

    public FoodItem(String name, int calories) {
        super(name);
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public void displayInfo() {
        System.out.println("Food Item: " + name);
        System.out.println("Calories: " + calories);
    }
}

class FoodDatabase {
    public List<FoodItem> foodItems;

    public FoodDatabase() {
        foodItems = new ArrayList<>();
    }

    public void addFoodItem(String name, int calories) {
        foodItems.add(new FoodItem(name, calories));
    }

    public List<FoodItem> searchFoodItem(String name) {
        List<FoodItem> searchResults = new ArrayList<>();
        for (FoodItem item : foodItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                searchResults.add(item);
            }
        }
        return searchResults;
    }
}

class Meal {
    private Map<FoodItem, Double> foodItems;

    public Meal() {
        foodItems = new HashMap<>();
    }

    public void addFoodItem(FoodItem item, double portionSize) {
        foodItems.put(item, portionSize);
    }

    public int calculateTotalCalories() {
        int totalCalories = 0;
        for (Map.Entry<FoodItem, Double> entry : foodItems.entrySet()) {
            totalCalories += entry.getKey().getCalories() * entry.getValue();
        }
        return totalCalories;
    }
}

class NutritionalAnalyzer {
    public int calculateTotalCalories(Meal meal) {
        return meal.calculateTotalCalories();
    }

    public int recommendCalories(User user) {
        double bmi = user.getWeight() / ((user.getHeight() / 100) * (user.getHeight() / 100));
        int recommendedCalories;
        double CALORIES_PER_KG = 30;

        if (bmi < 18.5) {
            recommendedCalories = (int) (user.getWeight() * CALORIES_PER_KG * 1.2); // Underweight
        } else if (bmi >= 18.5 && bmi < 24.9) {
            recommendedCalories = (int) (user.getWeight() * CALORIES_PER_KG * 1.0); // Normal weight
        } else if (bmi >= 24.9 && bmi < 29.9) {
            recommendedCalories = (int) (user.getWeight() * CALORIES_PER_KG * 0.8); // Overweight
        } else {
            recommendedCalories = (int) (user.getWeight() * CALORIES_PER_KG * 0.6); // Obese
        }
        return recommendedCalories;
    }

    public boolean isMealExceedsRecommendation(int recommendedCalories, int consumedCalories) {
        return consumedCalories > recommendedCalories;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Food Tracker!");
        System.out.println("Please register to continue.");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        System.out.print("Enter your weight (in kg): ");
        double weight = scanner.nextDouble();
        System.out.print("Enter your height (in cm): ");
        double height = scanner.nextDouble();
        User user = new User(username, password, age, weight, height);
        user.register();

        FoodDatabase foodDatabase = new FoodDatabase();
        System.out.println("\nAdd food items to the database.");
        while (true) {
            System.out.print("Enter the name of the food item (or 'done' to finish): ");
            String itemName = scanner.next();
            if (itemName.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Enter the number of calories: ");
            int calories = scanner.nextInt();
            foodDatabase.addFoodItem(itemName, calories);
            System.out.println("Food item added to the database.");
        }

        System.out.println("\nFood items in the database:");
        for (FoodItem foodItem : foodDatabase.foodItems) {
            foodItem.displayInfo();
            System.out.println();
        }

        Meal meal = new Meal();
        System.out.println("\nLog your meal.");
        while (true) {
            System.out.print("Enter the name of the food item to add to the meal (or 'done' to finish): ");
            String itemName = scanner.next();
                        if (itemName.equalsIgnoreCase("done")) {
                break;
            }
            List<FoodItem> searchResults = foodDatabase.searchFoodItem(itemName);
            if (searchResults.isEmpty()) {
                System.out.println("Food item not found in the database. Please try again.");
                continue;
            }
            System.out.println("Select the food item from the search results:");
            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println((i + 1) + ". " + searchResults.get(i).getName());
            }
            System.out.print("Enter the number corresponding to the food item: ");
            int choice = scanner.nextInt();
            if (choice < 1 || choice > searchResults.size()) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            FoodItem selectedFoodItem = searchResults.get(choice - 1);
            System.out.print("Enter the portion size: ");
            double portionSize = scanner.nextDouble();
            meal.addFoodItem(selectedFoodItem, portionSize);
            System.out.println("Food item added to the meal.");
        }

        NutritionalAnalyzer analyzer = new NutritionalAnalyzer();
        int totalCalories = analyzer.calculateTotalCalories(meal);
        System.out.println("\nTotal calories for the meal: " + totalCalories);

        int recommendedCalories = analyzer.recommendCalories(user);
        System.out.println("Recommended calories: " + recommendedCalories);

        if (analyzer.isMealExceedsRecommendation(recommendedCalories, totalCalories)) {
            System.out.println("Your meal calorie intake exceeds the recommendation.");
        } else {
            System.out.println("Your meal calorie intake is within the recommendation.");
        }

        int balance = recommendedCalories - totalCalories;
        System.out.println("Balance of calories: " + balance);

        scanner.close();
    }
}
