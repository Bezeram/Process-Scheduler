package app.user;

public class UserFactory {
	public static User createUser(String userType, String username, int age, String city) {
		return switch (userType) {
			case "user" -> new User(username, age, city);
			case "artist" -> new Artist(username, age, city);
			case "host" -> new Host(username, age, city);
			default -> throw new IllegalStateException("Unexpected user type: " + userType);
		};
	}
}
