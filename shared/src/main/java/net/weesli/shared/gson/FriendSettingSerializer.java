package net.weesli.shared.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.weesli.shared.enums.FriendSetting;

import java.io.IOException;

public class FriendSettingSerializer extends TypeAdapter<FriendSetting> {

    @Override
    public void write(JsonWriter out, FriendSetting value) throws IOException {
        out.beginObject();
        out.name("name");
        out.value(value.name());
        out.endObject();
    }

    @Override
    public FriendSetting read(JsonReader in) throws IOException {
        in.beginObject();
        String setting = null;
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("name")) {
                setting = in.nextString();
            }
        }
        in.endObject();
        return FriendSetting.valueOf(setting);
    }
}
