package example.com.taxicityappdriver.model.interfaces;

public interface ActionCallBack<T> {

    void onSuccess(T obj);

    void onFailure(Exception exception);

    void onProgress(String status, double percent);

}
