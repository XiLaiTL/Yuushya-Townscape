import com.yuushya.collision.CollisionFileCreator;
import com.yuushya.datagen.ConfigReader;
import com.yuushya.datagen.JarCreator;
import com.yuushya.datagen.ZipReader;
import com.yuushya.ui.Mode;
import com.yuushya.ui.YuushyaLog;
import com.yuushya.utils.ImageSizeReader;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Files;
import java.nio.file.Path;

public class CreateAssetsAndDataTask extends DefaultTask {
    public String path = ".";
    public CreateAssetsAndDataTask(){
        this.setDescription("Create Yuushya Assets And Data");
    }
    @TaskAction
    public void start() {
        System.out.println("Begin Creating");
        System.out.println(Path.of(this.path).toAbsolutePath().toString());
        JarCreator jarCreator = new JarCreator("yuushya", Path.of(this.path));
        jarCreator.createJson();
        CollisionFileCreator collisionFileCreator=new CollisionFileCreator("yuushya",Path.of(this.path));
        collisionFileCreator.createJson();
        System.out.println("Create Success");
    }
}
