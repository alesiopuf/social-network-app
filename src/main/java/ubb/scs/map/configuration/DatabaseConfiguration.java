package ubb.scs.map.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class DatabaseConfiguration {
    private String url;
    private String user;
    private String password;

    public DatabaseConfiguration() {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.yaml")) {
            Map<String, Object> obj = yaml.load(in);
            Map<String, String> database = (Map<String, String>) obj.get("database");
            this.url = database.get("url");
            this.user = database.get("user");
            this.password = database.get("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters
    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}