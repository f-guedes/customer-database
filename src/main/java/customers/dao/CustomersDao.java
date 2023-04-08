
package customers.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.print.attribute.standard.MediaSize.Other;
import customers.entity.Customer;
import customers.entity.Project;
import customers.exception.DbException;
import customers.dao.DbConnection;
import provided.util.DaoBase;

/**
 * This class uses JDBC to perform CRUD operations on the project tables.
 *
 */
@SuppressWarnings("unused")
public class CustomersDao extends DaoBase {
  private static final String CUSTOMERS_TABLE = "customers";
  private static final String PROJECTS_TABLE = "projects";


  /**
   * This method fetches all customers in the database.
   * 
   * @return list of customers from the CUSTOMERS_TABLE
   */
  public List<Customer> fetchAllCustomers() {
    String sql = "SELECT * FROM " + CUSTOMERS_TABLE + " ORDER BY customer_id";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        try (ResultSet rs = stmt.executeQuery()) {
          List<Customer> customers = new LinkedList<>();

          while (rs.next()) {
            customers.add(extract(rs, Customer.class));
          }

          return customers;
        }
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  /**
   * This method fetches all projects in the database
   * 
   * @return list of projects in the PROJECTS_TABLE
   */
  public List<Project> fetchAllProjects() {
    String sql =
        "SELECT customer_id, project_id, gross_price, system_size_kw, dealer_fees, adders, installed, install_year, install_month, rep_commission FROM "
            + PROJECTS_TABLE + " ORDER BY customer_id";
  
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
  
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        try (ResultSet rs = stmt.executeQuery()) {
          List<Project> projects = new LinkedList<>();
  
          while (rs.next()) {
            projects.add(extract(rs, Project.class));
          }
  
          return projects;
        }
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  /**
   * This method fetches a specific customer from the CUSTOMERS_TABLE by its customer_id number and
   * calls the method fetchCustomerProjects for that customer's respective project(s) from the
   * PROJECTS_TABLE.
   * 
   * @param Integer customerId
   * @return customer object, if exists. If null, returns an optional.
   */
  public Optional<Customer> fetchCustomerById(Integer customerId) {
    String sql = "SELECT * FROM " + CUSTOMERS_TABLE + " WHERE customer_id = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try {
        Customer customer = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          setParameter(stmt, 1, customerId, Integer.class);

          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              customer = extract(rs, Customer.class);
            }
          }
        }

        if (Objects.nonNull(customer)) {
          customer.getProjects().addAll(fetchCustomerProjects(conn, customerId));
        }

        return Optional.ofNullable(customer);
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }



  /**
   * This method is called inside the "fetchCustomerById" method to fetch projects from the projects
   * table that match the customers fetched.
   * 
   * @param conn
   * @param customerId
   * @return List of customer projects in the PROJECTS_TABLE
   * @throws SQLException
   */
  private List<Project> fetchCustomerProjects(Connection conn, Integer customerId) throws SQLException {
    //@formatter:off
    String sql = ""
        + "SELECT c.*, p.project_id, p.gross_price, p.system_size_kw, p.dealer_fees, p.adders, p.installed, p.rep_commission "
        + "FROM " + CUSTOMERS_TABLE + " c, " + PROJECTS_TABLE + " p "
        + "WHERE c.customer_id = p.customer_id "
        + "ORDER BY c.customer_id";
    //@formatter:off
    
    try(PreparedStatement stmt = conn.prepareStatement(sql)){
      setParameter(stmt, 1, customerId, Integer.class);;
      try(ResultSet rs = stmt.executeQuery()){
        List<Project> projects = new LinkedList<Project>();
        
        while (rs.next()) {
          Project project = extract(rs, Project.class);
          projects.add(project);
          
        /*
         * Alternatively, you can use the code below inside this loop:
         * projects.add(extract(rs, Project.class));
         */
          
        }
        
        return projects;
      }
    }
  }
    
  
    /**
   * Inserts a customer in the CUSTOMERS_TABLE
   * 
   * @param customer. The customer object to inserted.
   * @return The Customer object with the primary key.
   * @throws DbException Thrown if an error occurs inserting the row.
   */
  public Customer insertCustomer(Customer customer) {
  
    String sql = "INSERT INTO " + CUSTOMERS_TABLE + " " + "(customer_name) " + "VALUES " + "(?)";
  
  
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
  
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        setParameter(stmt, 1, customer.getCustomerName(), String.class);
  
  
        stmt.executeUpdate();
  
        Integer customerId = getLastInsertId(conn, CUSTOMERS_TABLE);
        commitTransaction(conn);
  
        customer.setCustomerId(customerId);
        return customer;
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }


  /**
   * Inserts a project in the PROJECTS_TABLE
   * 
   * @param project. The project object to inserted.
   * @return The project object with the primary key.
   * @throws DbException Thrown if an error occurs inserting the row.
   */
  
  public Project insertProject(Project project) {

  // @formatter:off
  String sql = ""
      + "INSERT INTO " + PROJECTS_TABLE + " "
      + "(project_id, gross_price, system_size_kw, dealer_fees, adders, installed, install_year, install_month, rep_commission) "
      + "VALUES "
      + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
  // @formatter:on

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        setParameter(stmt, 1, project.getProjectId(), Integer.class);
        setParameter(stmt, 2, project.getGrossPrice(), BigDecimal.class);
        setParameter(stmt, 3, project.getSystemSizeInKw(), BigDecimal.class);
        setParameter(stmt, 4, project.getDealerFees(), BigDecimal.class);
        setParameter(stmt, 5, project.getAdders(), BigDecimal.class);
        setParameter(stmt, 6, project.getInstalled(), Boolean.class);
        setParameter(stmt, 7, project.getInstallYear(), Integer.class);
        setParameter(stmt, 8, project.getInstallMonth(), Integer.class);
        setParameter(stmt, 9, project.getRepCommission(), BigDecimal.class);

        stmt.executeUpdate();

        Integer customerId = getLastInsertId(conn, PROJECTS_TABLE);
        commitTransaction(conn);

        project.setCustomerId(customerId);
        return project;
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  /**
   * This method takes the formatted list of String that will populate the database using the data in
   * the DATA_FILE
   * 
   * @param Formatted List of String with the SQL commands to be executed in the database
   */

  public void executeBatch(List<String> sqlBatch) {

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (Statement stmt = conn.createStatement()) {
        for (String sql : sqlBatch) {
          stmt.addBatch(sql);
        }
        stmt.executeBatch();
        commitTransaction(conn);
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }

  }

}
