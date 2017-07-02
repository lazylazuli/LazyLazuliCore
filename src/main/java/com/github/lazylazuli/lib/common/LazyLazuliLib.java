package com.github.lazylazuli.lib.common;

import com.github.lazylazuli.lib.common.mod.LazyLazuliMod;
import com.github.lazylazuli.lib.common.mod.Proxy;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = LazyLazuliLib.MODID,
	 version = LazyLazuliLib.VERSION + "-" + LazyLazuliLib.BUILD,
	 acceptedMinecraftVersions = LazyLazuliLib.MCVERSION)
public final class LazyLazuliLib extends LazyLazuliMod
{
	public static final String MODID = "lazylazulilib";
	
	public static final String MCVERSION = "1.12";
	
	public static final String VERSION = "2.0.0";
	public static final String BUILD = "78";
	
	@Mod.Instance(value = LazyLazuliLib.MODID)
	public static LazyLazuliLib instance;
	
	@Mod.InstanceFactory
	public static LazyLazuliLib initializeMod()
	{
		return new LazyLazuliLib();
	}
	
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
