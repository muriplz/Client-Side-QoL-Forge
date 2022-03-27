package com.blockog.clientsideqol;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("clientsideqol")
public class ClientSideQoL {
    public static ClientSideQoL instance;

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "clientsideqol";

    private void setup(final FMLCommonSetupEvent event)
    {
        instance=this;
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    public static ClientSideQoL getInstance(){
        return instance;
    }
}
