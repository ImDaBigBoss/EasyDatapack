package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.exceptions.CustomMapException;
import com.github.imdabigboss.easydatapack.api.managers.MapManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MapManagerImpl implements Listener, MapManager {
    private final EasyDatapack datapack;

    private final Map<Integer, String> savedImages = new HashMap<>();

    public MapManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
        this.loadImages();
    }

    @EventHandler
    public void onMapInitEvent(MapInitializeEvent event) throws CustomMapException {
        if (this.hasImage(event.getMap().getId())) {
            MapView view = event.getMap();
            view.getRenderers().clear();
            try {
                view.addRenderer(new CustomMapRenderer(this.getImage(view.getId())));
            } catch (IOException e) {
                throw new CustomMapException("Failed to load image for map " + view.getId(), e);
            }
            view.setScale(Scale.FARTHEST);
            view.setTrackingPosition(false);
        }
    }

    public void saveImage(Integer id, String url) {
        this.datapack.getConfig().set("ids." + id, url);
        this.datapack.saveConfig();
    }

    private void loadImages() {
        if (this.datapack.getConfig().contains("ids")) {
            this.datapack.getConfig().getConfigurationSection("ids").getKeys(false).forEach(id -> {
                savedImages.put(Integer.parseInt(id), this.datapack.getConfig().getString("ids." + id));
            });
        }
    }

    public boolean hasImage(int id) {
        return savedImages.containsKey(id);
    }

    public String getImage(int id) {
        return savedImages.get(id);
    }

    @Override
    public ItemStack createMap(@NonNull URL url) {
        World world = this.datapack.getServer().getWorld("world");
        if (world == null) {
            return null;
        }

        MapView view = this.datapack.getServer().createMap(world);
        view.getRenderers().clear();

        CustomMapRenderer renderer;
        try {
            renderer = new CustomMapRenderer(url);
        } catch (IOException e) {
            return null;
        }
        view.addRenderer(renderer);

        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setMapView(view);
        map.setItemMeta(meta);

        return map;
    }

    private static class CustomMapRenderer extends MapRenderer {
        private BufferedImage image;
        private boolean done;

        public CustomMapRenderer(URL url) throws IOException {
            this.image = null;
            this.done = false;

            BufferedImage image = ImageIO.read(url);
            image = MapPalette.resizeImage(image);
            this.image = image;
        }

        public CustomMapRenderer(String url) throws IOException {
            this(new URL(url));
        }

        @Override
        public void render(MapView view, MapCanvas canvas, Player player) {
            if (this.done) {
                return;
            }

            canvas.drawImage(0, 0, this.image);
            view.setTrackingPosition(false);
            this.done = true;
        }
    }
}
