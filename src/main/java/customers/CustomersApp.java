package customers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import customers.entity.Customer;
import customers.entity.Project;
import customers.exception.DbException;
import customers.service.ProjectService;

/**
 * This class is a menu-driven application that takes user input from the console and performs
 * operations on the project tables....
 * 
 */

public class CustomersApp {
  private Scanner scanner = new Scanner(System.in);
  private ProjectService projectService = new ProjectService();

  // @formatter:off
  private List<String> operations = List.of(
      "1) Create and populate database tables",
      "2) Add a project",
      "3) List all customers",
      "4) List all projects"
  );
  // @formatter:on

  /**
   * Entry point for Java application.
   * 
   * @param args Unused.
   */
  public static void main(String[] args) {
    new CustomersApp().processUserSelections();
  }

  /**
   * This method prints the operations, gets a user menu selection, and performs the requested
   * operation. It repeats until the user requests that the application terminate.
   */
  private void processUserSelections() {
    boolean done = false;

    while (!done) {
      try {
        int selection = getUserSelection();

        switch (selection) {
          case -1:
            done = exitMenu();
            break;

          case 1:
            createTables();
            break;

          case 2:
            addCustomer();
            break;

          case 3:
            listCustomers();
            break;

          case 4:
            listProjects();
            break;

          default:
            System.out.println("\n" + selection + " is not a valid selection. Try again.");
            break;
        }
      } catch (Exception e) {
        System.out.println("\nError: " + e + ". Try again.");
      }
    }
  }

  /**
   * This method invokes the printOperations method, which prints the available menu selections. It
   * then gets the user's menu selection from the console and converts it to an int.
   * 
   * @return The menu selection is an int or -1 if nothing is selected.
   */
  private int getUserSelection() {
    printOperations();

    Integer input = getIntInput("Enter a menu selection here or press the Enter key to quit: ");

    return Objects.isNull(input) ? -1 : input;
  }

  /**
   * Print the menu selections, one per line.
   */
  private void printOperations() {
    System.out.println("\nPrinting available options below...");

    /* With Lambda expression */
    operations.forEach(line -> System.out.println("   " + line));

    /*
     * That can also be achieved with an enhanced for loop: for(String line : operations) {
     * System.out.println(" " + line); }
     */
  }

  /**
   * Called when the user wants to exit the application. It prints a message and returns {@code true}
   * to terminate the app.
   * 
   * @return {@code true}
   */
  private boolean exitMenu() {
    System.out.println("Exiting the menu...");
    return true;
  }

  /**
   * Called in the menu when the user wants to create and populate the tables. It calls the
   * createAndPopulateTables method in ProjectService, which connects to the database and queries the
   * table creations as well as saved insert queries from main/resources.
   */
  private void createTables() {
    projectService.createAndPopulateTables();
    System.out.println(
        "\nSuccessfully connected to the database. Tables have been created and populated with provided files.\n");

  }

  /**
   * Gather user input for a project row then call the project service to create the row.
   */
  private void addCustomer() {
    String customerName = getStringInput("Enter the customer's name: ");
    Integer projectId = getIntInput("Enter the project ID number: ");
    BigDecimal grossPrice = getDecimalInput("Enter the project's gross price: ");
    BigDecimal systemSizeInKw = getDecimalInput("Enter the system size in KW: ");
    BigDecimal dealerFees = getDecimalInput("Enter any dealer fees or press the Enter key to skip: ");
    BigDecimal adders = getDecimalInput("Enter the total adders cost or press the Enter key to skip: ");
    Boolean installed =
        getBooleanInput("Has this project been installed? Enter Y or N, or press the Enter key to skip: ");
    Integer installYear = getIntInput("Enter the year this project was installed, or press the Enter key to skip: ");
    Integer installMonth = getIntInput(
        "Enter the month this project was installed using 2 numeric digits, or press the Enter key to skip: ");
    BigDecimal repCommission = getDecimalInput("Enter commission for project: ");

    Customer customer = new Customer();
    customer.setCustomerName(customerName);


    Project project = new Project();
    project.setProjectId(projectId);
    project.setGrossPrice(grossPrice);
    project.setSystemSizeInKw(systemSizeInKw);
    project.setDealerFees(dealerFees);
    project.setAdders(adders);
    project.setInstalled(installed);
    project.setInstallYear(installYear);
    project.setInstallMonth(installMonth);
    project.setRepCommission(repCommission);
    project.setInstallYearAndDate(installYear, installMonth);;


    Customer dbCustomer = projectService.addCustomer(customer);
    Project dbProject = projectService.addProject(project);
    System.out
        .println("\nYou have successfully entered the following project to the database: " + dbCustomer + dbProject);
  }

  /**
   * This method lists all customers in the database
   * 
   */
  private void listCustomers() {
    List<Customer> customers = projectService.fetchCustomers();

    System.out.println("\nCustomers in the database:");
    for (Customer customer : customers) {
      // Print customer information
      System.out.println("\n");
      System.out.println("   Id: " + customer.getCustomerId());
      System.out.println("   Name: " + customer.getCustomerName());
      System.out.println("   Projects in the database:\n");


      /**
       * This is still being worked on. The goal is to print information from the Project object with a
       * matching customerId
       */
      // List<Project> projects = customer.getProjects();
      // for (Project project : projects) {
      //
      // }

    }
  }

  /**
   * Lists all projects on the database
   */
  private void listProjects() {
    List<Project> projects = projectService.fetchProjects();

    System.out.println("   Projects in the database:");
    for (Project project : projects) {
      // Print projects information
      System.out.println("\n");
      System.out.println("   CustomerId: " + project.getCustomerId());
      System.out.println("   ProjectId: " + project.getProjectId());
      System.out.println("   Gross Price: " + project.getGrossPrice());
      System.out.println("   System Size(KW): " + project.getSystemSizeInKw());
      System.out.println("   Installed: " + project.getInstalled());
      System.out.println("   Margin: " + project.getRepCommission());



      /**
       * This is still being worked on. The goal is to print the customer name from the Customer object
       * with a matching customerId
       */
      // int customerId = project.getCustomerId();
      // Optional<Customer> customerName = projectService.getCustomerNameFromId(customerId);
      // if (customerName.isPresent()) {
      // customerName.toString();
      // } ;

    }
  }

  /**
   * Prints a prompt on the console and then gets the user's input from the console. If the user
   * enters nothing, {@code null} is returned. Otherwise, the trimmed input is returned.
   * 
   * @param prompt The prompt to print.
   * @return The user's input or {@code null}.
   */
  private String getStringInput(String prompt) {
    System.out.print(prompt);
    String input = scanner.nextLine();

    return input.isBlank() ? null : input.trim();
  }

  /**
   * Prints a prompt on the console and then gets the user's input from the console. It then converts
   * the input to an Integer.
   * 
   * @param prompt The prompt to print.
   * @return If the user enters nothing, {@code null} is returned. Otherwise, the input is converted
   *         to an Integer.
   * @throws DbException Thrown if the input is not a valid Integer.
   */
  private Integer getIntInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }

    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid number.");
    }
  }

  private Boolean getBooleanInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      throw new DbException(" Please enter 'Y' or 'N'");

    } else if (input.trim().equals("y") || input.trim().equals("Y")) {
      return true;
    } else if (input.trim().equals("n") || input.trim().equals("N")) {
      return false;
    }
    return null;
  }

  /**
   * Gets the user's input from the console and converts it to a BigDecimal.
   * 
   * @param prompt The prompt to display on the console.
   * @return A BigDecimal value if successful.
   * @throws DbException Thrown if an error occurs converting the number to a BigDecimal.
   */
  private BigDecimal getDecimalInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }

    try {
      /* Create the BigDecimal object and set it to two decimal places (the scale). */
      return new BigDecimal(input).setScale(3);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid number.");
    }
  }

}
