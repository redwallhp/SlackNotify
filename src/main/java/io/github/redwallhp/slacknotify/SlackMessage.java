package io.github.redwallhp.slacknotify;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * For the purposes of this plugin, a SlackMessage is equivalent to a Slack "Attachment."
 * The plugin will queue SlackMessage objects up and batch them into bursts, sending
 * requests to Slack with up to five of any queued attachments.
 */
public class SlackMessage {


    private JSONArray fields;
    private JSONObject json;


    public SlackMessage() {
        json = new JSONObject();
        fields = new JSONArray();
    }


    /**
     * Text content for the attachment.
     * Use this method for unformatted plaintext.
     */
    public SlackMessage setText(String text) {
        json.put("text", text);
        json.put("fallback", text);
        return this;
    }


    /**
     * Text content for the attachment.
     * If you're using formatting, you must specify a plaintext fallback.
     * @param text text content
     * @param fallback unformatted text content (e.g. for notifications)
     */
    public SlackMessage setText(String text, String fallback) {
        json.put("text", text);
        json.put("fallback", fallback);
        return this;
    }


    /**
     * Optional title for the attachment
     */
    public SlackMessage setTitle(String title) {
        json.put("title", title);
        return this;
    }


    /**
     * Optional title URL for the attachment
     */
    public SlackMessage setTitleLink(String titleLink) {
        json.put("title_link", titleLink);
        return this;
    }


    /**
     * Optional URL for a body image.
     * Will be resized to 500px.
     */
    public SlackMessage setImage(String imageURL) {
        json.put("image_url", imageURL);
        return this;
    }


    /**
     * Optional color bar, as good, warning, danger or a hex value prefixed by a pound sign
     */
    public SlackMessage setColor(String color) {
        json.put("color", color);
        return this;
    }


    /**
     * Set the thumbnail for the message. Must be under 500KB and preferably be 75x75 pixels
     * @param thumbnailURL URL to the thumbnail
     */
    public SlackMessage setThumbnail(String thumbnailURL) {
        json.put("thumb_url", thumbnailURL);
        return this;
    }


    /**
     * Set the author name for this Slack attachment.
     * e.g. the name of the plugin invoking the message.
     * @param author the name
     */
    public SlackMessage setAuthor(String author) {
        json.put("author_name", author);
        return this;
    }


    /**
     * Add some brief text to help contextualize and identify an attachment. Limited to 300 characters.
     * @param text the text to insert at the bottom of the message
     */
    public SlackMessage setFooter(String text) {
        json.put("footer", text);
        return this;
    }


    /**
     * If the attachment relates to something that happened at a specific time, you can specify
     * a timestamp to be included at the bottom of the message.
     * @param epochTime UNIX time stamp
     */
    public SlackMessage setTimestamp(int epochTime) {
        json.put("ts", epochTime);
        return this;
    }


    /**
     * Add a Slack "field" to the card
     * @param title term
     * @param value contents
     * @param small size
     */
    public SlackMessage addField(String title, String value, boolean small) {
        JSONObject field = new JSONObject();
        field.put("title", title);
        field.put("value", value);
        field.put("short", small);
        fields.put(field);
        return this;
    }


    /**
     * Produce the final JSON object to be used in the API call
     */
    public JSONObject build() {
        json.put("mrkdwn_in", Lists.newArrayList("text")); //enable markdown in text
        json.put("fields", fields);
        return json;
    }


}
