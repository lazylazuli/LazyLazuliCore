package com.github.lazylazuli.lib.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LazyLazuliMod
{
	private final Logger logger = LogManager.getLogger(getId());
	
	public abstract String getId();
	
	public abstract Proxy getProxy();
	
	public Logger getLogger()
	{
		return logger;
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void preInit(FMLPreInitializationEvent event)
	{
		getProxy().preInit(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void init(FMLInitializationEvent event)
	{
		getProxy().init(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void postInit(FMLPostInitializationEvent event)
	{
		getProxy().postInit(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		getProxy().serverAboutToStart(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void serverStarting(FMLServerStartingEvent event)
	{
		getProxy().serverStarting(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void serverStarted(FMLServerStartedEvent event)
	{
		getProxy().serverStarted(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void serverStopping(FMLServerStoppingEvent event)
	{
		getProxy().serverStopping(event);
	}
	
	/**
	 * Override and annotate with {@link Mod.EventHandler @Mod.EventHandler} to use
	 */
	public void serverStopped(FMLServerStoppedEvent event)
	{
		getProxy().serverStopped(event);
	}
}
