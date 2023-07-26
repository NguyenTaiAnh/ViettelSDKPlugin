

import GoSignSDKLite

@objc(ViettelSDKPlugin)
class ViettelSDKPlugin: CDVPlugin {

    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {
        
        var pluginResult: CDVPluginResult? = nil
        if let echo = command.arguments.first as? String, !echo.isEmpty {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: echo)
        } else {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
        }
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    

    @objc(getDeviceId:)
    func getDeviceId(command: CDVInvokedUrlCommand){
        var pluginResult: CDVPluginResult? = nil
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs:UserDefaults.standard.deviceID)
        print(UserDefaults.standard.deviceID)
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(registerDevice:)
        func registerDevice(command: CDVInvokedUrlCommand) {
            var pluginResult: CDVPluginResult? = nil
            if let echo = command.arguments.first as? String, !echo.isEmpty {
                DispatchQueue.global(qos: .background).async {
                    API.registerDevice(authenToken: echo) { result in
                        switch result {
                        case .success(let res):
                            print("kiemtra: res:", res)
                            DispatchQueue.main.async {
                                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "success")
                                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                            }
                        case .failure(let error):
                            print("kiemtra: error", error.localizedDescription)
                            DispatchQueue.main.async {
                                pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "\(error.localizedDescription)")
                                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                            }
                        }
                    }
                }
            } else {
                pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid argument")
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            }
        }
    
    @objc(authorisationPendingRequest:)
        func authorisationPendingRequest(command: CDVInvokedUrlCommand) {
            var pluginResult: CDVPluginResult? = nil
//            var arguments = command.arguments as? [Any]
//            var request = arguments?.first as? String
//            var access_token = arguments?[1] as? String
//            var transaction_id = arguments?[2] as? String
//            print("request:", command.arguments.first)
//            print("access_token:", command.arguments[1])
//            print("transactionID:", command.arguments[2])
            
            if let request = command.arguments.first as? String, !request.isEmpty,
               let access_token  = command.arguments?[1] as? String, !access_token.isEmpty,
               let transactionID = command.arguments?[2] as? String, !transactionID.isEmpty
            {
                print("request:", request)
                print("access_token:", access_token)
                print("transactionID:", transactionID)
                DispatchQueue.global(qos: .background).async {
                    API.authoriseaPendingRequest(authenToken: access_token, pendingAuthorisationAPIResponse: PendingAuthorisationAPIResponse(transactionID: transactionID, request: request, hashAlgorithm: "SHA256")) { result in
                        switch result {
                        case .success(let res):
                            print("kiemtra: res:", res)
                            DispatchQueue.main.async {
                                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "success")
                                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                            }
                        case .failure(let error):
                            print("kiemtra: error", error.localizedDescription)
                            DispatchQueue.main.async {
                                pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "\(error.localizedDescription)")
                                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                            }
                        }
                    }
                }
            } else {
                pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid argument")
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            }
        }
    
}
