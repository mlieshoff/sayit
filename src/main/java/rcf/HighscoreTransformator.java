package rcf;

import org.apache.commons.io.FileUtils;
import rcf.achievment.Achievment;
import rcf.achievment.AchievmentType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighscoreTransformator {

    private static final DecimalFormat format = new DecimalFormat("#,###,###,##0");

    public void transform(Cube cube, Map<String, List<UserAchievment>> userAchievments) throws IOException {
        List<IntegerUserData> usersByTotal = cube.getUsersByTotal();
        String html = createHtml(cube, usersByTotal, userAchievments);
        FileUtils.write(new File("/tmp/index.html"), html, "UTF-8");
    }

    private String createHtml(Cube cube, List<IntegerUserData> userDatas, Map<String, List<UserAchievment>> userAchievments) {
        StringBuilder s = new StringBuilder();
        s.append("<table class=\"table table-inverse table-striped\">");
        s.append("<thead>");
        s.append("<tr>");
        s.append("<th>");
        s.append("#");
        s.append("</th>");
        s.append("<th>");
        s.append("Nick");
        s.append("</th>");
        s.append("<th>");
        s.append("Punkte");
        s.append("</th>");
        s.append("</tr>");
        s.append("</thead>");
        s.append("<tbody>");
        for (IntegerUserData userData : userDatas) {
            s.append("<tr>");
            s.append("<th scope=\"row\">");
            s.append(userData.getRank());
            s.append(".</th>");
            s.append("<td>");
            s.append(String.format("<a class=\"h4\" href=\"https://cr.deckshop.pro/player/%s\">%s</a>", userData.getTag(), userData.getName().equals(Cube.TOBI) ? "Tobi" : userData.getName()));
            s.append("<span class=\"pull-right\">");
            s.append(renderAchievments(userAchievments.get(userData.getName())));
            s.append("</span>");
            s.append("</td>");
            s.append("<td>");
            s.append(format.format(userData.getValue()));
            s.append("</td>");
            s.append("</tr>");
        }
        s.append("</tbody>");
        s.append("</table>");
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <!-- Required meta tags -->\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://s.unicode-table.com/css/font-awesome.min.css?fe256\" /><link href=\"https://fonts.googleapis.com/css?family=Cuprum:400,700\" rel=\"stylesheet\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1 align=\"center\">Royal Card Forces Highscore</h1>\n" +
                "%s" +
                "<hr/><p/>%s" +
                "\n" +
                "    <!-- Optional JavaScript -->\n" +
                "    <!-- jQuery first, then Popper.js, then Bootstrap JS -->\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js\" integrity=\"sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js\" integrity=\"sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1\" crossorigin=\"anonymous\"></script>\n" +
                "  </body>\n" +
                "</html>";
        String footer = "<footer class=\"bg-dark text-muted text-center pt-3 pb-4\">\n" +
                "        <div class=\"container\">\n" +
                "\n" +
                "\n" +
                "            <p class=\"mb-3 mt-3\">\n" +
                "                Made with passion and ♥ by micah" +
                "            </p>\n" +
                "\n" +
                "            <p class=\"small\">\n" +
                "                This content is not affiliated with, endorsed, sponsored, or specifically approved by Supercell and Supercell is not responsible for it.\n" +
                "                <br class=\"d-none d-lg-block\">\n" +
                "                For more information see <a href=\"http://www.supercell.com/fan-content-policy\" target=\"_blank\" rel=\"nofollow\" class=\"text-white\">Supercell’s Fan Content Policy</a>.\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </footer>";
        return String.format(template, s.toString(), footer);
    }

    private String renderAchievments(List<UserAchievment> userAchievments) {
        StringBuilder s = new StringBuilder();
        List<String> entries = new ArrayList<>();
        for (UserAchievment userAchievment : userAchievments) {
            Achievment achievment = userAchievment.getAchievment();
            AchievmentType achievmentType = achievment.getAchievmentType();
            /*
            s.append(String.format("<img src=\"%s\" style=\"padding-right: 5px; width: 8%%; height: 8%%\" class=\"img-fluid\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"%s Level: %s (%s*)\" alt=\"Responsive image\">",
                    userAchievment.getAchievment().getImage(),
                    userAchievment.getAchievment().getTitle(),
                    userAchievment.getLevel() + 1,
                    userAchievment.getCount()
                )
            );
            */
            if (achievmentType == AchievmentType.PROGRESSABLE) {
                entries.add(String.format("<span class=\"%s\">%s* %s</span>",
                        getColor(userAchievment.getLevel()),
                        userAchievment.getCount(),
                        userAchievment.getAchievment().getTitle()
                ));
            } else if (achievmentType == AchievmentType.ONEOFF) {
                entries.add(String.format("<span class=\"%s\">%s</span>",
                        getColor(userAchievment.getLevel()),
                        userAchievment.getAchievment().getTitle()
                ));
            } else if (achievmentType == AchievmentType.SINGLE) {
                entries.add(String.format("<span class=\"%s\">%s</span>",
                        "text-info",
                        userAchievment.getAchievment().getTitle()
                ));
            }
        }
        for (int i = 0, n = entries.size(); i < n; i ++) {
            s.append(entries.get(i));
            if (i < n - 1) {
                s.append(", ");
            }

        }
        return s.toString();
    }

    private String getColor(int level) {
        if (level == 0) {
            return "text-muted";
        } else if (level == 1) {
            return "text-primary";
        } else if (level == 2) {
            return "text-success";
        } else if (level == 3) {
            return "text-warning";
        }
        return "text-danger";
    }

}
