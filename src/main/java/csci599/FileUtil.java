package csci599;

import java.io.File;
import java.util.function.Consumer;

public class FileUtil {
    public static void forEach(File folder, Consumer<File> callback) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                forEach(file, callback);
            } else {
                callback.accept(file);
            }
        }
    }
}
