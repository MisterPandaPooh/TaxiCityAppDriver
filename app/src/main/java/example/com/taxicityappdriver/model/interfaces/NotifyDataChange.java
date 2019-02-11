package example.com.taxicityappdriver.model.interfaces;

/**
 * Notify data change CallBack Interface.
 * @param <T> The type of data changed.
 */
public interface NotifyDataChange<T> {
    void OnDataChanged(T obj);

    void onFailure(Exception exception);
}
