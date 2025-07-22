package com.ouroboros.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class WeightedTable<T>
{
	private T[] bakedTable = null;
	private Deque<T> table = new ArrayDeque<>();
	private Random random = new Random();

	public WeightedTable<T> add(@Nullable T input, @Range(min = 1, max = Integer.MAX_VALUE) int weight) 
	{
		RuntimeConditions.requireRange(weight, 1, Integer.MAX_VALUE);
		if (bakedTable != null)
		{
			throw new IllegalArgumentException("Cannot add entries to a baked weighted table");
		}

		for (int i = 0; i < weight; i++)
		{
			table.push(input);
		}

		return this;
	}

	@Nullable
	public synchronized T poll()
	{
		if (bakedTable == null)
		{
			throw new IllegalArgumentException("Cannot poll entries from a weighted table before it has been baked");
		}
		
		return this.bakedTable[this.random.nextInt(this.bakedTable.length)];
	}

	@SuppressWarnings("unchecked")
	public WeightedTable<T> bake()
	{
		Object[] data = new Object[this.table.size()];
		for (int i = 0; !table.isEmpty(); i++) // Flush table into array, starting from the head
		{
			data[i] = table.pollLast();
		}

		this.table = null;
		this.bakedTable = (T[]) data;

		return this;
	}
}
