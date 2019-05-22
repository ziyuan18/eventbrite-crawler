package ai.preferred.crawler.eventbrite.master;

import ai.preferred.crawler.EntityCSVStorage;
import ai.preferred.crawler.eventbrite.entity.EBEvent;
import ai.preferred.crawler.eventbrite.master.EventHandler; 
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.response.VResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class EventListHandler implements Handler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventListHandler.class);
	
	@Override
	public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
		
		LOGGER.info("processing: {}", request.getUrl());

		// JSoup
		final Document document = response.getJsoup();
		
		EventListParser.parseListing(document, scheduler); 

		// Crawl another page if there's a next page
		final String url = request.getUrl();
		try {
			final URIBuilder builder = new URIBuilder(url);
			int currentPage = 1;
			for (final NameValuePair param : builder.getQueryParams()) {
				if ("page".equals(param.getName())) {
					currentPage = Integer.parseInt(param.getValue());
				}
			}
			if(currentPage <= 5){
				builder.setParameter("page", String.valueOf(currentPage + 1));
				final String nextPageUrl = builder.toString();
				// Schedule the next page
				scheduler.add(new VRequest(nextPageUrl), this);
			}
		} catch (URISyntaxException | NumberFormatException e) {
			LOGGER.error("unable to parse url: ", e);
		}
	}
}
