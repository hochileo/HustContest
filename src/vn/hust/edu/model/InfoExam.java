package vn.hust.edu.model;

import java.util.List;

public class InfoExam {
private boolean status;
private TimeTest timeRemaining;
private TimeTest timeEnd;
private String contest;
private String subject;
private String subsubject;
private String exam;
private int length;
private List<Part> part;
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
public TimeTest getTimeRemaining() {
	return timeRemaining;
}
public void setTimeRemaining(TimeTest timeRemaining) {
	this.timeRemaining = timeRemaining;
}

public TimeTest getTimeEnd() {
	return timeEnd;
}
public void setTimeEnd(TimeTest timeEnd) {
	this.timeEnd = timeEnd;
}
public String getContest() {
	return contest;
}
public void setContest(String contest) {
	this.contest = contest;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getSubsubject() {
	return subsubject;
}
public void setSubsubject(String subsubject) {
	this.subsubject = subsubject;
}
public String getExam() {
	return exam;
}
public void setExam(String exam) {
	this.exam = exam;
}
public List<Part> getPart() {
	return part;
}
public void setPart(List<Part> part) {
	this.part = part;
}
public int getLength() {
	return length;
}
public void setLength(int length) {
	this.length = length;
}

}
