import Flutter
import UIKit

public class GoogleMapsWebservicePlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "google_maps_webservice", binaryMessenger: registrar.messenger())
    let instance = GoogleMapsWebservicePlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getHeaders":
      result([
        "X-Ios-Bundle-Identifier": Bundle.main.bundleIdentifier ?? ""
      ])
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
