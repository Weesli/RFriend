package net.weesli.shared.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.weesli.shared.enums.FriendSetting;
import net.weesli.shared.model.UserInvite;

public class GsonProvider {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(FriendSetting.class, new FriendSettingSerializer())
                .registerTypeAdapter(UserInvite.class, new UserInviteSerializer())
                .create();
    }
}


