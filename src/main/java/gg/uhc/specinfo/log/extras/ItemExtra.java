package gg.uhc.specinfo.log.extras;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

public class ItemExtra implements LogExtra {

    protected final HoverEvent UNSUPPORTED = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("Unsupported version of Spigot") });

    protected final BaseComponent chat;
    // don't show on console at all
    protected final String raw = "";

    public ItemExtra(ItemStack stack) {
        String contents = ItemStackNBTStringFetcher.readFromItemStack(stack);

        chat = new TextComponent("Item");
        chat.setColor(ChatColor.GRAY);

        if (contents != null) {
            chat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(contents)}));
        } else {
            chat.setHoverEvent(UNSUPPORTED);
        }
    }

    @Override
    public String getRawText() {
        return raw;
    }

    @Override
    public BaseComponent getChatComponent() {
        return chat;
    }
}
