package rcf.extract;

import system.service.ServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Michael Lieshoff
 */
public class Extractor {

    /** span class="text-muted">Inactive</span> */
    /** clan chest completed: <span class="text-success">&check; Completed</span> */

    private final Map<String, ExtractedUser> extractedUsers = new HashMap<>();

    private boolean chestActive = false;

    private void extractMemberData(String content) throws ServiceException {
        TextProcessing.builder()
                .one("<th class=\"font-weight-normal\">Clan Chest Status</th>", "</td>", new Action() {
                    @Override
                    public void execute(String sub) {
                    }
                })
                .chain("<span class=\"text-muted\">", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        if (!sub.contains("Inactive") && !sub.contains("Completed")) {
                            chestActive = true;
                        }
                    }
                })
                .build()
        .start(content);
        TextProcessing.builder()
                .one("<table class=\"table table-inverse table-complex\">", "</table>", new Action() {
                    @Override
                    public void execute(String sub) {
                    }
                })
                .chain("<tbody>", "</tbody>", new Action() {
                    @Override
                    public void execute(String sub) {
                    }
                })
                .chainMulti("<tr id=\"", "</tr>", new Action() {
                    @Override
                    public void execute(String sub) {
                        extractMember(sub);
                    }
                })
                .build()
        .start(content);
    }

    private void extractMember(String content) {
        final AtomicReference<String> currentUser = new AtomicReference<>();
        TextProcessing.builder()
                .one("<a class=\"h4\" href=\"/player/", "</a>", new Action() {
                    @Override
                    public void execute(String sub) {
                        String[] tagAndNick = sub.split("\">");
                        String tag = tagAndNick[0];
                        String nick = tagAndNick[1];
                        ExtractedUser extractedUser = new ExtractedUser();
                        extractedUser.setNickname(nick);
                        extractedUser.setTag(tag);
                        extractedUsers.put(tag, extractedUser);
                        currentUser.set(tag);
                    }
                })
                .one("<span class=\"badge", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        String role = sub.substring(sub.indexOf("\">") + 2);
                        ExtractedUser extractedUser = extractedUsers.get(currentUser.get());
                        extractedUser.setRole(role);
                    }
                })
                .one("<span class=\"text-success\">", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        ExtractedUser extractedUser = extractedUsers.get(currentUser.get());
                        extractedUser.setSpends(Integer.valueOf(sub.replace("&uarr;", "")));
                    }
                })
                .one("<span class=\"text-white\">", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        if (chestActive) {
                            ExtractedUser extractedUser = extractedUsers.get(currentUser.get());
                            extractedUser.setCrowns(Integer.valueOf(sub));
                        }
                    }
                })
                .build()
        .start(content);
    }

    public Map<String, ExtractedUser> start() throws IOException, ServiceException {
        System.setProperty("org.mili.Crawler.cached", "true");
        String content = new Crawler().get("https://cr.deckshop.pro/clan/RP88QQG");
        extractMemberData(content);
        return extractedUsers;
    }

}
