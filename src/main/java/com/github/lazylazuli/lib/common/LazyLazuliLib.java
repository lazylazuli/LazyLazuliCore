package com.github.lazylazuli.lib.common;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = LazyLazuliLib.MODID, version = LazyLazuliLib.VERSION + "-" + LazyLazuliLib.BUILD,
		acceptedMinecraftVersions = LazyLazuliLib.MCVERSION)
public final class LazyLazuliLib extends LazyLazuliMod
{
	public static final String MODID = "lazylazulilib";
	public static final String VERSION = "2.0.0";
	public static final String BUILD = "58";
	public static final String MCVERSION = "1.12";
	
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
