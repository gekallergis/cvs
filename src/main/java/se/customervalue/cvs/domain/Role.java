package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Role {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roleId;

	private String label;

	@ManyToMany(mappedBy = "roles")
	private Collection<Employee> employees = new ArrayList<Employee>();

	public Role() {}

	public Role(String label) {
		this.label = label;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
