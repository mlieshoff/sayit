package alexa;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class AlexaServlet extends SpeechletServlet {

    public AlexaServlet() {
        System.setProperty("com.amazon.speech.speechlet.servlet.supportedApplicationIds", "amzn1.ask.skill.508d8df6-52b2-47d8-ac9c-acb3ebf723f6");
        setSpeechlet(new AlexaSpeechlet());
    }

}
