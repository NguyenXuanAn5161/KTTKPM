package entity;

public class User {
	private String surname;
	private String name;
	private String company;
	private String age;

	public User(String surname, String name, String company, String age) {
		super();
		this.surname = surname;
		this.name = name;
		this.company = company;
		this.age = age;
	}

	public User() {
		super();
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [surname=" + surname + ", name=" + name + ", company=" + company + ", age=" + age + "]";
	}

}
