package rcf;

import org.apache.commons.io.FileUtils;
import rcf.achievment.Achievment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class HighscoreTransformator {

    private static final DecimalFormat format = new DecimalFormat("#,###,###,##0");

    public void transform(Cube cube, Map<String, List<UserAchievment>> userAchievments) throws IOException {
        List<IntegerUserData> usersByTotal = cube.getUsersByTotal();
        String html = createHtml(cube, usersByTotal, userAchievments);
        FileUtils.write(new File("/tmp/index.html"), html);
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
        s.append("<th>");
        s.append("Abzeichen");
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
            s.append(userData.getName());
            s.append("</td>");
            s.append("<td>");
            s.append(format.format(userData.getValue()));
            s.append("</td>");
            s.append("<td>");
            s.append(renderAchievments(userAchievments.get(userData.getName())));
            s.append("</td>");
            s.append("</tr>");
        }
        s.append("</tbody>");
        s.append("</table>");
        s.append(achievments(cube));
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <!-- Required meta tags -->\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1 align=\"center\">Royal Card Forces Highscore</h1>\n" +
                "%s" +
                "\n" +
                "    <!-- Optional JavaScript -->\n" +
                "    <!-- jQuery first, then Popper.js, then Bootstrap JS -->\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js\" integrity=\"sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js\" integrity=\"sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1\" crossorigin=\"anonymous\"></script>\n" +
                "  </body>\n" +
                "</html>";
        return String.format(template, s.toString());
    }

    private String achievments(Cube cube) {
        StringBuilder s = new StringBuilder();
        s.append("<table class=\"table table-inverse table-striped\">");
        s.append("<thead>");
        s.append("<tr>");
        s.append("<th>");
        s.append("Abzeichen");
        s.append("</th>");
        s.append("<th>");
        s.append("Bezeichnung");
        s.append("</th>");
        s.append("</tr>");
        s.append("</thead>");
        s.append("<tbody>");
        for (Achievment achievment : cube.getAchievments()) {
            if (achievment.isActive()) {
                s.append("<tr>");
                s.append("<th scope=\"row\">");
                s.append(String.format("<img src=\"%s\" style=\"padding-right: 5px; width: 8%%; height: 8%%\" class=\"img-fluid\" data-toggle=\"tooltip\" data-placement=\"right\" alt=\"Responsive image\">", achievment.getImage()));
                s.append("</th>");
                s.append("<td>");
                s.append(achievment.getTitle());
                s.append("</td>");
                s.append("</tr>");
            }
        }
        s.append("</tbody>");
        s.append("</table>");
        return s.toString();
    }

    private String renderAchievments(List<UserAchievment> userAchievments) {
        StringBuilder s = new StringBuilder();
        for (UserAchievment userAchievment : userAchievments) {
            /*
            s.append(String.format("<img src=\"%s\" style=\"padding-right: 5px; width: 8%%; height: 8%%\" class=\"img-fluid\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"%s Level: %s (%s*)\" alt=\"Responsive image\">",
                    userAchievment.getAchievment().getImage(),
                    userAchievment.getAchievment().getTitle(),
                    userAchievment.getLevel() + 1,
                    userAchievment.getCount()
                )
            );
            */
            s.append(userAchievment.getAchievment().getSign());
        }
        return s.toString();
    }

}
