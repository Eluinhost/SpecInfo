package gg.uhc.specinfo.log.extras;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public abstract class TeleportClickable implements LogExtra {

    abstract String getSubcommand();
    abstract BaseComponent[] getHoverText();

    @Override
    public BaseComponent getChatComponent() {
        TextComponent component = new TextComponent(getRawText());
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/specinfo:silenttp " + getSubcommand()));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getHoverText()));
        component.setColor(ChatColor.GRAY);
        return component;
    }
}
