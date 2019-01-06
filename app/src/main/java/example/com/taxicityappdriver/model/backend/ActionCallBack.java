package example.com.taxicityappdriver.model.backend;

public interface ActionCallBack<T> {

    void onSuccess(T obj);

    void onFailure(Exception exception);

    void onProgress(String status, double percent);

}
