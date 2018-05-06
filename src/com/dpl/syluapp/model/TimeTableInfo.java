package com.dpl.syluapp.model;

public class TimeTableInfo {

	/** 科目 */
	String subject;

	/** 上课教室 */
	String address;

	/**
	 * @return 科目
	 */

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCourse() {
		if (subject.equals(""))

			return null;
		else
			return subject + "\n" + "@" + address;
	}

}

