import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'google_maps_webservice_method_channel.dart';

abstract class GoogleMapsWebservicePlatform extends PlatformInterface {
  /// Constructs a GoogleMapsWebservicePlatform.
  GoogleMapsWebservicePlatform() : super(token: _token);

  static final Object _token = Object();

  static GoogleMapsWebservicePlatform _instance = MethodChannelGoogleMapsWebservice();

  /// The default instance of [GoogleMapsWebservicePlatform] to use.
  ///
  /// Defaults to [MethodChannelGoogleMapsWebservice].
  static GoogleMapsWebservicePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [GoogleMapsWebservicePlatform] when
  /// they register themselves.
  static set instance(GoogleMapsWebservicePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<Map<String, String>> getHeaders();
}
