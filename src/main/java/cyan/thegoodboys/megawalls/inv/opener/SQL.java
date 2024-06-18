package cyan.thegoodboys.megawalls.inv.opener;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SQL {

    public static final String token = "b41039abcffc65da8a393f91a74eda26";

    //访问文本信息
    public static String fetchTextFromGitee() throws Exception {
        URL url = new URL("https://gitee.com/h1a/mega-walls-core/raw/master/Core");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "token " + token);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
            content.append(System.lineSeparator());
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }

    //许可证访问链接
    public static List<String> getLicenseFromGitee() {
        List<String> licenseLines = new ArrayList<>();
        try {
            URL url = new URL("https://gitee.com/h1a/mega-walls-license/raw/master/mw"); // 替换为你的 URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "token " + token);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                licenseLines.add(inputLine);
            }
            // 关闭连接
            in.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return licenseLines;
    }

    public static void Register() {
        // 获取许可证
        String expectedLicense = MegaWalls.getInstance().getConfig().getString("code");
        List<String> actualLicenseLines = getLicenseFromGitee();
        // 验证许可证
        boolean licenseMatched = false;
        for (String line : actualLicenseLines) {
            if (line.equals(expectedLicense)) {
                licenseMatched = true;
                break;
            }
        }
        // 如果许可证不正确，返回false
        if (!licenseMatched) {
            MegaWalls.getInstance().getLogger().severe("§c许可证不正确，插件启动失败！请检查您的配置文件！");
            MegaWalls.getInstance().getServer().getPluginManager().disablePlugin(MegaWalls.getInstance());
            Bukkit.shutdown();
        }
    }

    //许可证服务器IP
    /*
    public List<String> getLicenseServerIP() {
        List<String> licenseLines = new ArrayList<>();
        try {
            URL url = new URL("https://gitee.com/h1a/mega-walls-license/raw/master/mw"); // 替换为你的 URL
            String serverIP = url.getHost();

            // 获取本机IP地址
            InetAddress localAddress = InetAddress.getLocalHost();
            String localIP = localAddress.getHostAddress();

            // 如果服务器IP地址与本机IP地址不同，停止服务器
            if (!serverIP.equals(localIP)) {
                System.out.println("服务器IP地址与本机IP地址不同，停止服务器");
                Bukkit.getPluginManager().disablePlugin(MegaWalls.getInstance());
                Bukkit.shutdown();
                return licenseLines;
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "token " + token);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                licenseLines.add(inputLine);
            }
            // 关闭连接
            in.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return licenseLines;
    }

     */
}
