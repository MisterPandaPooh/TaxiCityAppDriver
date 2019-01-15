package example.com.taxicityappdriver.entities;

import android.util.Log;

import example.com.taxicityappdriver.model.backend.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

public class test {

    public static BackEnd db = BackEndFactory.getInstance();
    public static String TAG = "testLab";
    public static void insertData(){
        Driver driver = new Driver();
        driver.setFirstName("Netanel");
        driver.setLastName("Cohen Solal");
        driver.setIdNumber(2222);
        driver.setEmail("jeanyves@me.com");

        db.addDriver(driver, new ActionCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "onFailure: ");

            }

            @Override
            public void onProgress(String status, double percent) {
                Log.i(TAG, "onProgress: ");

            }
        });

    }
}
