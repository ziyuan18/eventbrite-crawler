package ai.preferred.crawler.eventbrite.master;

import ai.preferred.crawler.EntityCSVStorage;
import ai.preferred.crawler.eventbrite.entity.EBEvent;
import ai.preferred.venom.Crawler;
import ai.preferred.venom.Session;
import ai.preferred.venom.SleepScheduler;
import ai.preferred.venom.fetcher.AsyncFetcher;
import ai.preferred.venom.fetcher.Fetcher;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.validator.EmptyContentValidator;
import ai.preferred.venom.validator.StatusOkValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class EventListCrawler {
	
	static final Session.Key<EntityCSVStorage<EBEvent>> STORAGE_KEY = new Session.Key<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(EventListCrawler.class);
	
	public static void main(String[] args) {

		// Get file to save to
		final String filename = "data/eventbrite.csv";

		// Start CSV printer
		try (final EntityCSVStorage<EBEvent> storage = new EntityCSVStorage<>(filename)) {

		  // Let's init the session, this allows us to retrieve the array list in the handler
		  final Session session = Session.builder()
			  .put(STORAGE_KEY, storage)
			  .build();

		  // Start crawler
		  try (final Crawler crawler = createCrawler(createFetcher(), session).start()) {
			LOGGER.info("starting crawler...");

			final String startUrl = "https://www.eventbrite.com/d/singapore--singapore/all-events/";
			crawler.getScheduler().add(new VRequest(startUrl), new EventListHandler());
		  } catch (Exception e) {
			LOGGER.error("Could not run crawler: ", e);
		  }

		} catch (IOException e) {
		  LOGGER.error("unable to open file: {}, {}", filename, e);
		}
	}
	
	private static Fetcher createFetcher() {
		return AsyncFetcher.builder()
        .setValidator(
            new EmptyContentValidator(),
            new StatusOkValidator())
            //new ListingValidator())
        .build();
  }

	private static Crawler createCrawler(Fetcher fetcher, Session session) {
		return Crawler.builder()
			.setFetcher(fetcher)
			.setSession(session)
			// Just to be polite
			.setSleepScheduler(new SleepScheduler(1500, 3000))
			.build();
	}
}