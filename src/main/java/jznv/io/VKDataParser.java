package jznv.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jznv.data.NamePair;
import jznv.entity.StudentInfo;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Getter
public class VKDataParser {
    private final HashMap<NamePair, StudentInfo> infoMap = new HashMap<>();

    public VKDataParser() throws URISyntaxException, IOException {
        parse();
    }

    public void parse() throws URISyntaxException, IOException {
        String resp_data = getData();
        JsonObject json = new Gson().fromJson(resp_data, JsonObject.class);
        JsonObject response = json.getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");

        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();

            StudentInfo info = new StudentInfo();

            NamePair pair = new NamePair(
                    item.get("first_name").getAsString(),
                    item.get("last_name").getAsString()
            );

            if (item.get("bdate") != null)
                info.setBirthDate(item.get("bdate").getAsString());

            if (item.getAsJsonObject("city") != null)
                info.setCity(item.getAsJsonObject("city").get("title").getAsString());

            if (item.getAsJsonObject("country") != null)
                info.setCountry(item.getAsJsonObject("country").get("title").getAsString());

            if (item.get("sex") != null)
                info.setGender(item.get("sex").getAsInt() == 1 ? "Female" : "Male");

            if (item.get("university_name") != null)
                info.setUniversity_name(item.get("university_name").getAsString());

            infoMap.put(pair, info);
        }
    }

    private String getData() throws IOException, URISyntaxException {
        String urlRaw = String.format(
                "https://api.vk.com/method/groups.getMembers?group_id=%s&fields=%s&access_token=%s&v=5.131",
                Props.cfg.getProperty("vk.groupId"),
                Props.cfg.getProperty("vk.fields"),
                Props.cfg.getProperty("vk.token")
        );
        Scanner scanner = new Scanner((InputStream) new URI(urlRaw).toURL().getContent());
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) builder.append(scanner.nextLine());
        return builder.toString();
    }
}