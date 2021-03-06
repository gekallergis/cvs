package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Role;

import java.io.Serializable;

public class RoleRepresentation implements Serializable {
	private int roleId;

	private String label;

	public RoleRepresentation() {}

	public RoleRepresentation(Role role) {
		this.roleId = role.getRoleId();
		this.label = role.getLabel();
	}

	public RoleRepresentation(int roleId, String label) {
		this.roleId = roleId;
		this.label = label;
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

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}

		if(obj instanceof RoleRepresentation) {
			RoleRepresentation anotherRole = (RoleRepresentation)obj;
			if(this.roleId == anotherRole.roleId) {
				return true;
			}
		}

		return false;
	}
}
