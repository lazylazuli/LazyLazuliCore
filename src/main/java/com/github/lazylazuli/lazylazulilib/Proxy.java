package com.github.lazylazuli.lazylazulilib;

import net.minecraftforge.fml.common.event.*;

public interface Proxy
{
	default void preInit(FMLPreInitializationEvent event) {}
	
	default void init(FMLInitializationEvent event) {}
	
	default void postInit(FMLPostInitializationEvent event) {}
	
	default void serverAboutToStart(FMLServerAboutToStartEvent event) {}
	
	default void serverStarting(FMLServerStartingEvent event) {}
	
	default void serverStarted(FMLServerStartedEvent event) {}
	
	default void serverStopping(FMLServerStoppingEvent event) {}
	
	default void serverStopped(FMLServerStoppedEvent event) {}
}
