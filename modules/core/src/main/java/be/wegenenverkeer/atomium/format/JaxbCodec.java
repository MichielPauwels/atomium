package be.wegenenverkeer.atomium.format;

import be.wegenenverkeer.atomium.api.AtomiumDecodeException;
import be.wegenenverkeer.atomium.api.AtomiumEncodeException;
import be.wegenenverkeer.atomium.api.FeedPage;
import be.wegenenverkeer.atomium.api.FeedPageCodec;
import org.xml.sax.InputSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * Created by Karel Maesen, Geovise BVBA on 15/11/16.
 */
public class JaxbCodec<T> implements FeedPageCodec<T,String> {

    private final JAXBContext jaxbContext;

    public JaxbCodec(Class<T> entryTypeMarker) {
        try {
            jaxbContext = JAXBContext.newInstance(FeedPage.class, Link.class, entryTypeMarker);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getMimeType() {
        return "application/xml";
    }

    @Override
    public String encode(FeedPage<T> page) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            marshaller.marshal(page, stream);
            return stream.toString("UTF-8");
        } catch (Exception e){
            throw new AtomiumEncodeException(e.getMessage(), e);
        }
    }

    @Override
    public FeedPage<T> decode(String encoded) {
        try {
            return (FeedPage<T>)jaxbContext.createUnmarshaller().unmarshal(new InputSource(new StringReader(encoded)));
        } catch (JAXBException e) {
            throw new AtomiumDecodeException(e.getMessage(), e);
        }
    }
}
