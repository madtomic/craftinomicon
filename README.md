Craftinomicon
=============

Craftinomicon is a [bukkit](https://bukkit.org/) plugin. It adds a new item to the game of minecraft, the craftinomicon,
which is a cheat sheet for all crafting recipes in the game of minecraft. It is directly inspired by
[NEI](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1279956-chickenbones-mods),
but as a bukkit plugin, it works with vanilla clients.

[Download craftinomicon](https://github.com/sciolizer/craftinomicon/releases/download/v000.001.000/craftinomicon-0.1.jar)

[Bukkit page](http://dev.bukkit.org/bukkit-plugins/craftinomicon/)

Usage
-----

Craft a book and crafting table together to create the craftinomicon.

![Craftinomicon recipe](docs/img/craftinomicon.png "Craftinomicon recipe")

Right click while selecting the craftinomicon
in your hot bar, and the craftinomicon interface will appear.

![Browser screen](docs/img/browser.png "Browser screen")

Click the signs in the lower corners to page forward and backward. Left click an item to see all of its recipes.
For example, left click the piston to see the recipe for making a piston:

![Piston recipe](docs/img/piston.png "Piston recipe")

Click the sign in the upper left corner to go back to the previous screen. You can also click one of the items
in the recipe, to "dig down further". For instance, if you click on one of the planks used to make the piston, you
will see the recipe for making planks. Similarly, you can click on any item in your inventory to see its recipe.

Right click any item to see which recipes it is used in. For instance, bone meal:

![Bone meal usage](docs/img/bonemeal.png "Bone meal usage")

At the bottom are the different items that can be crafted from bone meal.

Furnace recipes are also supported.

![Cactus green recipe](docs/img/cactus-green.png "Cactus green recipe")

The craftinomicon uses the bukkit api to query for all known recipes, so in theory the craftinomicon
should be able to display recipes
from other plugins. This has not been tested, however.

Craftinomicon was developed against the bukkit api for minecraft 1.8.3.

Supported languages
-------------------

Language can be changed from English by changing the language.code property in `$BUKKIT/plugins/craftinomicon/config.yml`.
Currently supported languages are English, Simplified Chinese, and Traditional Chinese.
Add your own translation to the [localization page](http://dev.bukkit.org/bukkit-plugins/craftinomicon/localization/)!

Permissions
-----------

* `craftinomicon.craft.book` Grants the ability to craft the craftinomicon. Default value: true
* `craftinomicon.upgrade.announce` When a new version of craftinomicon is released, users with this permission enabled will see an extra item in their craftinomicon, letting them know that an upgrade is available. If no user has this permission, then the plugin will not attempt to check for updates. Default value: op

Updates
-------

By default, craftinomicon will check for updates every 24 hours. Nothing is downloaded, but ops will
be able to see an extra item in their craftinomicon, letting them know that a new version is available.

![Update sign](docs/img/update.png "Update sign")

Checking for updates can be disabled by setting the `craftinomicon.upgrade.announce` permission to `false` for all users.

Statistics
----------

By default, craftinomicon sends the following anonymous statistics to [mcstats.org](http://mcstats.org/plugin/craftinomicon):

* Exceptions (programming errors) thrown during execution.
* Whether the update checker is enabled or not.
* Version of the craftinomicon plugin.
* Which language the plugin is configured with.

Collecting this information is helpful to me (sciolizer) for knowing which features to focus on.

Additionally, mcstats collects the following anonymous statistics:

* Number of players
* The server's GeoIP
* Version of minecraft
* Version of bukkit/spigot

Statistics visible to me are visible to everyone. You can read more about
[mcstats on their website](http://mcstats.org/learn-more/).

If you do not want craftinomicon to collect stats from your server, change the `opt-out` value in
`$BUKKIT/plugins/PluginMetrics/config.yml` to true.

Building and installing
-----------------------

```
mvn clean package
cp target/*.jar ~/server/plugins
```
