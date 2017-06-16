package com.github.lazylazuli.lib.common.item.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JsonRecipeFactory
{
	private final String path;
	
	public JsonRecipeFactory(String path)
	{
		this.path = path;
	}
	
	private void write(JsonObject json, String name)
	{
		try (Writer writer = new FileWriter(path + name + ".json"))
		{
			Gson gson = new GsonBuilder().create();
			gson.toJson(json, writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void writePattern(JsonObject json, String... pattern)
	{
		JsonArray array = new JsonArray();
		
		for (String s : pattern)
		{
			array.add(s);
		}
		
		json.add("pattern", array);
		
		JsonObject key = new JsonObject();
		json.add("key", key);
	}
	
	private JsonObject writeStack(ItemStack stack)
	{
		JsonObject object = new JsonObject();
		
		ResourceLocation resourceLocation = stack.getItem()
												 .getRegistryName();
		String s = resourceLocation.getResourceDomain();
		s += ":";
		s += resourceLocation.getResourcePath();
		object.addProperty("item", s);
		object.addProperty("data", stack.getMetadata());
		object.addProperty("count", stack.getCount());
		
		return object;
	}
	
	public void createShapedRecipe(String name, ItemStack result, Object... params)
	{
		JsonObject json = new JsonObject();
		json.addProperty("type", "minecraft:crafting_shaped");
		
		List<String> patternList = new ArrayList<>();
		
		Queue<Object> q = new LinkedList<>(Arrays.asList(params));
		
		JsonObject key = new JsonObject();
		
		while (!q.isEmpty())
		{
			Object o = q.poll();
			
			if (o instanceof String)
			{
				patternList.add((String) o);
			} else if (o instanceof Character)
			{
				char c = (char) o;
				o = q.poll();
				
				ItemStack stack;
				if (o instanceof Block)
				{
					stack = new ItemStack((Block) o, 1, 32767);
				} else if (o instanceof Item)
				{
					stack = new ItemStack((Item) o, 1, 32767);
				} else
				{
					stack = (ItemStack) o;
				}
				
				key.add(Character.toString(c), writeStack(stack));
			}
		}
		
		writePattern(json, patternList.toArray(new String[patternList.size()]));
		json.add("key", key);
		json.add("result", writeStack(result));
		
		write(json, name);
	}
	
	public void createShapelessRecipe(String name, ItemStack result, Object... params)
	{
		JsonObject json = new JsonObject();
		json.addProperty("type", "minecraft:crafting_shapeless");
		
		List<String> patternList = new ArrayList<>();
		
		Queue<Object> q = new LinkedList<>(Arrays.asList(params));
		
		JsonArray ingredients = new JsonArray();
		
		while (!q.isEmpty())
		{
			Object o = q.poll();
			
			ItemStack stack;
			if (o instanceof Block)
			{
				stack = new ItemStack((Block) o, 1, 32767);
			} else if (o instanceof Item)
			{
				stack = new ItemStack((Item) o, 1, 32767);
			} else
			{
				stack = (ItemStack) o;
			}
			
			ingredients.add(writeStack(stack));
		}
		
		writePattern(json, patternList.toArray(new String[patternList.size()]));
		json.add("ingredients", ingredients);
		json.add("result", writeStack(result));
		
		write(json, name);
	}
}
