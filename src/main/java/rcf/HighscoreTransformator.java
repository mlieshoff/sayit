package rcf;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HighscoreTransformator {

    public void transform(Cube cube, Map<String, List<UserAchievment>> userAchievments) throws IOException {
        List<IntegerUserData> usersByTotal = cube.getUsersByTotal();
        String html = createHtml(usersByTotal, userAchievments);
        FileUtils.write(new File("/tmp/test.html"), html);
    }

    private String createHtml(List<IntegerUserData> userDatas, Map<String, List<UserAchievment>> userAchievments) {
        StringBuilder s = new StringBuilder();
        s.append("<div class=\"container\">");
        s.append("<div class=\"row\">");
        s.append("<div class=\"col\">");
        s.append("Rank");
        s.append("</div>");
        s.append("<div class=\"col-6\">");
        s.append("Nick");
        s.append("</div>");
        s.append("<div class=\"col\">");
        s.append("Punkte");
        s.append("</div>");
        s.append("<div class=\"col\">");
        s.append("Auszeichnungen");
        s.append("</div>");
        s.append("</div>");
        for (UserData userData : userDatas) {
            s.append("<div class=\"row\">");
            s.append("<div class=\"col\">");
            s.append(userData.getRank());
            s.append(".</div>");
            s.append("<div class=\"col-6\">");
            s.append(userData.getName());
            s.append("</div>");
            s.append("<div class=\"col\">");
            s.append(userData.getValue());
            s.append("</div>");
            s.append("<div class=\"col\">");
            s.append(renderAchievments(userAchievments.get(userData.getName())));
            s.append("</div>");
            s.append("</div>");
        }
        s.append("</div>");
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
                "    <h1>Hello, world!</h1>\n" +
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

    private String renderAchievments(List<UserAchievment> userAchievments) {
        StringBuilder s = new StringBuilder();
        s.append("<div class=\"row\">");
        for (UserAchievment userAchievment : userAchievments) {
            s.append("<div class=\"col-sm-4\">");
            s.append(String.format("<img src=\"%s\" class=\"img-thumbnail rounded\" alt=\"Responsive image\">", userAchievment.getAchievment().getImage()));
            s.append("</div>");
        }
        s.append("</div>");
        return s.toString();
    }

}
