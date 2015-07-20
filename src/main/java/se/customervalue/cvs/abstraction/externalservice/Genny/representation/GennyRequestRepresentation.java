package se.customervalue.cvs.abstraction.externalservice.Genny.representation;

public class GennyRequestRepresentation {
	private int salesDataId;
	private int employeeId;
	private int companyId;
	private int ownedProductId;
	private int languageId;
	private int currencyId;

	public GennyRequestRepresentation() {}

	public GennyRequestRepresentation(int salesDataId, int employeeId, int companyId, int ownedProductId, int languageId, int currencyId) {
		this.salesDataId = salesDataId;
		this.employeeId = employeeId;
		this.companyId = companyId;
		this.ownedProductId = ownedProductId;
		this.languageId = languageId;
		this.currencyId = currencyId;
	}

	public int getSalesDataId() {
		return salesDataId;
	}

	public void setSalesDataId(int salesDataId) {
		this.salesDataId = salesDataId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getOwnedProductId() {
		return ownedProductId;
	}

	public void setOwnedProductId(int ownedProductId) {
		this.ownedProductId = ownedProductId;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
}
