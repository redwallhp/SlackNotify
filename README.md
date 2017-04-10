SlackNotify
===========

SlackNotify is a Bukkit plugin that exposes a simple and powerful API for other plugins to use for Slack notifications. SlackNotify does all of the heavy lifting, allowing your plugin to simply use it as a dependency and push messages to Slack when something interesting happens.

The plugin uses a queue system that batches messages into bursts so as to not run afoul of the Slack API's rate limiting.


Usage
-----

SlackNotify exposes two static methods on its class that you can call from your plugin. Once you have added the dependecy, you can invoke them whenever you want.

```java
// Simple plain text message
SlackNotify.send("Hello World!");

// And now for something completely different
SlackNotify.send(new SlackMessage()
    .setText("\*Fancy\* \_formatted\_ text!", "Plain text for notifications.")
    .setColor("good")
    .setImage("http://i.imgur.com/H2XMMir.jpg")
    .addField("Key", "Value", true)
    .addField("Something", "Else", true)
);
```

![Sceenshot of Slack](http://i.imgur.com/MHWojAR.png)

If you're familiar with the Slack API, you may note that these messages [are actually "attachments"](https://api.slack.com/docs/message-attachments) in Slack parlance instead of fully separate messages. This design decision was made both for simplicity and for more bang for your rate limit buck. By bundling multiple attachments into one message request, you can send more messages in a short span of time.

The SlackMessage class is a Builder that offers a clean way to quickly build attachments to be pushed in batches to Slack. To get an idea of what formatting options are at your disposal, see the [relevant Slack developer page.](https://api.slack.com/docs/message-attachments)


Installation
------------

Are you a user installing SlackNotify with a plugin that depends on it? Installation is simple:

1. Drop the jar into your plugins/ directory.
2. Restart the server
3. Edit SlackNotify's config.yml file and set your API token and desired channel.
4. Run `/slacknotify reload` to reload the configuration.

You get your API token by first [creating a bot user](https://my.slack.com/services/new/bot) under your organization. The token should be listed on the new bot user's page, where you will be given the option to set its name and avatar.

Next, invite your bot user to the channel you wish it to broadcast notifications to you, and set the channel name (e.g. `#mychannel`) in config.yml. Note that the channel name *must* be surrounded by quotation marks (e.g. `channel: "#mychannel"`) or it will not work.


Adding the Dependency to Your Plugin
------------------------------------

Before you can use SlackNotify in your plugin, you must first add it as a dependency. You'll first want to check out the source and build/install with `mvn install`, so you have the files in your local Maven repository. (Alternatively, you can use [JitPack](https://jitpack.io/#redwallhp/SlackNotify).)

Next, add it as a dependency in your pom.xml, right next to Bukkit:

```
<dependencies>
    <dependency>
        <groupId>org.bukkit</groupId>
        <artifactId>bukkit</artifactId>
        <version>1.11-R0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>io.github.redwallhp</groupId>
        <artifactId>SlackNotify</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

Now you have to update your plugin.yml to add a `depend` or `softdepend` line so Spigot ensures SlackNotify loads first.
 
```
depend: ["SlackNotify"]
```

For more information on plugin dependencies, here are some useful resources:

* [Writing Spigot Plugins with Dependencies](http://rdwl.xyz/blog/spigot-plugin-dependencies/)
* [Bukkit Plugin YAML Guide](http://bukkit.gamepedia.com/Plugin_YAML)