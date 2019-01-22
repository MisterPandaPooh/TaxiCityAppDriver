package example.com.taxicityappdriver.model.helpers;

public class ReadOnly<T> {

    private boolean isSetted;
    private T field;

    public ReadOnly(){
        isSetted = false;
        field = null;
    }


    public T get() {
        return field;
    }

    public void set(T field) {
        if(isSetted)
            return;
        this.field = field;
        isSetted = true;
    }
}
