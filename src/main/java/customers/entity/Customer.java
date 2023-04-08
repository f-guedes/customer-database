/**
 * 
 */
package customers.entity;

import java.util.LinkedList;
import java.util.List;


public class Customer {
  private Integer customerId;
  private String customerName;
  private List<Project> projects = new LinkedList<>();


  @Override
  public String toString() {
  String result = "";
  result += "\n Customer ID: " + customerId;
  result += "\n Customer name: " + customerName;
  return result;
  }


  /*
   * Getters and Setters
   */

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public List<Project> getProjects() {
    return projects;
  }

}
