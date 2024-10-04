package net.weesli.shared.enums;

import net.weesli.shared.RFriend;
import net.weesli.rozsLib.color.ColorBuilder;

public enum Lang {
    already_friend("already-friend"),
    player_not_found("player-not-found"),
    blocked("blocked"),
    invite_already_sent("invite-already-send"),
    invite_sent("invite-sent"),
    no_invite("no-invite"),
    accept_invite("accept-invite"),
    decline_invite("decline-invite"),
    already_blocked("already-blocked"),
    added_block_player("added-block-player"),
    not_blocked("not-blocked"),
    removed_block_player("removed-block-player"),
    not_friend("not-friend"),
    removed_friend("removed-friend"),
    setting_message_disabled("setting-message-disabled");

    private final String TAG;

    Lang(String tag) {
        TAG = tag;
    }

    public String getMessage() {
        return ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.prefix") + RFriend.getInstance().getLang().getString(TAG));
    }
}
