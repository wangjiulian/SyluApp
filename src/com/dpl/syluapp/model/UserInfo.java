package com.dpl.syluapp.model;

public class UserInfo {
    private  String phone;
    private  String account;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGander() {
		return gander;
	}
	public void setGander(String gander) {
		this.gander = gander;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getUsersign() {
		return usersign;
	}
	public void setUsersign(String usersign) {
		this.usersign = usersign;
	}
	@Override
	public String toString() {
		return "UserInfo [phone=" + phone + ", account=" + account + ", pswd="
				+ pswd + ", nickname=" + nickname + ", gander=" + gander
				+ ", age=" + age + ", hometown=" + hometown
				+ ", studentNumber=" + studentNumber + ", usersign=" + usersign
				+ ", headPortrait=" + headPortrait + "]";
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	private  String pswd;
    private  String nickname;
    private  String gander;
    private  String age;
    private  String hometown;
    private  String studentNumber;
    private  String usersign;
    private  String headPortrait;
	
	

}
