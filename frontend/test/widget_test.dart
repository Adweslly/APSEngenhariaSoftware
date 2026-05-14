import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/main.dart';

void main() {
  testWidgets('Dashboard smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const EmergyApp());

    // Verify that the dashboard title is shown.
    expect(find.text('Dashboard - Bem-vindo ao Sistema de Emergia'), findsOneWidget);
  });
}
