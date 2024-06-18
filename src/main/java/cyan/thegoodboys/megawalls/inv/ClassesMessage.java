package cyan.thegoodboys.megawalls.inv;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassesMessage {
    public static final String token = "b41039abcffc65da8a393f91a74eda26";
    private static final String PLUGIN_PATH = "plugins/MegaWalls-3.1-SNAPSHOT.jar"; // 插件在服务器上的路径
    private static final String GITEE_URL = "https://gitee.com/h1a/mega-walls-update/raw/master/MegaWalls-3.1-SNAPSHOT.jar"; // Gitee仓库中插件的URL

    public void onStart() {
        try {
            String currentVersion = MegaWalls.getInstance().getDescription().getVersion();
            String latestVersion = getLatestVersion();

            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                HttpURLConnection conn = (HttpURLConnection) new URL(GITEE_URL).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "token " + token);

                try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                     FileOutputStream out = new FileOutputStream(PLUGIN_PATH)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "插件检测到更新！");
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "正在重启服务器以应用更新...");
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), Bukkit::shutdown, 60L);
            }
        } catch (FileNotFoundException e) {
            System.out.println("无法获取此目标服务器更新！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLatestVersion() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GITEE_URL).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "token " + token);

        try (InputStream in = conn.getInputStream();
             JarInputStream jarIn = new JarInputStream(in)) {
            JarEntry entry;
            while ((entry = jarIn.getNextJarEntry()) != null) {
                if (entry.getName().equals("plugin.yml")) {
                    try (InputStreamReader reader = new InputStreamReader(jarIn, StandardCharsets.UTF_8)) {
                        PluginDescriptionFile desc = new PluginDescriptionFile(reader);
                        return desc.getVersion();
                    } catch (InvalidDescriptionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return null;
    }
}
