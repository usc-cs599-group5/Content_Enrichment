package csci599;
import java.io.*;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.geo.topic.GeoParser;
import org.apache.tika.Tika;

public class Geo {
    public static void extract() throws IOException {
        Tika tika = new Tika(TikaConfig.getDefaultConfig().getDetector(), new GeoParser());
        Metadata meta = new Metadata();
        try (InputStream stream = TikaInputStream.get(new java.net.URL("https://raw.githubusercontent.com/chrismattmann/geotopicparser-utils/master/geotopics/polar.geot"), meta)) {
            tika.parse(stream, meta);
        }
        System.out.println(meta);
    }
}
