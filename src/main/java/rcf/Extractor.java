package rcf;

import java.io.IOException;

/**
 * @author Michael Lieshoff
 */
public class Extractor {

    /**
     * clan chest completed: <span class="text-success">&check; Completed</span>
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("com.bytediscover.ta.lib.web.Crawler.cached", "true");
        String content = new Crawler().get("https://cr.deckshop.pro/clan/RP88QQG");
        extractMemberData(content);
    }

    private static void extractMemberData(String content) {
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

    private static void extractMember(String content) {
        TextProcessing.builder()
                .one(null, "\"", new Action() {
                    @Override
                    public void execute(String sub) {
                        System.out.println("id: " + sub);
                    }
                })
                .one("<span class=\"badge", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        System.out.println("role: " + sub);
                    }
                })
                .one("<span class=\"text-success\">", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        System.out.println("spends: " + sub);
                    }
                })
                .one("<span class=\"text-white\">", "</span>", new Action() {
                    @Override
                    public void execute(String sub) {
                        System.out.println("crowns: " + sub);
                    }
                })
                .build()
        .start(content);
    }

}
