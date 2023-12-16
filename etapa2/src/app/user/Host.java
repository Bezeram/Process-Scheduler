package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
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
	 *  - A user's selected page is the host's
	 *
	 * @return true / false
	 */
	@Override
	public boolean isSafeToDelete() {
		boolean safeDelete = true;

		for (User user : Admin.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(username))
				continue;

			if (user.player.getCurrentAudioFile() != null) {
				PlayerStats playerStats = user.getPlayerStats();
				if (!playerStats.isPaused() && this.createdEpisodeByName(user.player.getCurrentAudioFile().getName())) {
					safeDelete = false;
					break;
				}
			}

			if (user.getCurrentPage() instanceof HostPage hostPage && hostPage.getHost() == this) {
				safeDelete = false;
				break;
			}
		}

		return safeDelete;
	}

	/**
	 * Checks to see if host has a podcast with an episode with the same name
	 *
	 * @param name the input name
	 * @return true / false
	 */
	public boolean createdEpisodeByName(final String name) {
		for (Podcast podcast : podcasts)
			for (Episode episode : podcast.getEpisodes())
				if (episode.getName().equalsIgnoreCase(name))
					return true;
		return false;
	}

	/**
	 * Checks to see if host has a podcast with the name provided
	 *
	 * @param podcastName the podcast name
	 * @return true / false
	 */
	public boolean hasPodcastByName(final String podcastName) {
		for (Podcast podcast : podcasts)
			if (podcast.getName().equalsIgnoreCase(podcastName))
				return true;
		return false;
	}

	/**
	 * Adds a podcast
	 *
	 * @param podcast the podcast
	 */
	public void addPodcast(final Podcast podcast) {
		podcasts.add(podcast);
	}

	/**
	 * Checks if host has an announcement with the provided name
	 *
	 * @param announcementName the announcement name
	 */
	public boolean hasAnnouncementByName(final String announcementName) {
		for (Announcement announcement : announcements)
			if (announcement.getName().equalsIgnoreCase(announcementName))
				return true;
		return false;
	}

	/**
	 * Adds an announcement
	 *
	 * @param announcement the announcement
	 */
	public void addAnnouncement(final Announcement announcement) {
		announcements.add(announcement);
	}

	/**
	 * Removes an announcement
	 *
	 * @param announcementName the announcement name
	 */
	public void removeAnnouncement(final String announcementName) {
		for (int i = 0; i < announcements.size(); i++)
			if (announcements.get(i).getName().equalsIgnoreCase(announcementName)) {
				announcements.remove(i);
				return;
			}
	}

	/**
	 * It is NOT safe to delete the podcast if a user's player is running it and the current episode is of the host's
	 *
	 * @param albumName the album name is always valid
	 * @return true / false
	 */
	public boolean isPodcastSafeToDelete(final String albumName) {
		boolean safeDelete = true;
		Podcast match = null;
		for (Podcast podcast : podcasts)
			if (podcast.getName().equalsIgnoreCase(albumName))
				match = podcast;

		for (User user : Admin.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(username) || user.player.getCurrentAudioFile() == null)
				continue;

			PlayerStats playerStats = user.getPlayerStats();
			if (!playerStats.isPaused()) {
				safeDelete = false;
				break;
			}

			for (Episode episode : match.getEpisodes()) {
				if (episode.getName().equalsIgnoreCase(user.player.getCurrentAudioFile().getName())) {
					safeDelete = false;
					break;
				}
			}
			if (!safeDelete)
				break;
		}

		return safeDelete;
	}

	/**
	 * Removes a podcast
	 *
	 * @param albumName the album name
	 */
	public void removePodcast(final String albumName) {
		for (int i = 0; i < podcasts.size(); i++)
			if (podcasts.get(i).getName().equalsIgnoreCase(albumName)) {
				podcasts.remove(i);
				return;
			}
	}

	/**
	 * Announcement data structure
	 */
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
