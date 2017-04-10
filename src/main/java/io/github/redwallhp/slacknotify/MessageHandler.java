package io.github.redwallhp.slacknotify;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


public class MessageHandler extends BukkitRunnable {


    private SlackNotify plugin;
    private Queue<SlackMessage> queue;


    public MessageHandler() {
        plugin = SlackNotify.getInstance();
        queue = new ConcurrentLinkedQueue<>();
        this.runTaskTimerAsynchronously(plugin, 60L, 60L);
    }


    public void run() {
        // Avoid rate limiting by sending messages in batches of 3
        // and only calling run() every 3 seconds
        if (queue.size() > 0) {

            // Grab up to five messages from the queue
            int i = 0;
            List<SlackMessage> toSend = new ArrayList<>();
            while (queue.size() > 0 && i < 5) {
                toSend.add(queue.poll());
                i++;
            }

            // Make the request
            try {
                makeRequest(toSend);
            } catch (UnirestException ex) {
                plugin.getLogger().warning("HTTP Error: " + ex.getMessage());
            } catch (IOException ex) {
                plugin.getLogger().warning("Slack Error: " + ex.getMessage());
            }

        }
    }


    private void makeRequest(List<SlackMessage> messages) throws UnirestException, IOException {
        HttpResponse<JsonNode> response = Unirest.post("https://slack.com/api/chat.postMessage")
                .fields(buildRequestFields(messages))
                .asJson();
        if (response.getStatus() != 200 || !response.getBody().getObject().getBoolean("ok")) {
            throw new IOException(response.getBody().getObject().getString("error"));
        }
    }


    private Map<String, Object> buildRequestFields(List<SlackMessage> messages) {
        JSONArray attachments = new JSONArray(messages.stream().map(SlackMessage::build).collect(Collectors.toList()));
        Map<String, Object> fields = new HashMap<>();
        fields.put("token", plugin.getConfig().getString("token"));
        fields.put("channel", plugin.getConfig().getString("channel"));
        fields.put("as_user", true);
        fields.put("attachments", attachments.toString());
        return fields;
    }


    public Queue<SlackMessage> getQueue() {
        return queue;
    }


}
