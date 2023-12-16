package app.page;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;

public record HostPage(Host host) implements Page {

    /**
     * Print according to Host Page
     *
     * @return final string
     */
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

        out.append("]\n\nAnnouncements:\n\t[");
        for (Host.Announcement announcement : host.getAnnouncements()) {
            out.append(announcement.name());
            out.append(":\n\t");
            out.append(announcement.description());
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
