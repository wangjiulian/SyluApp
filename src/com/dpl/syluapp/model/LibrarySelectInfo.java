package com.dpl.syluapp.model;

import java.io.Serializable;

public class LibrarySelectInfo implements Serializable {
	private String bAuthor;

	public String getbAuthor() {
		return bAuthor;
	}

	public void setbAuthor(String bAuthor) {
		this.bAuthor = bAuthor;
	}

	private String bName;
	private String bBorrowDay;
	private String bReturnDay;
	private String bRenew;
	private String Bbar_code;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getbRenew() {
		return bRenew;
	}

	public void setbRenew(String bRenew) {
		this.bRenew = bRenew;
	}

	public String getBbar_code() {
		return Bbar_code;
	}

	public void setBbar_code(String bbar_code) {
		Bbar_code = bbar_code;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getbBorrowDay() {
		return bBorrowDay;
	}

	public void setbBorrowDay(String bBorrowDay) {
		this.bBorrowDay = bBorrowDay;
	}

	public String getbReturnDay() {
		return bReturnDay;
	}

	@Override
	public String toString() {
		return "LibrarySelectInfo [bAuthor=" + bAuthor + ", bName=" + bName
				+ ", bBorrowDay=" + bBorrowDay + ", bReturnDay=" + bReturnDay
				+ ", bRenew=" + bRenew + ", Bbar_code=" + Bbar_code + ", code="
				+ code + "]";
	}

	public void setbReturnDay(String bReturnDay) {
		this.bReturnDay = bReturnDay;
	}

}
