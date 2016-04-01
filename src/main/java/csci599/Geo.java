package csci599;

import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.*;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.geo.topic.GeoParser;
import org.apache.tika.Tika;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        Map<String, Map<String, String>> json = new HashMap<>();
        FileUtil.forEach(folder, file -> {
            System.out.println(file.getPath());
            Metadata meta = new Metadata();
            try (InputStream stream = TikaInputStream.get(file, meta)) {
                tika.parse(stream, meta);
            } catch (Exception ex) {
                System.err.println("Error parsing file: " + file.getPath());
            }
            Map<String, String> geoInfo = Arrays.stream(meta.names())
                .filter(name -> name.contains("NAME") || name.contains("LONGITUDE") || name.contains("LATITUDE"))
                .collect(toMap(name -> name, name -> meta.get(name)));
            if (!geoInfo.isEmpty()) {
                // key is relative path of file inside folder
                json.put(folder.toURI().relativize(file.toURI()).getPath(), geoInfo);
            }
            // save output as we go since GeoTopicParser is slow
            try {
                new ObjectMapper().writeValue(new File("geotopic.json"), json);
            } catch (IOException ex) {
                System.err.println("Error writing geotopic.json");
            }
            System.out.println(meta);
        });
    }
}
