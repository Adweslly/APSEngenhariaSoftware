import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiBase {
  static const String baseUrl = 'http://localhost:8080/api';

  static Map<String, String> get headers => {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  static Future<http.Response> get(String endpoint) async {
    return await http.get(Uri.parse('$baseUrl$endpoint'), headers: headers);
  }

  static Future<http.Response> post(String endpoint, dynamic body) async {
    return await http.post(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
      body: jsonEncode(body),
    );
  }

  static Future<http.Response> put(String endpoint, dynamic body) async {
    return await http.put(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
      body: jsonEncode(body),
    );
  }

  static Future<http.Response> delete(String endpoint) async {
    return await http.delete(Uri.parse('$baseUrl$endpoint'), headers: headers);
  }
}
