/**
 * 
 */
package customers.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import customers.dao.CustomersDao;
import customers.entity.Customer;
import customers.entity.Project;
import customers.exception.DbException;

/**
 * This class implements the service layer in the 3-tier application. The CRUD operations that the
 * application performs are so simple that this class acts mostly as a pass-through from the input
 * layer to the data layer.
 * 
 */
public class ProjectService {
  private static final String SCHEMA_FILE = "customers-schema.sql";
  private static final String DATA_FILE = "customers-data.sql";

  private CustomersDao customerDao = new CustomersDao();

  public void createAndPopulateTables() {
    loadFromFile(SCHEMA_FILE);
    loadFromFile(DATA_FILE);
  }

  private void loadFromFile(String fileName) {
    String content = readFileContent(fileName);
    List<String> sqlStatements = convertFileContentToSqlStatements(content);

    customerDao.executeBatch(sqlStatements);

    /*
     * If you want to print each line from the SCHEMA_FILE to the console, use the Lambda expression
     * below: sqlStatements.forEach(line -> System.out.println(line));
     */
  }

  private List<String> convertFileContentToSqlStatements(String content) {
    content = removeComments(content);
    content = replaceWhiteSpaceSequencesWithSingleSpace(content);

    return extractLinesFromContent(content);
  }

  private List<String> extractLinesFromContent(String content) {
    List<String> listOfSqlCommands = new LinkedList<>();

    while (!content.isEmpty()) {
      int semicolonPosition = content.indexOf(";");

      if (semicolonPosition == -1) {
        if (!content.isBlank()) {
          listOfSqlCommands.add(content);
        }
        content = "";
      } else {
        listOfSqlCommands.add(content.substring(0, semicolonPosition).trim());
        content = content.substring(semicolonPosition + 1);
      }
    }

    return listOfSqlCommands;
  }

  private String replaceWhiteSpaceSequencesWithSingleSpace(String content) {
    return content.replaceAll("\\s+", " ");
  }

  private String removeComments(String content) {
    StringBuilder builder = new StringBuilder(content);
    int commentPosition = 0;

    while ((commentPosition = builder.indexOf("-- ", commentPosition)) != -1) {
      int endOfLinePosition = builder.indexOf("\n", commentPosition);
      if (endOfLinePosition == -1) {
        builder.replace(commentPosition, builder.length(), "");
      } else {
        builder.replace(commentPosition, endOfLinePosition + 1, "");
      }
    }

    return builder.toString();
  }

  private String readFileContent(String fileName) {
    try {
      Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
      return Files.readString(path);
    } catch (Exception e) {
      throw new DbException(e);
    }
  }


  /**
   * This method simply calls the DAO class to insert a customer row in the CUSTOMERS_TABLE.
   * 
   * @param Customer object.
   * @return The Customer object with the newly generated primary key value.
   */
  public Customer addCustomer(Customer customer) {
    return customerDao.insertCustomer(customer);
  }

  /**
   * This method simply calls the DAO class to insert a project row in the PROJECTS_TABLE.
   * 
   * @param Project object
   * @return The Project object
   */
  public Project addProject(Project project) {
    return customerDao.insertProject(project);
  }

  public List<Customer> fetchCustomers() {
    return customerDao.fetchAllCustomers();
  }

  public List<Project> fetchProjects() {
    return customerDao.fetchAllProjects();
  }

  /**
   * This is still being worked on. It called by the method listProjects() in the I/O layer and calls
   * the fetchCustomerById method in the DAO layer.
   * 
   * @param integer customerId
   */

  public Optional<Customer> getCustomerNameFromId(int customerId) {
    return customerDao.fetchCustomerById(customerId);

  }

}
