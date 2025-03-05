package me.rochblondiaux.ultralimbo.utils.nbt;

import java.util.*;

import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.*;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {
    static final NbtComponentSerializer INSTANCE = new NbtComponentSerializerImpl();

    @Override
    public @NotNull Component deserialize(@NotNull BinaryTag input) {
        return deserializeAnyComponent(input);
    }

    @Override
    public @NotNull BinaryTag serialize(@NotNull Component component) {
        return serializeComponent(component);
    }

    // DESERIALIZATION

    private @NotNull Component deserializeAnyComponent(@NotNull BinaryTag nbt) {
        if (nbt instanceof CompoundBinaryTag compound) {
            return deserializeComponent(compound);
        } else if (nbt instanceof StringBinaryTag string) {
            return Component.text(string.value());
        } else if (nbt instanceof ListBinaryTag list) {
            ComponentBuilder<?, ?> builder = Component.text();
            for (BinaryTag element : list) {
                builder.append(deserializeAnyComponent(element));
            }
            return builder.build();
        }

        throw new UnsupportedOperationException("Unknown NBT type: " + nbt.getClass().getName());
    }

    private @NotNull Component deserializeComponent(@NotNull CompoundBinaryTag compound) {
        ComponentBuilder<?, ?> builder;
        BinaryTag type = compound.get("type");
        if (type instanceof StringBinaryTag sType) {
            // If type is specified, use that
            builder = switch (sType.value()) {
                case "text" -> deserializeTextComponent(compound);
                case "translatable" -> deserializeTranslatableComponent(compound);
                case "score" -> deserializeScoreComponent(compound);
                case "selector" -> deserializeSelectorComponent(compound);
                case "keybind" -> deserializeKeybindComponent(compound);
                case "nbt" -> deserializeNbtComponent(compound);
                default -> throw new UnsupportedOperationException("Unknown component type: " + type);
            };
        } else {
            // Try to infer the type from the fields present.
            Set<String> keys = compound.keySet();
            if (keys.isEmpty()) {
                return Component.empty();
            } else if (keys.contains("text")) {
                builder = deserializeTextComponent(compound);
            } else if (keys.contains("translate")) {
                builder = deserializeTranslatableComponent(compound);
            } else if (keys.contains("score")) {
                builder = deserializeScoreComponent(compound);
            } else if (keys.contains("selector")) {
                builder = deserializeSelectorComponent(compound);
            } else if (keys.contains("keybind")) {
                builder = deserializeKeybindComponent(compound);
            } else if (keys.contains("nbt")) {
                builder = deserializeNbtComponent(compound);
            } else if (keys.contains("")) {
                //todo This feels like a bug, im not sure why this is created.
                builder = Component.text().content(compound.getString(""));
            } else throw new UnsupportedOperationException("Unable to infer component type");
        }

        // Children
        ListBinaryTag extra = compound.getList("extra");
        if (extra.size() > 0) {
            List<ComponentLike> list = new ArrayList<ComponentLike>();
            for (BinaryTag child : extra) list.add(deserializeAnyComponent(child));
            builder.append(list);
        }

        // Formatting
        builder.style(deserializeStyle(compound));

        return builder.build();
    }

    @Override
    public @NotNull Style deserializeStyle(@NotNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound)) {
            return Style.empty();
        }

        Style.Builder style = Style.style();
        String color = compound.getString("color");
        if (!color.isEmpty()) {
            TextColor hexColor = TextColor.fromHexString(color);
            if (hexColor != null) {
                style.color(hexColor);
            } else {
                NamedTextColor namedColor = NamedTextColor.NAMES.value(color);
                if (namedColor != null) {
                    style.color(namedColor);
                } else {
                    throw new UnsupportedOperationException("Unknown color: " + color);
                }
            }
        }
        @Subst("minecraft:default") String font = compound.getString("font");
        if (!font.isEmpty()) style.font(Key.key(font));
        BinaryTag bold = compound.get("bold");
        if (bold instanceof ByteBinaryTag b)
            style.decoration(TextDecoration.BOLD, b.value() == 1 ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);
        BinaryTag italic = compound.get("italic");
        if (italic instanceof ByteBinaryTag b)
            style.decoration(TextDecoration.ITALIC, b.value() == 1 ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);
        BinaryTag underlined = compound.get("underlined");
        if (underlined instanceof ByteBinaryTag b)
            style.decoration(TextDecoration.UNDERLINED, b.value() == 1 ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);
        BinaryTag strikethrough = compound.get("strikethrough");
        if (strikethrough instanceof ByteBinaryTag b)
            style.decoration(TextDecoration.STRIKETHROUGH, b.value() == 1 ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);
        BinaryTag obfuscated = compound.get("obfuscated");
        if (obfuscated instanceof ByteBinaryTag b)
            style.decoration(TextDecoration.OBFUSCATED, b.value() == 1 ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);

        // Interactivity
        String insertion = compound.getString("insertion");
        if (!insertion.isEmpty()) style.insertion(insertion);
        CompoundBinaryTag clickEvent = compound.getCompound("clickEvent");
        if (clickEvent.size() > 0) style.clickEvent(deserializeClickEvent(clickEvent));
        CompoundBinaryTag hoverEvent = compound.getCompound("hoverEvent");
        if (hoverEvent.size() > 0) style.hoverEvent(deserializeHoverEvent(hoverEvent));

        return style.build();
    }

    private @NotNull ComponentBuilder<?, ?> deserializeTextComponent(@NotNull CompoundBinaryTag compound) {
        String text = compound.getString("text");
        return Component.text().content(text);
    }

    private @NotNull ComponentBuilder<?, ?> deserializeTranslatableComponent(@NotNull CompoundBinaryTag compound) {
        String key = compound.getString("translate");
        TranslatableComponent.Builder builder = Component.translatable().key(key);

        BinaryTag fallback = compound.get("fallback");
        if (fallback instanceof StringBinaryTag s) builder.fallback(s.value());

        ListBinaryTag args = compound.getList("with", BinaryTagTypes.COMPOUND);
        if (args.size() > 0) {
            List<ComponentLike> list = new ArrayList<ComponentLike>();
            for (BinaryTag arg : args) list.add(deserializeComponent((CompoundBinaryTag) arg));
            builder.arguments(list);
        }

        return builder;
    }

    private @NotNull ComponentBuilder<?, ?> deserializeScoreComponent(@NotNull CompoundBinaryTag compound) {
        CompoundBinaryTag scoreCompound = compound.getCompound("score");
        String name = scoreCompound.getString("name");
        String objective = scoreCompound.getString("objective");
        ScoreComponent.Builder builder = Component.score().name(name).objective(objective);

        String value = scoreCompound.getString("value");
        if (!value.isEmpty())
            //noinspection deprecation
            builder.value(value);

        return builder;
    }

    private @NotNull ComponentBuilder<?, ?> deserializeSelectorComponent(@NotNull CompoundBinaryTag compound) {
        String selector = compound.getString("selector");
        SelectorComponent.Builder builder = Component.selector().pattern(selector);

        BinaryTag separator = compound.get("separator");
        if (separator != null) builder.separator(deserializeAnyComponent(separator));

        return builder;
    }

    private @NotNull ComponentBuilder<?, ?> deserializeKeybindComponent(@NotNull CompoundBinaryTag compound) {
        String keybind = compound.getString("keybind");
        return Component.keybind().keybind(keybind);
    }

    private @NotNull ComponentBuilder<?, ?> deserializeNbtComponent(@NotNull CompoundBinaryTag compound) {
        throw new UnsupportedOperationException("NBTComponent is not implemented yet");
    }

    private @NotNull ClickEvent deserializeClickEvent(@NotNull CompoundBinaryTag compound) {
        String actionName = compound.getString("action");
        ClickEvent.Action action = ClickEvent.Action.NAMES.value(actionName);
        String value = compound.getString("value");
        return ClickEvent.clickEvent(action, value);
    }

    private @NotNull HoverEvent<?> deserializeHoverEvent(@NotNull CompoundBinaryTag compound) {
        String actionName = compound.getString("action");
        CompoundBinaryTag contents = compound.getCompound("contents");

        HoverEvent.Action<?> action = HoverEvent.Action.NAMES.value(actionName);
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return HoverEvent.showText(deserializeComponent(contents));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            @Subst("minecraft:stick") String id = contents.getString("id");
            int count = contents.getInt("count");
            String tag = contents.getString("tag");
            BinaryTagHolder binaryTag = tag.isEmpty() ? null : BinaryTagHolder.binaryTagHolder(tag);
            return HoverEvent.showItem(Key.key(id), count, binaryTag);
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            CompoundBinaryTag name = contents.getCompound("name");
            Component nameComponent = name.size() == 0 ? null : deserializeComponent(name);
            @Subst("minecraft:pig") String type = contents.getString("type");
            String id = contents.getString("id");
            return HoverEvent.showEntity(Key.key(type), UUID.fromString(id), nameComponent);
        } else {
            throw new UnsupportedOperationException("Unknown hover event action: " + actionName);
        }
    }

    // SERIALIZATION

    private @NotNull CompoundBinaryTag serializeComponent(@NotNull Component component) {
        CompoundBinaryTag.Builder compound = CompoundBinaryTag.builder();

        // Base component types
        if (component instanceof TextComponent text) {
            compound.putString("type", "text");
            compound.putString("text", text.content());
        } else if (component instanceof TranslatableComponent translatable) {
            compound.putString("type", "translatable");
            compound.putString("translate", translatable.key());
            String fallback = translatable.fallback();
            if (fallback != null) compound.putString("fallback", fallback);
            List<TranslationArgument> args = translatable.arguments();
            if (!args.isEmpty()) compound.put("with", serializeTranslationArgs(args));
        } else if (component instanceof ScoreComponent score) {
            compound.putString("type", "score");
            CompoundBinaryTag.Builder scoreCompound = CompoundBinaryTag.builder();
            scoreCompound.putString("name", score.name());
            scoreCompound.putString("objective", score.objective());
            @SuppressWarnings("deprecation") String value = score.value();
            if (value != null) scoreCompound.putString("value", value);
            compound.put("score", scoreCompound.build());
        } else if (component instanceof SelectorComponent selector) {
            compound.putString("type", "selector");
            compound.putString("selector", selector.pattern());
            Component separator = selector.separator();
            if (separator != null) compound.put("separator", serializeComponent(separator));
        } else if (component instanceof KeybindComponent keybind) {
            compound.putString("type", "keybind");
            compound.putString("keybind", keybind.keybind());
        } else if (component instanceof NBTComponent<?, ?> nbt) {
            //todo
            throw new UnsupportedOperationException("NBTComponent is not implemented yet");
        } else {
            throw new UnsupportedOperationException("Unknown component type: " + component.getClass().getName());
        }

        // Children
        if (!component.children().isEmpty()) {
            ListBinaryTag.Builder<CompoundBinaryTag> children = ListBinaryTag.builder(BinaryTagTypes.COMPOUND);
            for (Component child : component.children())
                children.add(serializeComponent(child));
            compound.put("extra", children.build());
        }

        // Formatting/Interactivity
        compound.put(serializeStyle(component.style()));

        return compound.build();
    }

    @Override
    public @NotNull CompoundBinaryTag serializeStyle(@NotNull Style style) {
        CompoundBinaryTag.Builder compound = CompoundBinaryTag.builder();

        TextColor color = style.color();
        if (color != null) {
            if (color instanceof NamedTextColor named) {
                compound.putString("color", named.toString());
            } else {
                compound.putString("color", color.asHexString());
            }
        }
        Key font = style.font();
        if (font != null)
            compound.putString("font", font.toString());
        TextDecoration.State bold = style.decoration(TextDecoration.BOLD);
        if (bold != TextDecoration.State.NOT_SET)
            compound.putBoolean("bold", bold == TextDecoration.State.TRUE);
        TextDecoration.State italic = style.decoration(TextDecoration.ITALIC);
        if (italic != TextDecoration.State.NOT_SET)
            compound.putBoolean("italic", italic == TextDecoration.State.TRUE);
        TextDecoration.State underlined = style.decoration(TextDecoration.UNDERLINED);
        if (underlined != TextDecoration.State.NOT_SET)
            compound.putBoolean("underlined", underlined == TextDecoration.State.TRUE);
        TextDecoration.State strikethrough = style.decoration(TextDecoration.STRIKETHROUGH);
        if (strikethrough != TextDecoration.State.NOT_SET)
            compound.putBoolean("strikethrough", strikethrough == TextDecoration.State.TRUE);
        TextDecoration.State obfuscated = style.decoration(TextDecoration.OBFUSCATED);
        if (obfuscated != TextDecoration.State.NOT_SET)
            compound.putBoolean("obfuscated", obfuscated == TextDecoration.State.TRUE);

        String insertion = style.insertion();
        if (insertion != null) compound.putString("insertion", insertion);
        ClickEvent clickEvent = style.clickEvent();
        if (clickEvent != null) compound.put("clickEvent", serializeClickEvent(clickEvent));
        HoverEvent<?> hoverEvent = style.hoverEvent();
        if (hoverEvent != null) compound.put("hoverEvent", serializeHoverEvent(hoverEvent));

        return compound.build();
    }

    private @NotNull BinaryTag serializeTranslationArgs(@NotNull Collection<TranslationArgument> args) {
        ListBinaryTag.Builder<CompoundBinaryTag> argList = ListBinaryTag.builder(BinaryTagTypes.COMPOUND);
        for (TranslationArgument arg : args)
            argList.add(serializeComponent(arg.asComponent()));
        return argList.build();
    }

    private @NotNull BinaryTag serializeClickEvent(@NotNull ClickEvent event) {
        return CompoundBinaryTag.builder()
                .putString("action", event.action().toString())
                .putString("value", event.value())
                .build();
    }

    @SuppressWarnings("unchecked")
    private @NotNull BinaryTag serializeHoverEvent(@NotNull HoverEvent<?> event) {
        CompoundBinaryTag.Builder compound = CompoundBinaryTag.builder();

        //todo surely there is a better way to do this?
        compound.putString("action", event.action().toString());
        if (event.action() == HoverEvent.Action.SHOW_TEXT) {
            Component value = ((HoverEvent<Component>) event).value();
            compound.put("contents", serializeComponent(value));
        } else if (event.action() == HoverEvent.Action.SHOW_ITEM) {
            HoverEvent.ShowItem value = ((HoverEvent<HoverEvent.ShowItem>) event).value();

            CompoundBinaryTag.Builder itemCompound = CompoundBinaryTag.builder();
            itemCompound.putString("id", value.item().asString());
            if (value.count() != 1) itemCompound.putInt("count", value.count());
            BinaryTagHolder tag = value.nbt();
            if (tag != null) itemCompound.putString("tag", tag.string());

            compound.put("contents", itemCompound.build());
        } else if (event.action() == HoverEvent.Action.SHOW_ENTITY) {
            HoverEvent.ShowEntity value = ((HoverEvent<HoverEvent.ShowEntity>) event).value();

            CompoundBinaryTag.Builder entityCompound = CompoundBinaryTag.builder();
            Component name = value.name();
            if (name != null) entityCompound.put("name", serializeComponent(name));
            entityCompound.putString("type", value.type().asString());
            entityCompound.putString("id", value.id().toString());

            compound.put("contents", entityCompound.build());
        } else {
            throw new UnsupportedOperationException("Unknown hover event action: " + event.action());
        }

        return compound.build();
    }

}