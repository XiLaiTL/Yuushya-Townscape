package net.cocofish.yuushya;

import com.google.gson.*;
import net.cocofish.yuushya.blockentity.ModelFrameBlockEntityRender;
import net.cocofish.yuushya.entity.ExhibitionEntityRender;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.cocofish.yuushya.Yuushya.*;
import static net.cocofish.yuushya.Yuushya.modelframeblockentity;

//@Environment(EnvType.CLIENT)
public class YuushyaRegister extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public int flag=0;
    protected static final Identifier ID = new Identifier("yuushya", "register");
    private static final Gson GSON = new GsonBuilder().create();
    //public  HashMap<String,JsonObject> YuushyaObject=new HashMap<>();
    public YuushyaRegister() {
        super(GSON, "register");

    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }


    @Override

    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        JsonObject root=new JsonObject();
        try {
            BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
            JsonElement je = new JsonParser().parse(br);
            root = JsonHelper.asObject(je, "root");
            br.close();
        }
        catch (Throwable e) {}
        try {
            JsonArray array = root.getAsJsonArray("block");
            List<String> name = new ArrayList<>();
            JsonObject mainobject = new JsonObject();
            for (JsonElement jsonElement1 : array) {
                JsonObject object1 = JsonHelper.asObject(jsonElement1, "normal");
                String object2 = object1.get("name").getAsString();
                mainobject.add(object2, jsonElement1);
                name.add(object2);
            }
            prepared.forEach((fieldId, jsonElement) -> {

                JsonObject object = JsonHelper.asObject(jsonElement, fieldId.toString());
                JsonArray array1 = object.getAsJsonArray("block");
                for (JsonElement jsonElement1 : array1) {
                    JsonObject object1 = JsonHelper.asObject(jsonElement1, "normal");
                    String object2 = object1.get("name").getAsString();
                    mainobject.add(object2, jsonElement1);
                    name.add(object2);
                }

            });
            JsonArray array1 = new JsonArray();
            for (String name1 : name) {
                JsonObject object = mainobject.getAsJsonObject(name1);
                if (object != null) array1.add(object);
                mainobject.remove(name1);
            }
            root.add("block", array1);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
                String s = YM.FormatJsonString(root.toString());
                bw.write(s);
                bw.close();
            } catch (Throwable e) { }
        }
        catch (Throwable e){ }
        try {
            JsonArray array = root.getAsJsonArray("item");
            List<String> name = new ArrayList<>();
            JsonObject mainobject = new JsonObject();
            for (JsonElement jsonElement1 : array) {
                JsonObject object1 = JsonHelper.asObject(jsonElement1, "normal");
                String object2 = object1.get("name").getAsString();
                mainobject.add(object2, jsonElement1);
                name.add(object2);
            }
            prepared.forEach((fieldId, jsonElement) -> {

                JsonObject object = JsonHelper.asObject(jsonElement, fieldId.toString());
                JsonArray array1 = object.getAsJsonArray("item");
                for (JsonElement jsonElement1 : array1) {
                    JsonObject object1 = JsonHelper.asObject(jsonElement1, "normal");
                    String object2 = object1.get("name").getAsString();
                    mainobject.add(object2, jsonElement1);
                    name.add(object2);
                }

            });
            JsonArray array1 = new JsonArray();
            for (String name1 : name) {
                JsonObject object = mainobject.getAsJsonObject(name1);
                if (object != null) array1.add(object);
                mainobject.remove(name1);
            }
            root.add("item", array1);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
                String s = YM.FormatJsonString(root.toString());
                bw.write(s);
                bw.close();
            } catch (Throwable e2) { }
        }
        catch (Throwable e3){}

    }
    public void register()
    {

    }


}
