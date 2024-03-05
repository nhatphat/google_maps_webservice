
import 'google_maps_webservice_platform_interface.dart';

class GoogleMapsWebservice {
  GoogleMapsWebservicePlatform get _service {
    return GoogleMapsWebservicePlatform.instance;
  }

  Future<Map<String, String>> getHeaders() async {
    return _service.getHeaders();
  }
}
