package cn.edu.fudan.se.helpseeking.googleSearch;

public class LogonDataBean {

	private int client_num;
	private String email;

	public void setClient_num(int client_num) {
		this.client_num = client_num;
	}

	private String password = "null";
	private String name = "null";
	private String cellphone = "null";
	private String homephone = "null";
	private String birthday = "null";
	private String address = "null";
	private String school = "null";
	private String workplace = "null";
	private String occupation = "null";

	public int getClient_num() {
		return client_num;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	// 로그인 암호화
	public void setPassword(String password) {
		this.password = password;
	}
}