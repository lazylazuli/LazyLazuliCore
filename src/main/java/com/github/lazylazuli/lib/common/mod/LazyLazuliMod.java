package com.github.lazylazuli.lib.common.mod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LazyLazuliMod
{
	private final Logger log = LogManager.getLogger(getId());
	
	public LazyLazuliMod()
	{
		log.info("Creating instance");
	}
	
	public abstract String getId();
	
	public abstract Proxy getProxy();
	
	public Logger getLogger()
	{
		return log;
	}
	
	//
	
	/**
	 * Use method annotated with {@link net.minecraftforge.fml.common.Mod.InstanceFactory @Mod.InstanceFactory} to
	 * register to {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS MinecraftForge.EVENT_BUS}.
	 */
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{
		getProxy().registerBlocks(event);
	}
	
	/**
	 * Use method annotated with {@link net.minecraftforge.fml.common.Mod.InstanceFactory @Mod.InstanceFactory} to
	 * register to {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS MinecraftForge.EVENT_BUS}.
	 */
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		getProxy().registerItems(event);
	}
	
	//
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void preInit(FMLPreInitializationEvent event)
	{
		getProxy().preInit(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void init(FMLInitializationEvent event)
	{
		getProxy().init(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void postInit(FMLPostInitializationEvent event)
	{
		getProxy().postInit(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		getProxy().serverAboutToStart(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void serverStarting(FMLServerStartingEvent event)
	{
		getProxy().serverStarting(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void serverStarted(FMLServerStartedEvent event)
	{
		getProxy().serverStarted(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void serverStopping(FMLServerStoppingEvent event)
	{
		getProxy().serverStopping(event);
	}
	
	/**
	 * Override this method and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler @Mod.EventHandler}
	 */
	public void serverStopped(FMLServerStoppedEvent event)
	{
		getProxy().serverStopped(event);
	}
}
