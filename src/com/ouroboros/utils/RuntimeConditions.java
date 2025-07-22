package com.ouroboros.utils;

import java.util.Collection;

public class RuntimeConditions
{
	/**
	 * Validates that an integer is positive. 
	 *
	 * @param value Value to validate
	 * 
	 * @return The value given
	 * @throws IllegalArgumentException If the value given is less than 0
	 */
	public static int requirePositive(int value)
	throws IllegalArgumentException
	{
		if (value < 0)
		{
			throw new IllegalArgumentException("Value must be positive");
		}

		return value;
	}

	/**
	 * Validates that an integer is within a range.
	 *
	 * @param value Value to validate
	 * @param min Minimum allowed value
	 * @param max Maximum allowed value
	 *
	 * @return The value given
	 * @throws IllegalArgumentException If the value given is less than min or greater than max
	 * @throws IllegalArgumentException If min is greater than max
	 */
	public static int requireRange(int value, int min, int max) 
	{
		if (min > max)
		{
			throw new IllegalArgumentException("Minimum value must be lower than maximum value");
		}

		if (value < min || value > max)
		{
			throw new IllegalArgumentException("Value must be between " + min + " (inclusive) and " + max + " (inclusive); received " + value);
		}

		return value;
	}

	/**
	 * Validates that an array has at least one element.
	 *
	 * @param <T> Type of array
	 *
	 * @param array Array to validate
	 *
	 * @return The unmodified array given
	 * @throws IllegalArgumentException If the array is empty
	 * @throws NullPointerException If the array is null
	 */
	public static <T> T[] requirePopulated(T[] array) 
	{
		if (array == null)
		{
			throw new NullPointerException("Array must not be null");
		}

		if (array.length == 0)
		{
			throw new IllegalArgumentException("Array must have at least one element");
		}

		return array;
	}

	/**
	 * Validates that a collection has at least one element.
	 *
	 * @param <T> Type contained within the collection
	 * @param <C> Type of collection
	 *
	 * @param collection Collection to validate
	 *
	 * @return The unmodified collection given
	 * @throws IllegalArgumentException If the collection is empty
	 * @throws NullPointerException If the collection is null
	 */
	public static <T, C extends Collection<T>> C requirePopulated(C collection) 
	{
		if (collection == null)
		{
			throw new NullPointerException("Collection must not be null");
		}

		if (collection.isEmpty())
		{
			throw new IllegalArgumentException("Collection must have at least one element");
		}

		return collection;
	}
}
