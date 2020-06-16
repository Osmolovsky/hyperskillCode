package JetBrains;

import java.util.Scanner;

public class CoffeeMachine {
    
    Scanner scanner;
    int water;
    int milk;
    int coffee;
    int cups;
    int money;
    MachineState currentState;
    
    CoffeeMachine(Scanner scanner, int water, int milk, int coffee, int cups, int money) {
        this.scanner = scanner;
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cups = cups;
        this.money = money;
        this.currentState = MachineState.SELECT_ACTION;
    }
    
    enum TypeOfCoffee {
        ESPRESSO(1, 250, 0, 16, 4),
        LATTE(2, 350, 75, 20, 7),
        CAPPUCCINO(3, 200, 100, 12, 6);
        
        int type;
        int water;
        int milk;
        int coffee;
        int cup;
        int price;
        
        TypeOfCoffee(int type, int water, int milk, int coffee, int price) {
            this.type = type;
            this.water = water;
            this.milk = milk;
            this.coffee = coffee;
            this.price = price;
        }
        
        static TypeOfCoffee findByType(int type) {
            for (TypeOfCoffee value: values()) {
                if (type == value.type) {
                    return value;
                }
            }
            return null;
        }
    };
    
    enum MachineState {
        SELECT_ACTION,
        EXIT;
    }
    
    void selectAction() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        String action = scanner.next();
        switch (action) {
                   case "buy":
                       buy();
                       break;
                   case "fill":
                       fill();
                       break;
                   case "take":
                       take();
                       break;
                   case "remaining":
                       showInfo();
                       break;
                   case "exit":
                       currentState = MachineState.EXIT;
                       break;
                   default:
                       System.out.println("Unknown action!");
                       break;
           }    
    }
    
    void showInfo() {
        System.out.println("The coffee machine has:");
        System.out.println(water + " of water");
        System.out.println(milk + " of milk");
        System.out.println(coffee + " of coffee beans");
        System.out.println(cups + " of disposable cups");
        System.out.println(money + " of money");
    }
    
    void take() {
        System.out.println("I gave you $" + money);
        money = 0;
    }
    
    void fill() {
        System.out.println("Write how many ml of water do you want to add:");
        water = water + scanner.nextInt();
        System.out.println("Write how many ml of milk do you want to add:");
        milk = milk + scanner.nextInt();
        System.out.println("Write how many grams of coffee beans do you want to add:");
        coffee = coffee + scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee do you want to add:");
        cups = cups + scanner.nextInt();
    }
    
    void buyOneCup(TypeOfCoffee type) {
        water = water - type.water;
        milk = milk - type.milk;
        coffee = coffee - type.coffee;
        money = money + type.price;
        cups = cups - 1;
    }
    
    void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String select = scanner.next();
        String back = "back";
        if (!select.equals(back)) {
            TypeOfCoffee typeOfCoffee = TypeOfCoffee.findByType(Integer.parseInt(select));
            if (cups < 1 || water < typeOfCoffee.water || milk < typeOfCoffee.milk || coffee < typeOfCoffee.coffee) {
                System.out.println("I have enough resources, making you a coffee!");
            } else {                    
                buyOneCup(typeOfCoffee);
            }
        } 
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine machine = new CoffeeMachine(scanner, 400, 540, 120, 9, 550);
        String exit = "EXIT";
        while (!machine.currentState.name().equals(exit)) {                 
            machine.selectAction();
        }
    }
}
