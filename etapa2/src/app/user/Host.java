package app.user;

import app.audio.Collections.Podcast;
import app.page.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class Host extends User {
	private ArrayList<Podcast> podcasts;
	private ArrayList<Announcement> announcements;
	@Setter
	private Page currentPage;

	public Host(User user, ArrayList<Podcast> podcasts, ArrayList<Announcement> announcements) {
		super(user.getUsername(), user.getAge(), user.getCity());
		this.podcasts = podcasts;
		this.announcements = announcements;
	}

	@Getter
	public static class Announcement {
		private String name;
		private String description;

		public Announcement(String name, String description) {
			this.name = name;
			this.description = description;
		}
	}
}
