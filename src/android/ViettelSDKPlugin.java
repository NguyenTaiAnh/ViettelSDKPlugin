package cordova.nta.viettel.sdk.plugin;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.viettel.biometrics.signature.helpers.GoSignSDK;
import com.viettel.biometrics.signature.helpers.GoSignSDKSetup;
import com.viettel.biometrics.signature.listener.ServiceApiListener;
import com.viettel.biometrics.signature.listener.ServiceApiListenerEmpty;
import com.viettel.biometrics.signature.network.request.PendingAuthorisationRequest;
import com.viettel.biometrics.signature.network.response.CertificateResponse;
import com.viettel.biometrics.signature.network.response.ResponseError;
import com.viettel.biometrics.signature.ultils.BiometricApiType;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class ViettelSDKPlugin extends CordovaPlugin {

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    Log.d("actopn kiemtra: ", action);
    if (action.equals("coolMethod")) {
      String message = args.getString(0);
      this.coolMethod(message, callbackContext);
      return true;
    } else if (action.equals("getDeviceId")) {
      this.getDeviceId(callbackContext);
      return true;
    } else if (action.equals("registerDevice")) {
      String message = args.getString(0);
      Log.d("test kiemtra:", args.getString(0));
      cordova.getThreadPool().execute(() -> registerDevice(message, callbackContext));
      return true;
    }else if (action.equals("authorisationPendingRequest")) {
      String request = args.getString(0);
      String access_token = args.getString(1);
      String transactionID = args.getString(2);
      Log.d("test kiemtra request:", args.getString(0));
      Log.d("test kiemtra access_token: ", args.getString(1));
      Log.d("test kiemtra: transactionID", args.getString(2));
      cordova.getThreadPool().execute(() -> authorisationPendingRequest(request, access_token, transactionID, callbackContext));
      return true;
    }
    return false;
  }
  private void coolMethod(String message, CallbackContext callbackContext) {
    if (message != null && message.length() > 0) {
      callbackContext.success(message);
    } else {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }
  private void getDeviceId(CallbackContext callbackContext) {
    System.out.println("====================================GETDEVICEID=================================");
    try {
      GoSignSDKSetup.initialize(cordova.getActivity().getApplication(), "https://remotesigning.viettel.vn:8773", null);
      String deviceId = GoSignSDK.get().getDeviceId();
      System.out.println("test: kiemtra device " +  deviceId);
      callbackContext.success(deviceId);
    }catch (Exception e){
      System.out.println("test: kiemtra loi " +  e.getMessage());
      callbackContext.error(e.getMessage());
    }
  }

  private void registerDevice(String message, CallbackContext callbackContext) {
    if (message != null && message.length() > 0) {
      try {
        cordova.getActivity().runOnUiThread(() -> {
          try {
            GoSignSDKSetup.initialize(cordova.getActivity().getApplication(), "https://remotesigning.viettel.vn:8773", null);
            String token = message;

            Log.d("kiemtra qua initial:", "done");
            GoSignSDK.get().registerDevice(cordova.getActivity(), token, BiometricApiType.AUTO, new ServiceApiListener<CertificateResponse>() {
              @Override
              public void onSuccess(CertificateResponse certificateResponse) {
                Log.d("registerDevice", "Success");
                cordova.getActivity().runOnUiThread(() -> callbackContext.success("Register device success"));
              }

              @Override
              public void onFail(ResponseError responseError) {
                Log.d("registerDevice", "onFail");
                Log.d("Error Message:", responseError.getErrorMessage());
                cordova.getActivity().runOnUiThread(() -> callbackContext.error("Register device failed: " + responseError.getErrorMessage()));
              }

              @Override
              public void showLoading() {
                Log.d("registerDevice", "showLoading");
              }

              @Override
              public void hideLoading() {
                Log.d("registerDevice", "hideLoading");
              }
            });
          } catch (Exception e) {
            Log.d("Kiemtra bug: ", e.getMessage());
            cordova.getActivity().runOnUiThread(() -> callbackContext.error("Register device failed: " + e.getMessage()));
          }
        });
      } catch (Exception e) {
        Log.d("Kiemtra bug: tétttt ", e.getMessage());
        callbackContext.error("Register device failed: " + e.getMessage());
      }
    } else {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }
  private void authorisationPendingRequest(String request, String access_token, String transactionID , CallbackContext callbackContext) {
    Log.d("kiemtra request:", request);
    Log.d("kiemtra access_token:", access_token);
    Log.d("kiemtra transactionID:", transactionID);
    System.out.println("kiemtra check true false:"+  request != null && request.length() > 0 &&  access_token != null && access_token.length() > 0 && transactionID != null && transactionID.length() > 0);

    if (request != null && request.length() > 0 &&  access_token != null && access_token.length() > 0 && transactionID != null && transactionID.length() > 0) {
      Log.d("kiemtra :", "voooô");

      try {
        cordova.getActivity().runOnUiThread(() -> {
          try {

            PendingAuthorisationRequest pendingAuthorisationRequest = new PendingAuthorisationRequest(transactionID,
              request, "SHA256");


            GoSignSDK.get().authorisationPendingRequest(cordova.getActivity(), access_token, pendingAuthorisationRequest, BiometricApiType.AUTO, new ServiceApiListenerEmpty() {

              @Override
              public void onSuccess() {
//                cordova.getActivity().runOnUiThread(() -> callbackContext.success(" device success"));
                callbackContext.success(" device success");
                System.out.println("Report: On Success");

                System.out.println("=============================Success===================");
              }

              @Override
              public void onFail(ResponseError responseError) {
                System.out.println("Report: onFail");
                System.out.println("=====================================BEGIN==========================");
                System.out.println(responseError.getErrorMessage());
                callbackContext.error("Register device failed: " + responseError.getErrorMessage());
//                cordova.getActivity().runOnUiThread(() -> callbackContext.error("Register device failed: " + responseError.getErrorMessage()));
                System.out.println("=====================================END==========================");

              }

              @Override
              public void showLoading() {
                System.out.println("Report: showLoading");

              }

              @Override
              public void hideLoading() {
                System.out.println("Report: hideLoading");

              }
            });
          } catch (Exception e) {
            Log.d("Kiemtra bug: ", e.getMessage());
            cordova.getActivity().runOnUiThread(() -> callbackContext.error("Register device failed in try catch runOnUiThread: " + e.getMessage()));
          }
        });
      } catch (Exception e) {
        Log.d("Kiemtra bug: tétttt ", e.getMessage());
        callbackContext.error("Register device failed: try catch " + e.getMessage());
      }
    } else {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }

}
