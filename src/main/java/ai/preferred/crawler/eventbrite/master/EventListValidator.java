package ai.preferred.crawler.eventbrite.master;

import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.Response;
import ai.preferred.venom.response.VResponse;
import ai.preferred.venom.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListValidator implements Validator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventListValidator.class);

	@Override
	public Validator.Status isValid(Request request, Response response) {
		final VResponse vResponse = new VResponse(response);
		
		if(vResponse.getHtml().contains("Use Eventbrite")) {
			return Status.VALID;
		}
		
		LOGGER.info("Invalid content");
		return Status.INVALID_CONTENT;
	}
}