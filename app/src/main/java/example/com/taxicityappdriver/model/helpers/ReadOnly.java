package example.com.taxicityappdriver.model.helpers;


/**
 * Set just One time the field
 *
 * @param <T> Type of the Field.
 */
public class ReadOnly<T> {

    private boolean isSetted;
    private T field;

    public ReadOnly() {
        isSetted = false;
        field = null;
    }


    public T get() {
        return field;
    }

    public void set(T field) {
        if (isSetted)
            return;
        this.field = field;
        isSetted = true;
    }
}
