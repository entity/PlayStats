package mc.play.stats.hytale.util;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ApiConfig {

    public static final BuilderCodec<ApiConfig> CODEC =
            BuilderCodec.builder(ApiConfig.class, ApiConfig::new)
                    .append(new KeyedCodec<String>("SecretKey", Codec.STRING),
                            (config, value, _) -> config.secretKey = value,
                            (config, _) -> config.secretKey)
                    .add()

                    .append(new KeyedCodec<String>("BaseUrl", Codec.STRING),
                            (config, value, _) -> config.baseUrl = value,
                            (config, _) -> config.baseUrl)
                    .add()

                    .build();

    private String secretKey = "";
    private String baseUrl = "";

    public ApiConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
