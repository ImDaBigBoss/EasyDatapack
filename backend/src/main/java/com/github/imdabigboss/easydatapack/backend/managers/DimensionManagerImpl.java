package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.dimentions.CustomDimension;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomDimensionException;
import com.github.imdabigboss.easydatapack.api.managers.DimensionManager;
import com.github.imdabigboss.easydatapack.api.utils.BlockFaces;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DimensionManagerImpl implements Listener, DimensionManager {
    private final EasyDatapack datapack;

    private final Map<String, CustomDimension> dimensions = new HashMap<>();

    public DimensionManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
    }

    public void registerCustomDimension(CustomDimension dimension) throws CustomDimensionException {
        for (CustomDimension registeredDimension : dimensions.values()) {
            if (registeredDimension.getName().equals(dimension.getName())) {
                throw new CustomDimensionException("A dimension with the name " + dimension.getName() + " has already been registered!");
            } else if (registeredDimension.getPortalFrameMaterial().equals(dimension.getPortalFrameMaterial())) {
                throw new CustomDimensionException("A dimension with the portal frame material " + dimension.getPortalFrameMaterial() + " has already been registered!");
            }
        }

        dimensions.put(dimension.getName(), dimension);
    }

    public void createWorlds() {
        long seed = 0;
        World world = this.datapack.getServer().getWorld("world");
        if (world != null) {
            seed = world.getSeed();
        }

        for (CustomDimension dimension : dimensions.values()) {
            dimension.createWorld(seed);
        }
    }

    @Override
    public CustomDimension getDimension(String name) {
        return dimensions.get(name);
    }

    @Override
    public List<CustomDimension> getCustomDimensions() {
        return new ArrayList<>(this.dimensions.values());
    }

    public CustomDimension getDimensionFromPortalMaterial(Material material) {
        for (CustomDimension dimension : dimensions.values()) {
            if (dimension.getPortalFrameMaterial().equals(material)) {
                return dimension;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        if (!event.getCause().equals(PlayerPortalEvent.TeleportCause.NETHER_PORTAL)) {
            return;
        }

        Location sourceLocation = event.getFrom();

        Block sourceBlock = sourceLocation.getBlock();
        Block portalBlock = null;

        if (sourceBlock.getType() != Material.NETHER_PORTAL) {
            for (BlockFaces targetFace : BlockFaces.values()) {
                if (targetFace.isSimple()) {
                    Block block = sourceBlock.getRelative(targetFace.getFace());
                    if (block.getType() == Material.NETHER_PORTAL) {
                        portalBlock = block;
                        break;
                    }
                }
            }

            if (portalBlock == null) {
                return;
            }
        } else {
            portalBlock = sourceBlock;
        }

        boolean northSouth;
        if (portalBlock.getBlockData().getAsString().contains("[axis=x]")) {
            northSouth = true;
        } else if (portalBlock.getBlockData().getAsString().contains("[axis=z]")) {
            northSouth = false;
        } else {
            return;
        }

        if (northSouth) { //Get the north/south left side of the portal
            Block tmpBlock = portalBlock.getRelative(BlockFace.WEST);
            if (tmpBlock.getType() == Material.NETHER_PORTAL) {
                portalBlock = tmpBlock;
            }
        } else {
            Block tmpBlock = portalBlock.getRelative(BlockFace.NORTH);
            if (tmpBlock.getType() == Material.NETHER_PORTAL) {
                portalBlock = tmpBlock;
            }
        }

        while (true) { //Get the bottom left block of the portal
            Block tmpBlock = portalBlock.getRelative(BlockFace.DOWN);
            if (tmpBlock.getType() == Material.NETHER_PORTAL) {
                portalBlock = tmpBlock;
            } else {
                break;
            }
        }

        portalBlock = portalBlock.getRelative(BlockFace.DOWN); //Move down and left one more block
        if (northSouth) {
            portalBlock = portalBlock.getRelative(BlockFace.WEST);
        } else {
            portalBlock = portalBlock.getRelative(BlockFace.NORTH);
        }

        Block[][] blocks = {new Block[5], new Block[5], new Block[5], new Block[5]}; //Read all the nether portal blocks
        Block tmpBlock = portalBlock;
        if (northSouth) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 5; y++) {
                    blocks[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                }
                tmpBlock = tmpBlock.getRelative(BlockFace.EAST);
            }
        } else {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 5; y++) {
                    blocks[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                }
                tmpBlock = tmpBlock.getRelative(BlockFace.SOUTH);
            }
        }

        Material portalMaterial = blocks[0][1].getType();
        CustomDimension dimension = this.getDimensionFromPortalMaterial(portalMaterial);
        if (dimension == null) {
            return;
        }

        if (!this.isPortal(blocks, portalMaterial, Material.NETHER_PORTAL)) {
            return;
        }

        event.setCancelled(true);
        if (event.getFrom().getWorld().getName().equals(dimension.getName())) {
            event.getPlayer().teleport(dimension.dimensionToNormal(event.getFrom()));
        } else {
            event.getPlayer().teleport(dimension.normalToDimension(event.getFrom()));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getItem() == null || event.getItem().getType() != Material.FLINT_AND_STEEL) {
                return;
            }
            if (event.getBlockFace() != BlockFace.UP) {
                return;
            }

            Material portalMaterial = event.getClickedBlock().getType();
            CustomDimension dimension = this.getDimensionFromPortalMaterial(portalMaterial);
            if (dimension == null) {
                return;
            }

            Block[][] blocksNorth = {new Block[5], new Block[5], new Block[5], new Block[5]}; //Read all the nether portal blocks
            boolean doNorth = false;
            Block[][] blocksSouth = {new Block[5], new Block[5], new Block[5], new Block[5]}; //Read all the nether portal blocks
            boolean doSouth = false;
            Block[][] blocksEast = {new Block[5], new Block[5], new Block[5], new Block[5]}; //Read all the nether portal blocks
            boolean doEast = false;
            Block[][] blocksWest = {new Block[5], new Block[5], new Block[5], new Block[5]}; //Read all the nether portal blocks
            boolean doWest = false;

            Block blockOnTop = event.getClickedBlock().getRelative(BlockFace.UP);

            if (blockOnTop.getRelative(BlockFace.NORTH).getType() == portalMaterial) {
                doNorth = true;
                Block tmpBlock = event.getClickedBlock().getRelative(BlockFace.NORTH);
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 5; y++) {
                        blocksNorth[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                    }
                    tmpBlock = tmpBlock.getRelative(BlockFace.SOUTH);
                }
            } else if (blockOnTop.getRelative(BlockFace.SOUTH).getType() == portalMaterial) {
                doSouth = true;
                Block tmpBlock = event.getClickedBlock().getRelative(BlockFace.SOUTH);
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 5; y++) {
                        blocksSouth[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                    }
                    tmpBlock = tmpBlock.getRelative(BlockFace.NORTH);
                }
            } else if (blockOnTop.getRelative(BlockFace.EAST).getType() == portalMaterial) {
                doEast = true;
                Block tmpBlock = event.getClickedBlock().getRelative(BlockFace.EAST);
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 5; y++) {
                        blocksEast[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                    }
                    tmpBlock = tmpBlock.getRelative(BlockFace.WEST);
                }
            } else if (blockOnTop.getRelative(BlockFace.WEST).getType() == portalMaterial) {
                doWest = true;
                Block tmpBlock = event.getClickedBlock().getRelative(BlockFace.WEST);
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 5; y++) {
                        blocksWest[x][y] = tmpBlock.getRelative(BlockFace.UP, y);
                    }
                    tmpBlock = tmpBlock.getRelative(BlockFace.EAST);
                }
            }

            if (doNorth && this.isPortal(blocksNorth, portalMaterial, Material.AIR)) {
                event.setCancelled(true);
                this.setPortalInside(blocksNorth, Material.NETHER_PORTAL, true);
            } else if (doSouth && this.isPortal(blocksSouth, portalMaterial, Material.AIR)) {
                event.setCancelled(true);
                this.setPortalInside(blocksSouth, Material.NETHER_PORTAL, true);
            } else if (doEast && this.isPortal(blocksEast, portalMaterial, Material.AIR)) {
                event.setCancelled(true);
                this.setPortalInside(blocksEast, Material.NETHER_PORTAL, false);
            } else if (doWest && this.isPortal(blocksWest, portalMaterial, Material.AIR)) {
                event.setCancelled(true);
                this.setPortalInside(blocksWest, Material.NETHER_PORTAL, false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        Location location = event.getLocation();
        CustomDimension dimension = this.getDimension(location.getWorld().getName());
        if (dimension == null) {
            return;
        }

        if (dimension.spawnEntity(location, event.getEntityType(), event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean isPortal(Block[][] blocks, Material portalMaterial, Material insideMaterial) {
        if (blocks.length != 4) {
            throw new IllegalArgumentException("Array must be 4x5!");
        }
        for (int x = 0; x < 4; x++) {
            if (blocks[x].length != 5) {
                throw new IllegalArgumentException("Array must be 4x5!");
            }
        }

        return blocks[0][1].getType() == portalMaterial &&
                blocks[0][2].getType() == portalMaterial &&
                blocks[0][3].getType() == portalMaterial &&
                blocks[1][0].getType() == portalMaterial &&
                blocks[1][1].getType() == insideMaterial &&
                blocks[1][2].getType() == insideMaterial &&
                blocks[1][3].getType() == insideMaterial &&
                blocks[1][4].getType() == portalMaterial &&
                blocks[2][0].getType() == portalMaterial &&
                blocks[2][1].getType() == insideMaterial &&
                blocks[2][2].getType() == insideMaterial &&
                blocks[2][3].getType() == insideMaterial &&
                blocks[2][4].getType() == portalMaterial &&
                blocks[3][1].getType() == portalMaterial &&
                blocks[3][2].getType() == portalMaterial &&
                blocks[3][3].getType() == portalMaterial;
    }

    private void setPortalInside(Block[][] blocks, Material insideMaterial, boolean pointNorth) {
        if (blocks.length != 4) {
            throw new IllegalArgumentException("Array must be 4x5!");
        }
        for (int x = 0; x < 4; x++) {
            if (blocks[x].length != 5) {
                throw new IllegalArgumentException("Array must be 4x5!");
            }
        }

        BlockData portalData;
        if (pointNorth) {
            portalData = insideMaterial.createBlockData("[axis=z]");
        } else {
            portalData = insideMaterial.createBlockData("[axis=x]");
        }

        blocks[1][1].setBlockData(portalData);
        blocks[1][2].setBlockData(portalData);
        blocks[1][3].setBlockData(portalData);
        blocks[2][1].setBlockData(portalData);
        blocks[2][2].setBlockData(portalData);
        blocks[2][3].setBlockData(portalData);
    }
}
