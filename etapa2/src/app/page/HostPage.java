package app.page;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;
import lombok.Getter;

@Getter
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
			out.append(":\n\t[");
			for (Episode episode : podcast.getEpisodes()) {
				out.append(episode.getName());
				out.append(" - ");
				out.append(episode.getDescription());
				out.append(", ");
			}
			if (!podcast.getEpisodes().isEmpty()) {
				out.deleteCharAt(out.length() - 1); // delete last comma
				out.deleteCharAt(out.length() - 1); // delete last space
			}
			out.append("]\n, ");
		}
		if (!host.getPodcasts().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last space
		}
// "message": "Podcasts:\n\t[Auditory Adventures:\n\t[Exploring Sonic Landscapes - Journey through the world's most unique auditory experiences., The Future of Music Technology - A look into the innovations shaping the future of music production and listening., Cultural Beats - An exploration of how different cultures influence music genres.]\n, Harmony & History:\n\t[The Roots of Blues - Tracing the origins and evolution of blues music., Classical Music's Modern Impact - Discussing how classical music continues to influence contemporary artists., Jazz Journeys - Exploring the rich history and vibrant present of jazz music., The Folk Revival - Examining the resurgence of folk music in modern times.]\n]\n\nAnnouncements:\n\t[Announcement2\n\tAl doilea anunt adaugat!\n]",
// "message": "Podcasts:\n\t[Auditory Adventures:\n\t[Exploring Sonic Landscapes - Journey through the world's most unique auditory experiences., The Future of Music Technology - A look into the innovations shaping the future of music production and listening., Cultural Beats - An exploration of how different cultures influence music genres.]\n, Harmony & History:\n\t[The Roots of Blues - Tracing the origins and evolution of blues music., Classical Music's Modern Impact - Discussing how classical music continues to influence contemporary artists., Jazz Journeys - Exploring the rich history and vibrant present of jazz music., The Folk Revival - Examining the resurgence of folk music in modern times.]\n]\n\nAnnouncements:\n\t[Announcement2:\n\tAl doilea anunt adaugat!\n]",
		out.append("]\n\nAnnouncements:\n\t[");
		for (Host.Announcement announcement : host.getAnnouncements()) {
			out.append(announcement.getName());
			out.append(":\n\t");
			out.append(announcement.getDescription());
			out.append("\n, ");
		}
		if (!host.getAnnouncements().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}
		out.append("]");

		return out.toString();
	}
}
