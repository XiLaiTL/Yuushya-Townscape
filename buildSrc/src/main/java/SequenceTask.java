import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskContainer;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SequenceTask extends DefaultTask {

    @Inject
    public SequenceTask( List<Object> sequence) {
        DefaultTask nextTask=null,task ;
        for(int i=sequence.size()-1;i>=0;i--){
            task = (i==0)? this : this.getProject().getTasks().create(this.getName()+"-"+i,DefaultTask.class);
            task.setDescription(sequence.get(i).toString());
            task.dependsOn(sequence.get(i));
            if(i !=sequence.size()-1)
                task.finalizedBy(nextTask);
            nextTask = task;
        }
    }

    @TaskAction
    public void start() {}
}
/*
tasks.register("build-chain"){
    doFirst {println("begin build chain")}
    dependsOn("build-chain-0")
}
tasks.register("build-chain-${linked_tasks.size()}"){
    doLast { println("finish build chain") }
}
for(int i=linked_tasks.size()-1;i>=0;i--){
    int t= i
    tasks.register("build-chain-${t}"){
        dependsOn(linked_tasks.get(t))
        finalizedBy("build-chain-${t+1}")
    }
}
 */
