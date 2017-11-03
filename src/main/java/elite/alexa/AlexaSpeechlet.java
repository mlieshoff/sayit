package elite.alexa;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import elite.api.EliteService;
import elite.api.dto.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mili.utils.Log;
import org.mili.utils.sql.service.ServiceException;
import org.mili.utils.sql.service.ServiceFactory;

/**
 * @author Michael Lieshoff, 06.07.16
 */
public class AlexaSpeechlet implements Speechlet {


    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        String speechText = "Willkommen Commander";

        String userId = session.getUser().getUserId();

        StringBuilder content = new StringBuilder();

        EliteService eliteService = ServiceFactory.getService(EliteService.class);

        try {
            User user = eliteService.login(userId);
            content.append(user.getCreatedAt());
            content.append("\n");
            content.append(user.getLastLogin());
        } catch (ServiceException e) {
            Log.error(AlexaSpeechlet.class, "onLaunch", "error while login: %s", ExceptionUtils.getStackTrace(e));
        }

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Willkommen Commander");
        card.setContent(content.toString());

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        String speechText = "Hallo Welt";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Hallo Welt");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {

    }
}
