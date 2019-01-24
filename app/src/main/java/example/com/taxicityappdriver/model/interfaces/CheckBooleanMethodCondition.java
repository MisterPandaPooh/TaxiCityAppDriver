package example.com.taxicityappdriver.model.interfaces;

/**
 * CallBack Interface checking a boolean Condition
 * @param <T> The Type of the Arg
 */
public interface CheckBooleanMethodCondition<T> {

    boolean isTrue(T obj);
}
