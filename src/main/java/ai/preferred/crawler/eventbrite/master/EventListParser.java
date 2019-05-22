 package ai.preferred.crawler.eventbrite.master;

import ai.preferred.crawler.eventbrite.entity.EBEvent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.response.VResponse;
import org.jsoup.*; 
import org.jsoup.nodes.*; 
import org.jsoup.select.*; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*; 
import java.util.*;

public class EventListParser {

	static void parseListing(Document document, Scheduler scheduler){
		final Elements events = document.select("div[class=eds-show-down-mn eds-l-mar-top-4] li[data-reactid]");
		for(final Element eb : events) {
			parseEvent(scheduler, eb); 
		}
	}
	
	private static String textOrNull(Element element) {
		return null == element ? null : element.text();
	}
	
	private static void parseEvent(Scheduler scheduler, Element e){
		final EBEvent event = new EBEvent(); 
		event.setTitle(textOrNull(e.select("div[class=event-card__formatted-name--is-clamped]").first())); 
		event.setDateTime(textOrNull(e.select("div[class=eds-media-card-content__sub-content] > div[class=eds-text-bs--fixed eds-text-color--grey-600 eds-l-mar-top-1]").first())); 
		event.setAddress(textOrNull(e.select("div[class=eds-text-bs--fixed eds-text-color--grey-600 eds-l-mar-top-1] > div[class=card-text--truncated__one]").first())); 
		Element link = e.select("a[class=eds-media-card-content__action-link]").first();
		String url = link.attr("href"); 
		event.setUrl(url); 
		scheduler.add(new VRequest(url), new EventHandler(event)); 
	}
	
}
