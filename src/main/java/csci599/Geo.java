package csci599;

import java.io.*;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.geo.topic.GeoParser;
import org.apache.tika.Tika;

public class Geo {
    public static void extract(File folder) {
        GeoParser parser;
        try {
            parser = new GeoParser();
        } catch (Exception ex) {
            System.err.println("Cannot load en-ner-location.bin. Make sure it's on the classpath.");
            return;
        }
        Tika tika = new Tika(TikaConfig.getDefaultConfig().getDetector(), parser);
        FileUtil.forEach(folder, file -> {
            Metadata meta = new Metadata();
            try (InputStream stream = TikaInputStream.get(file, meta)) {
                tika.parse(stream, meta);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(meta);
        });
    }
}
