package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Activation {
	@Id
	private String activationKey;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_EmployeeActivationEmployee"))
	private Employee employee;

	public Activation() {}

	@PrePersist
	protected void onCreate() {
		this.activationKey = UUID.randomUUID().toString();
		this.registrationDate = new Date();
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Activation(Employee employee) {
		this.employee = employee;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
