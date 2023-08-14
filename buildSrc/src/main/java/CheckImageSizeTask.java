import com.yuushya.ui.Mode;
import com.yuushya.utils.ImageSizeReader;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;

public class CheckImageSizeTask extends DefaultTask {
    public String path = ".";
    public CheckImageSizeTask(){
        this.setDescription("Check the image size");
    }
    @TaskAction
    public void start(){
        Mode.imageSize = Mode.registerTableType("image");
        ImageSizeReader imageSizeReader = new ImageSizeReader(Path.of(this.path));
        imageSizeReader.readAllPng();
        Mode.print();
    }
}
