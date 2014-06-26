package be.vlaanderen.awv.atom.javaapi;

import be.vlaanderen.awv.atom.FeedPosition;
import scala.runtime.BoxedUnit;
import scalaz.NonEmptyList;
import scalaz.Validation;

class FeedProcessor<E> {

    private final be.vlaanderen.awv.atom.FeedProcessor feedProcessorScala;

    FeedProcessor(FeedProvider<E> feedProvider, EntryConsumer<E> entryConsumer) {
        this(null, feedProvider, entryConsumer);
    }

    FeedProcessor(FeedPosition feedPosition, FeedProvider<E> feedProvider, EntryConsumer<E> entryConsumer) {
        feedProcessorScala = new be.vlaanderen.awv.atom.FeedProcessor<E>(
            feedPosition,
            new FeedProviderWrapper<E>(feedProvider),
            new EntryConsumerWrapper<E>(entryConsumer)
        );
    }

    public void start() {
        Validations.toException(feedProcessorScala.start());
    }


}