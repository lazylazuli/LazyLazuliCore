package com.github.lazylazuli.lib.common;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = LazyLazuliLib.MODID, version = LazyLazuliLib.VERSION, acceptedMinecraftVersions = LazyLazuliLib.MCVERSION)
public final class LazyLazuliLib extends LazyLazuliMod
{
	public static final String MODID = "lazylazulilib";
	public static final String VERSION = "@version@";
	public static final String MCVERSION = "@mcversion@";
	
	@Mod.Instance
	public static LazyLazuliLib instance;
	
	@Override
	public String getId()
	{
		return MODID;
	}
	
	@Override
	public Proxy getProxy()
	{
		return null;
	}
}
