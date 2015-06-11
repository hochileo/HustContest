package vn.hust.edu.model;

import java.util.List;

public class Histories {
private String subject;
private List<InfoHistories> listHistories;

public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public List<InfoHistories> getListHistories() {
	return listHistories;
}
public void setListHistories(List<InfoHistories> listHistories) {
	this.listHistories = listHistories;
}

}
