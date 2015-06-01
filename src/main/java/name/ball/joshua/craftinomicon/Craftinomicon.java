package name.ball.joshua.craftinomicon;

import name.ball.joshua.craftinomicon.di.DI;
import name.ball.joshua.craftinomicon.di.Inject;
import name.ball.joshua.craftinomicon.recipe.MaterialDataSubstitutes;
import name.ball.joshua.craftinomicon.recipe.RecipeBrowser;
import name.ball.joshua.craftinomicon.recipe.RecipeSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Craftinomicon extends JavaPlugin {

    @Inject private MaterialDataSubstitutes materialDataSubstitutes;
    @Inject private RecipeBrowser recipeBrowser;
    @Inject private RecipeSnapshot recipeSnapshot;

    public void onDisable() {
    }

    public void onEnable() {
//        new CraftinomiconTestRunner().runTests();

        DIGetter diGetter = new DIGetter();
        final DI di = diGetter.getDI();
        di.injectMembers(this);

        final PluginManager pm = this.getServer().getPluginManager();

        final ItemStack recipeBookItem = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = recipeBookItem.getItemMeta();
        itemMeta.setDisplayName(RECIPE_BOOK_DISPLAY_NAME);
        recipeBookItem.setItemMeta(itemMeta);

        ShapelessRecipe recipeBookRecipe = new ShapelessRecipe(recipeBookItem);
        recipeBookRecipe.addIngredient(Material.BOOK);
        recipeBookRecipe.addIngredient(Material.WORKBENCH);

        final Server server = Bukkit.getServer();
        server.addRecipe(recipeBookRecipe);

        class RecipeBookCraftingInterceptor implements Listener {
            @EventHandler
            public void convertToRecipeBook(PrepareItemCraftEvent event) {
                Recipe recipe = event.getInventory().getRecipe();
                if (recipe instanceof ShapelessRecipe) {
                    ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                    List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                    if (ingredientList.size() == 2 && ingredientList.get(1).getType().equals(Material.WORKBENCH)) {
                        ItemStack firstIngredient = ingredientList.get(0);
                        if (Material.BOOK.equals(firstIngredient.getType()) && !isRecipeBook(firstIngredient)) {
                            event.getInventory().setResult(recipeBookItem);
                        }
                    }
                }
            }
        }
        pm.registerEvents(new RecipeBookCraftingInterceptor(), this);

        class RecipeBookConsumeEventHandler implements Listener {
            @EventHandler
            public void onConsumeRecipeBook(PlayerInteractEvent event) {
                Action action = event.getAction();
                switch (action) {
                    case RIGHT_CLICK_AIR:
                    case RIGHT_CLICK_BLOCK:
                        ItemStack itemInHand = event.getPlayer().getItemInHand();
                        if (isRecipeBook(itemInHand)) {
                            recipeBrowser.showAllItems(event.getPlayer());
                        }
                }
            }
        }
        pm.registerEvents(new RecipeBookConsumeEventHandler(), this);

        // We don't want to construct the recipe index until after the other plugins have loaded and had a chance
        // to register their recipes.
        new BukkitRunnable() {
            @Override public void run() {
                materialDataSubstitutes.initialize();
                recipeSnapshot.initialize();
                for (Object o : di.getAllKnownInstances()) {
                    if (o instanceof Listener) {
                        pm.registerEvents((Listener)o, Craftinomicon.this);
                    }
                }
            }
        }.runTask(this);
    }

    protected boolean isRecipeBook(ItemStack itemStack) {
        if (!Material.BOOK.equals(itemStack.getType()) || !itemStack.hasItemMeta()) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && RECIPE_BOOK_DISPLAY_NAME.equals(itemMeta.getDisplayName());
    }

    public static final String RECIPE_BOOK_DISPLAY_NAME = "Craftinomicon";

    private class DIGetter {

        DI getDI() {
            Map<Class<?>, DI.Provider<?>> providers = new LinkedHashMap<Class<?>, DI.Provider<?>>();
            providers.put(Plugin.class, new DI.Provider<Plugin>() {
                @Override
                public Plugin get() {
                    return Craftinomicon.this;
                }
            });
            DI.DIVisitor visitor = new DI.DIVisitor() {
                @Override
                public void visitField(DI.DIField diField) {
                }
            };
            return new DI(visitor, providers);
        }

    }

}
