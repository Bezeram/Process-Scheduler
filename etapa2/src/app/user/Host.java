package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.page.ArtistPage;
import app.page.HostPage;
import app.page.Page;
import app.player.Player;
import app.player.PlayerStats;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

@Getter
public class Host extends User {
	private ArrayList<Podcast> podcasts = new ArrayList<>();
	private ArrayList<Announcement> announcements = new ArrayList<>();

	public Host(String username, int age, String city) {
		super(username, age, city);
		online = false;
	}

	/**
	 * It is NOT safe to delete the host if:<br>
	 *  - A user's player is running a podcast and the episode is of the host's<br>
	 *  - A user's selected page is the artist's
	 *
	 * @return true / false
	 */
	@Override
	public boolean isSafeToDelete() {
		boolean safeDelete = true;

		for (User user : Admin.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(username) || user.player.getCurrentAudioFile() == null)
				continue;

			PlayerStats playerStats = user.getPlayerStats();
			if (!playerStats.isPaused() && this.createdEpisodeByName(user.player.getCurrentAudioFile().getName())) {
				safeDelete = false;
				break;
			}

			if (user.getCurrentPage() instanceof HostPage hostPage && hostPage.getHost() == this) {
				safeDelete = false;
				break;
			}
		}

		return safeDelete;
	}

	public boolean createdEpisodeByName(final String name) {
		for (Podcast podcast : podcasts)
			for (Episode episode : podcast.getEpisodes())
				if (episode.getName().equalsIgnoreCase(name))
					return true;
		return false;
	}

	@Getter
	public static class Announcement {
		private final String name;
		private final String description;

		public Announcement(String name, String description) {
			this.name = name;
			this.description = description;
		}
	}
}
