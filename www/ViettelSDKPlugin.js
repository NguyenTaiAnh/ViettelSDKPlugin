var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'ViettelSDKPlugin', 'coolMethod', [arg0]);
};

exports.getDeviceId = function (success, error) {
    exec(success, error, 'ViettelSDKPlugin', 'getDeviceId');
};

exports.registerDevice = function (arg0, success, error) {
    exec(success, error, 'ViettelSDKPlugin', 'registerDevice', [arg0]);
};

exports.authorisationPendingRequest = function (arg0, arg1, arg2, success, error) {
    exec(success, error, 'ViettelSDKPlugin', 'authorisationPendingRequest', [arg0, arg1, arg2]);
};
