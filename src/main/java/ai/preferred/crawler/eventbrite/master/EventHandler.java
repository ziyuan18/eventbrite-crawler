package ai.preferred.crawler.eventbrite.master;

import ai.preferred.crawler.EntityCSVStorage;
import ai.preferred.crawler.eventbrite.entity.EBEvent;
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.response.VResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.*; 
import org.jsoup.nodes.*; 
import org.jsoup.select.*; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class EventHandler implements Handler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
	private EBEvent event; 
	
	public EventHandler(EBEvent e){
		event = e; 
	}
	
	private static String textOrNull(Element element) {
		return null == element ? null : element.text();
	}
	
	@Override
	public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
		
		LOGGER.info("processing event: {}", request.getUrl());
		
		final Document document = response.getJsoup();
		Element descElement = document.selectFirst("div[data-automation=listing-event-description]"); 
		event.setDescription(textOrNull(descElement)); 
		
		final EntityCSVStorage<EBEvent> storage = session.get(EventListCrawler.STORAGE_KEY);
		
		worker.executeBlockingIO(() -> {
			LOGGER.info("storing event: {}", event.getTitle()); 
			try {
				storage.append(event);
			} catch (IOException e) {
				LOGGER.error("Unable to store listing.", e); 
			}
		}); 
	}
}
