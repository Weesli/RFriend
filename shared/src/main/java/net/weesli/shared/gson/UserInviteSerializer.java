package net.weesli.shared.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.weesli.shared.model.UserInvite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserInviteSerializer extends TypeAdapter<UserInvite> {
    @Override
    public void write(JsonWriter out, UserInvite value) throws IOException {
        out.beginObject();

        out.name("receiver").value(String.valueOf(value.getReceiver()));

        out.name("senders");
        out.beginArray();
        for (UUID uuid : value.getSenders()) {
            out.value(String.valueOf(uuid));
        }
        out.endArray();

        out.endObject();
    }

    @Override
    public UserInvite read(JsonReader in) throws IOException {
        in.beginObject();
        UUID receiver = null;
        List<UUID> senders = new ArrayList<>();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("receiver")) {
                receiver = UUID.fromString(in.nextString());
            } else if (name.equals("senders")) {
                in.beginArray();
                while (in.hasNext()) {
                    senders.add(UUID.fromString(in.nextString()));
                }
                in.endArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return new UserInvite(receiver, senders);
    }

}
