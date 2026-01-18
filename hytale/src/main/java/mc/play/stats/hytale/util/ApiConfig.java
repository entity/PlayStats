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

                    .append(new KeyedCodec<Boolean>("Debug", Codec.BOOLEAN),
                            (config, value, _) -> config.debug = value,
                            (config, _) -> config.debug)
                    .add()

                    .build();

    private String secretKey = "";
    private String baseUrl = "";
    private boolean debug = false;

    public ApiConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isDebug() {
        return debug;
    }
}
