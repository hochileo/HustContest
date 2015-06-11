package vn.hust.edu.model;

import java.util.ArrayList;
import java.util.List;

public class Contest {
private boolean status;
private String id;
private String name;
private String semeter;
private List<Subject> subjects = new ArrayList<Subject>();
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
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
public String getSemeter() {
	return semeter;
}
public void setSemeter(String semeter) {
	this.semeter = semeter;
}
public List<Subject> getSubjects() {
	return subjects;
}
public void setSubjects(List<Subject> subjects) {
	this.subjects = subjects;
}


}
