package example.com.taxicityappdriver.model.interfaces;

public interface NotifyDataChange<T> {
    void OnDataChanged(T obj);

    //void OnDateAdded(T obj);

    void onFailure(Exception exception);
}
