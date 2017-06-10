package com.github.lazylazuli.lazylazulilib.common;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = LazyLazuliLib.MODID, version = LazyLazuliLib.VERSION, acceptedMinecraftVersions = LazyLazuliLib.MCVERSION)
public final class LazyLazuliLib
{
	public static final String MODID = "lazylazulilib";
	public static final String VERSION = "@version@";
	public static final String MCVERSION = "@mcversion@";
	
	@Mod.Instance
	public static LazyLazuliLib instance;
}
