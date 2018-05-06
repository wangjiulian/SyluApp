package com.dpl.syluapp.model;

public class EmptyRoomInfo {
	private String room1;
	private String room2;
	private String room3;
	private String room4;
	public String getRoom4() {
		return room4;
	}
	public void setRoom4(String room4) {
		this.room4 = room4;
	}
	public String getRoom1() {
		return room1;
	}
	@Override
	public String toString() {
		return "emptyroomInfo [room1=" + room1 + ", room2=" + room2
				+ ", room3=" + room3 + "]";
	}
	public void setRoom1(String room1) {
		this.room1 = room1;
	}
	public String getRoom2() {
		return room2;
	}
	public void setRoom2(String room2) {
		this.room2 = room2;
	}
	public String getRoom3() {
		return room3;
	}
	public void setRoom3(String room3) {
		this.room3 = room3;
	}

}
