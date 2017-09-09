package elite.alexa;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class AlexaServlet extends SpeechletServlet {

    public AlexaServlet() {
        System.setProperty("com.amazon.speech.speechlet.servlet.supportedApplicationIds", "amzn1.ask.skill.d4cd2bc9-9efd-4f0c-b281-1e88162efcbd");
        setSpeechlet(new AlexaSpeechlet());
    }

}
