package mc.play.stats.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.HttpResponse;
import com.intellectualsites.http.external.GsonMapper;
import mc.play.stats.adapter.LocalDateTimeTypeAdapter;
import mc.play.stats.obj.Event;
import mc.play.stats.obj.Leaderboard;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SDK {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private final HttpClient HTTP_CLIENT;
    private final int API_VERSION = 1;
//    private final String API_URL = String.format("https://dev.play.mc/api/v%d", API_VERSION);
    private final String API_URL = String.format("http://playmc.test/api/v%d", API_VERSION);
    private final String secretToken;

    public SDK(String secretToken) {
        this.secretToken = secretToken;
        final EntityMapper mapper = EntityMapper.newInstance()
                .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, GSON))
                .registerDeserializer(JsonObject.class, GsonMapper.deserializer(JsonObject.class, GSON));

        this.HTTP_CLIENT = HttpClient.newBuilder()
                .withBaseURL(API_URL)
                .withEntityMapper(mapper)
                .build();
    }

    public CompletableFuture<Boolean> sendEvents(List<Event> events) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpResponse response = this.HTTP_CLIENT.get("/events")
                    .withHeader("User-Agent", "PlayerStats")
                    .withHeader("Content-Type", "application/json")
                    .withInput(() -> GSON.toJson(events))
                    .onStatus(200, req -> {})
                    .onRemaining(req -> {
                        if(req.getStatusCode() != 200) {
                            throw new CompletionException(new IOException("Unexpected status code (" + req.getStatusCode() + ")"));
                        }
                    })
                    .execute();

            if(response == null) {
                throw new CompletionException(new IOException("Failed to send events"));
            }

            JsonObject body = response.getResponseEntity(JsonObject.class);
            return body.get("success").getAsBoolean();
        });
    }

    public CompletableFuture<Leaderboard> getLeaderboards(String leaderboard) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpResponse response = this.HTTP_CLIENT.get("/leaderboards")
                    .withHeader("User-Agent", "PlayerStats")
                    .withHeader("Content-Type", "application/json")
                    .onStatus(200, req -> {})
                    .onRemaining(req -> {
                        if(req.getStatusCode() != 200) {
                            throw new CompletionException(new IOException("Unexpected status code (" + req.getStatusCode() + ")"));
                        }
                    })
                    .execute();

            if(response == null) {
                throw new CompletionException(new IOException("Failed to get leaderboards"));
            }

            JsonObject body = response.getResponseEntity(JsonObject.class);
            JsonObject stats = body.get("stats").getAsJsonObject();
            return GSON.fromJson(stats, Leaderboard.class);
        });
    }

    public static Gson getGson() {
        return GSON;
    }
}
