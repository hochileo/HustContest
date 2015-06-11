package vn.hust.edu.model;

import java.util.List;

public class Subject {
private String id;
private String name;
private String instutide;
private String department;
private List<SubSubject> listSubSubject;
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
public String getInstutide() {
	return instutide;
}
public void setInstutide(String instutide) {
	this.instutide = instutide;
}
public String getDepartment() {
	return department;
}
public void setDepartment(String department) {
	this.department = department;
}
public List<SubSubject> getListSubSubject() {
	return listSubSubject;
}
public void setListSubSubject(List<SubSubject> listSubSubject) {
	this.listSubSubject = listSubSubject;
}

}
