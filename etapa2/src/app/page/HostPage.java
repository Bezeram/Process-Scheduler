package app.page;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;

public class HostPage implements Page {
	private final Host host;

	public HostPage(Host host) {
		this.host = host;
	}

	@Override
	public String print() {
		StringBuilder out = new StringBuilder();

		out.append("Podcasts:\n\t[");
		for (Podcast podcast : host.getPodcasts()) {
			out.append(podcast.getName());
			out.append(":\n\t");
			for (Episode episode : podcast.getEpisodes()) {
				out.append(episode.getName());
				out.append(" - ");
				out.append(episode.getDescription());
				out.append(", ");
			}
			out.deleteCharAt(out.length() - 1); // delete last comma
			out.deleteCharAt(out.length() - 1); // delete last space

			out.append("], ");
		}
		out.deleteCharAt(out.length() - 1); // delete last space
		out.deleteCharAt(out.length() - 1); // delete last space

		out.append("]\n\nAnnouncements:\n\t[");
		for (Host.Announcement announcement : host.getAnnouncements()) {
			out.append(announcement.getName());
			out.append("\n");
			out.append(announcement.getDescription());
			out.append("\n, ");
		}
		out.deleteCharAt(out.length() - 1); // delete last space
		out.deleteCharAt(out.length() - 1); // delete last comma

		return out.toString();
	}
}
