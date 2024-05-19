package util;

/**
 * Represents a pair of objects.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the value.
 */
public record Pair<K, V>(K key, V value) {
}