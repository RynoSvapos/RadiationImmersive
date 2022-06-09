package rynodevelops.radiationimmersive;


import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.apache.logging.log4j.LogManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


import java.util.*;

public class Main extends JavaPlugin implements Listener {

    public BukkitTask startChecker;
    public BukkitTask startAmbiance;
    public BukkitTask randomAmbiance;

    public static Main istance;

    private static final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Il plugin si e' avviato!");
        this.getServer().getPluginManager().registerEvents(this, this);

        LogAppender appender = new LogAppender();
        logger.addAppender(appender);

        if (Main.istance == null) {
            Main.istance = this;
        }

        startTimers();


    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Il plugin si e' stoppato!");
    }


    public WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    public BlockVector3 convertToBlockVector(Location location) {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Converting to blockvectors...");
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }


    public void startTimers() {
        startChecker = new BukkitRunnable() {

            @Override
            public void run() {
                loopParticles();
            }
        }.runTaskTimerAsynchronously(this, 0L, 10L);

        startAmbiance = new BukkitRunnable() {

            @Override
            public void run() {
                loopAmbiance();
            }
        }.runTaskTimerAsynchronously(this, 0L, 840L);

        randomAmbiance = new BukkitRunnable() {

            @Override
            public void run() {
                loopThing();
            }
        }.runTaskTimerAsynchronously(this, 0L, 400L);

    }


    public void loopAmbiance() {
        for(Player player : getServer().getOnlinePlayers()) {
            if (((player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON)) ||
                    (player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON) && player.hasPotionEffect(PotionEffectType.HARM))) &&
                            player.getLocation().getBlock().getLightFromSky() > 1)
            {
                player.playSound(player.getLocation(), Sound.AMBIENT_BASALT_DELTAS_LOOP, 500, 1);
            }
        }
    }

    public void loopParticles() {

        for(Player player : getServer().getOnlinePlayers()) {
            if (((player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON)) ||
                    (player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON) && player.hasPotionEffect(PotionEffectType.HARM))) &&
                    player.getLocation().getBlock().getLightFromSky() > 1)
            {
                Location location = player.getLocation();
                double value;
                if (location.getY() < 63) {
                    value = 80;
                } else {
                    value = location.getY();
                }

                player.getLocation().getWorld().spawnParticle(Particle.WHITE_ASH, location.getX(), value, location.getZ(), 10000, 30, 8, 30, 1);
            }
        }
    }

    public void loopThing() {
        for(Player player : getServer().getOnlinePlayers()) {
            if ((player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON)) ||
                    (player.hasPotionEffect(PotionEffectType.WITHER) && player.hasPotionEffect(PotionEffectType.POISON) && player.hasPotionEffect(PotionEffectType.HARM)))
            {
                player.playSound(player.getLocation(), Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, 500, 1);
            }
        }
    }



    public void wastelandGenerator(Location loc) {
        int radius = 860;


        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Generating wasteland!");

        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getLocation().distance(loc) <= 1000) {
                player.playSound(player.getLocation(), Sound.MUSIC_NETHER_NETHER_WASTES, 1000, 1);
            }
        }


        Location ref_corner_1 = new Location(loc.getWorld(), loc.getX() - radius, loc.getY() - 100, loc.getZ() - radius);
        Location ref_corner_2 = new Location(loc.getWorld(), loc.getX() + radius, loc.getY() + 100, loc.getZ() + radius);

        Location ref_center = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());


        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Corner RAW 1 pos: " + ref_corner_1.getX() + " " + ref_corner_1.getY() + " " + ref_corner_1.getZ() + " | Corner RAW 2 pos: " + ref_corner_2.getX() + " " + ref_corner_2.getY() + " " + ref_corner_2.getZ() );

        BlockVector3 corner1 = convertToBlockVector(ref_corner_1);
        BlockVector3 corner2 = convertToBlockVector(ref_corner_2);

        BlockVector3 centerVector = convertToBlockVector(ref_center);


        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Corner 1 pos: " + corner1.getX() + " " + corner1.getY() + " " + corner1.getZ() + " | Corner 2 pos: " + corner2.getX() + " " + corner2.getY() + " " + corner2.getZ() );

        BukkitWorld world = new BukkitWorld(loc.getWorld());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Generating for world: " + world.getName());


        SphereRegionFactory sphereRegionFactory = new SphereRegionFactory();

        Region region = sphereRegionFactory.createCenteredAt(centerVector, radius);
        //CuboidRegion region = new CuboidRegion(world, corner1, corner2);



        EditSession session = new EditSessionBuilder(world).allowedRegionsEverywhere().limitUnlimited().fastmode(false).build() ;

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Session builded! - Starting to replace blocks..");


        Set<BaseBlock> blocks_to_coarse = new HashSet<BaseBlock>();
        blocks_to_coarse.add(BlockTypes.GRASS_BLOCK.getDefaultState().toBaseBlock());
        blocks_to_coarse.add(BlockTypes.FARMLAND.getDefaultState().toBaseBlock());

        Set<BaseBlock> blocks_to_air = new HashSet<BaseBlock>();
        blocks_to_air.add(BlockTypes.OAK_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.DARK_OAK_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.SPRUCE_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.BIRCH_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.JUNGLE_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.ACACIA_LEAVES.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.VINE.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.SNOW.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.WATER.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.PLAYER_HEAD.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.PLAYER_WALL_HEAD.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.SEAGRASS.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.TALL_SEAGRASS.getDefaultState().toBaseBlock());
        blocks_to_air.add(BlockTypes.LILY_PAD.getDefaultState().toBaseBlock());



        Set<BaseBlock> blocks_to_dead_bush = new HashSet<BaseBlock>();
        blocks_to_dead_bush.add(BlockTypes.FERN.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.DANDELION.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.POPPY.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.BLUE_ORCHID.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.ALLIUM.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.AZURE_BLUET.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.RED_TULIP.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.ORANGE_TULIP.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.WHITE_TULIP.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.PINK_TULIP.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.OXEYE_DAISY.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.CORNFLOWER.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.LILY_OF_THE_VALLEY.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.SUNFLOWER.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.LILAC.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.ROSE_BUSH.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.PEONY.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.LARGE_FERN.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.WHEAT.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.POTATOES.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.CARROTS.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.BEETROOTS.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.PUMPKIN_STEM.getDefaultState().toBaseBlock());
        blocks_to_dead_bush.add(BlockTypes.MELON_STEM.getDefaultState().toBaseBlock());




        // CONVERT BLOCKS TO COARSE DIRT
        session.replaceBlocks(region, blocks_to_coarse, BlockTypes.COARSE_DIRT.getDefaultState().toBaseBlock());

        // CONVERT BLOCKS TO AIR
        session.replaceBlocks(region, blocks_to_air, BlockTypes.AIR.getDefaultState().toBaseBlock());


        // CONVERT BLOCKS TO DEAD BUSHES
        session.replaceBlocks(region, blocks_to_dead_bush, BlockTypes.DEAD_BUSH.getDefaultState().toBaseBlock());




    }

    public void particleExplosion(double x, double y, double z, String world) {

        Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z);
        Location loc2 = new Location(Bukkit.getServer().getWorld(world), x, y, z);
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);

        loc2.setX(x);
        loc2.setY(255);
        loc2.setZ(z);


        //loc.getWorld().createExplosion(loc, 10F, true, true);
        Bukkit.getServer().getWorld(world).spawnParticle(Particle.FLASH, loc.getX(),
                loc.getY(),
                loc.getZ(),
                100, 0, 0, 0,
                1);


        Bukkit.getServer().getWorld(world).spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, x,
                y,
                z,
                20000, 0, 0, 0,
                1);


        for (Entity e : loc.getWorld().getEntities()) {
            if (e.getLocation().distance(loc) <= 250 && e.getLocation().getBlock().getLightFromSky() > 0.1) {
                e.setFireTicks(1200);
            }
        }


        // CREATE CIRCULAR EXPLOSION
        new BukkitRunnable() {

            public void run() {

                Location expSphere = loc;
                int radiusSphere = 20;

                int xP = expSphere.getBlockX();
                int yP = expSphere.getBlockY() + 15;
                int zP = expSphere.getBlockZ();

                for(int xE = xP - radiusSphere; xE <= xP + radiusSphere; xE++) {
                    for(int yE = yP - radiusSphere; yE <= yP + radiusSphere; yE++) {
                        for(int zE = zP - radiusSphere; zE <= zP + radiusSphere; zE++) {

                            if ((xP - xE) * (xP - xE) + (yP - yE) * (yP - yE) + (zP - zE) * (zP - zE) <= radiusSphere*radiusSphere) {


                                loc.getBlock().setType(Material.AIR);

                            }

                        }
                    }
                }


            }

        }.runTask(istance);

        //BOMB SURFACE HOT AIR RING

        new BukkitRunnable() {
            Location exp = loc;
            int radius = 0;

            public void run() {
                for (double alpha = 0; alpha <= 2 * Math.PI; alpha = alpha + Math.PI / 32) {
                    double x = radius * Math.cos(alpha);
                    double y = 5;
                    double z = radius * Math.sin(alpha);
                    exp.add(x, y, z);

                    if (exp.getBlock().getType() != Material.AIR) {
                        loc.getWorld().createExplosion(exp, 8F, true, true);
                    }

                    Bukkit.getServer().getWorld(world).spawnParticle(Particle.EXPLOSION_HUGE, exp.getX(), exp.getY(), exp.getZ(), 1);


                    for (Entity e : loc.getChunk().getEntities()) {
                        if (e.getLocation().distance(exp) <= 16.0) {
                            e.setVelocity(e.getLocation().getDirection().multiply(-100));
                            if (e instanceof LivingEntity) {
                                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));
                                ((LivingEntity) e).damage(16);
                            }
                        }
                    }

                    exp.subtract(x, y, z);
                }
                loc.getWorld().strikeLightningEffect(loc2);
                radius += 1;
                if (radius > 210) {
                    this.cancel();
                }
            }


        }.runTaskTimer(this, 0, 1);




        //! HOT AIR

        new BukkitRunnable() {
            Location exp = loc;
            int radius = 0;

            public void run() {
                for (double alpha = 0; alpha <= 2 * Math.PI; alpha = alpha + Math.PI / 32) {
                    double x = 8 * Math.cos(alpha);
                    double y = radius * 0.5 + 1;
                    double z = 8 * Math.sin(alpha);
                    exp.add(x, y, z);

                    if (exp.getBlock().getType() != Material.AIR) {
                        loc.getWorld().createExplosion(exp, 8F, true, true);
                    }

                    Bukkit.getServer().getWorld(world).spawnParticle(Particle.EXPLOSION_HUGE, exp.getX(), exp.getY(), exp.getZ(), 1);
                    exp.subtract(x, y, z);
                }
                loc.getWorld().strikeLightningEffect(loc2);
                radius += 1;
                if (radius > 100) {
                    this.cancel();
                }
            }


        }.runTaskTimer(this, 0, 1);


        //! HOT AIR

        new BukkitRunnable() {
            Location exp = loc;
            int radius = 0;

            public void run() {
                for (double alpha = 0; alpha <= 2 * Math.PI; alpha = alpha + Math.PI / 36) {
                    double x = radius * Math.cos(alpha);
                    double y = 1 + (radius*0.1);
                    double z = radius * Math.sin(alpha);
                    exp.add(x, y, z);

                    Bukkit.getServer().getWorld(world).spawnParticle(Particle.EXPLOSION_HUGE, exp.getX(), exp.getY(), exp.getZ(), 1);

                    if (exp.getBlock().getType() != Material.AIR) {
                        loc.getWorld().createExplosion(exp, 8F, true, true);
                    }

                    for (Entity e : loc.getChunk().getEntities()) {
                        if (e.getLocation().distance(exp) <= 16.0) {
                            e.setVelocity(e.getLocation().getDirection().multiply(-100));
                            if (e instanceof LivingEntity) {
                                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));
                                ((LivingEntity) e).damage(16);
                            }
                        }
                    }

                    exp.subtract(x, y, z);
                }
                radius += 1;
                if (radius > 210) {
                    this.cancel();
                }
            }


        }.runTaskTimer(this, 40, 1);


        // VERY HOT AIR RISING

        new BukkitRunnable() {
            Location exp = loc;
            int radius = 0;

            public void run() {
                for (double alpha = 0; alpha <= 2 * Math.PI; alpha = alpha + Math.PI / 26) {
                    double x = radius * Math.cos(alpha);
                    double y = -8 + (radius * 0.5);
                    double z = radius * Math.sin(alpha);

                    exp.add(x, y, z);


                    loc.getWorld().createExplosion(exp, 8F, true, true);


                    exp.subtract(x, y, z);
                }
                radius += 1;
                if (radius > 150) {
                    this.cancel();
                }
            }


        }.runTaskTimer(this, 40, 1);





        //KILL VEGETATION

        wastelandGenerator(loc);







    }


}
