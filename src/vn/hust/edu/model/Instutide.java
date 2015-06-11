package vn.hust.edu.model;

import java.util.ArrayList;
import java.util.List;

public class Instutide {
	private String id;
	private String name;
	private String mail;
	private String add;
	private String phone;
	private String fax;
	private String web;
	private List<Department> departments = new ArrayList<Department>();
	private List<Classst> classsts = new ArrayList<Classst>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Classst> getClasssts() {
		return classsts;
	}

	public void setClasssts(List<Classst> classsts) {
		this.classsts = classsts;
	}

}
