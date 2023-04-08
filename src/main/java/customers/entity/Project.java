/**
 * 
 */
package customers.entity;

import java.math.BigDecimal;
import java.util.Objects;
import customers.exception.DbException;



public class Project {
  private Integer customerId;
  private Integer projectId;
  private BigDecimal grossPrice;
  private BigDecimal systemSizeKw;
  private BigDecimal dealerFees;
  private BigDecimal adders;
  private Boolean installed;
  private Integer installYear;
  private Integer installMonth;
  private BigDecimal repCommission;
  private String installYearAndDate;
  


  @Override
  public String toString() {
    String result = "";
    result += "\n Project ID: " + projectId;
    result += "\n Gross Price: " + grossPrice;
    result += "\n System Size (KW): " + systemSizeKw;
    result += "\n Dealer Fees ($): " + dealerFees;
    result += "\n Adders ($): " + adders;
    result += "\n Installed: " + installYearAndDate;
    result += "\n Commission ($): " + repCommission;
    return result;
  }



  /**
   * Getters and Setters
   */
  
  public void setInstallYearAndDate(Integer year, Integer month) {
    if (Objects.isNull(installed)) {
      installYearAndDate = null;
    } 
    try {
      installYearAndDate = String.valueOf(year) + " / " + String.valueOf(month);
    } catch (NumberFormatException e) {
      throw new DbException(installYear + " / " + installMonth + " is not a valid date.");
    }
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public BigDecimal getGrossPrice() {
    return grossPrice;
  }

  public void setGrossPrice(BigDecimal grossPrice) {
    this.grossPrice = grossPrice;
  }

  public BigDecimal getSystemSizeInKw() {
    return systemSizeKw;
  }

  public void setSystemSizeInKw(BigDecimal systemSizeInKw) {
    this.systemSizeKw = systemSizeInKw;
  }

  public BigDecimal getDealerFees() {
    return dealerFees;
  }

  public void setDealerFees(BigDecimal dealerFees) {
    this.dealerFees = dealerFees;
  }

  public BigDecimal getAdders() {
    return adders;
  }

  public void setAdders(BigDecimal adders) {
    this.adders = adders;
  }

  public Boolean getInstalled() {
    return installed;
  }

  public void setInstalled(boolean installed) {
    this.installed = installed;
  }

  public Integer getInstallYear() {
    return installYear;
  }

  public void setInstallYear(Integer installYear) {
    this.installYear = installYear;
  }

  public Integer getInstallMonth() {
    return installMonth;
  }

  public void setInstallMonth(Integer installMonth) {
    this.installMonth = installMonth;
  }

  public BigDecimal getRepCommission() {
    return repCommission;
  }

  public void setRepCommission(BigDecimal repCommission) {
    this.repCommission = repCommission;
  }


}
