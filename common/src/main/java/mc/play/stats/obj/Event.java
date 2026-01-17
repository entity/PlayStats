package mc.play.stats.obj;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.StringJoiner;

/**
 * Event class for storing event data and metadata.
 */
public class Event {
    private final String eventType;
    private final LocalDateTime timestamp;
    private final JsonObject metadata;

    /**
     * Constructor for Event class.
     *
     * @param eventType Type of the event (e.g., "player:join").
     */
    public Event(String eventType) {
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now(ZoneOffset.UTC);
        this.metadata = new JsonObject();
    }

    /**
     * Sets metadata for the event.
     *
     * @param key   The metadata key (e.g., "player", "blockType").
     * @param value The metadata value.
     * @return Returns the same Event instance to allow method chaining.
     */
    public Event setMetadata(String key, Object value) {
        String[] keys = key.split("\\.");
        JsonObject currentObject = this.metadata;
        for (int i = 0; i < keys.length - 1; i++) {
            String subKey = keys[i];
            if (!currentObject.has(subKey)) {
                currentObject.add(subKey, new JsonObject());
            }
            currentObject = currentObject.getAsJsonObject(subKey);
        }
        JsonElement element = switch (value) {
            case Number number -> new JsonPrimitive(number);
            case Boolean b -> new JsonPrimitive(b);
            case Character c -> new JsonPrimitive(c);
            case null, default -> new JsonPrimitive(value.toString());
        };
        currentObject.add(keys[keys.length - 1], element);
        return this;
    }

    /**
     * Gets the event type.
     *
     * @return The event type.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets all the metadata associated with this event.
     *
     * @return A map of all metadata key-value pairs.
     */
    public JsonObject getMetadata() {
        return metadata;
    }

    /**
     * Gets the timestamp of the event.
     *
     * @return The timestamp of the event.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get a specific metadata value.
     *
     * @param key The metadata key.
     *
     * @return The metadata value.
     */
    public JsonElement getMetadata(String key) {
        String[] keys = key.split("\\.");
        JsonObject currentObject = this.metadata;
        for (int i = 0; i < keys.length - 1; i++) {
            String subKey = keys[i];
            if (!currentObject.has(subKey)) {
                return null;
            }
            currentObject = currentObject.getAsJsonObject(subKey);
        }
        return currentObject.get(keys[keys.length - 1]);
    }

    /**c
     * Converts the event details to a string for logging or display.
     *
     * @return A string representation of the event.
     */
    @Override
    public String toString() {
        StringJoiner metadataJoiner = new StringJoiner(", ");
        metadata.entrySet().forEach(entry -> metadataJoiner.add(entry.getKey() + "=" + entry.getValue()));
        return "Event[type=" + eventType + ", timestamp=" + timestamp + ", metadata={" + metadataJoiner + "}]";
    }
}