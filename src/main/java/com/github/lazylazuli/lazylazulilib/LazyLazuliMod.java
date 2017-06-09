package com.github.lazylazuli.lazylazulilib;

import com.github.lazylazuli.lazylazulilib.common.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

public abstract class LazyLazuliMod
{
	public abstract Proxy getProxy();
	
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
