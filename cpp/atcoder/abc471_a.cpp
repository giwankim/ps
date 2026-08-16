#include <bits/stdc++.h>

int main() {
  std::ios_base::sync_with_stdio(false);
  std::cin.tie(nullptr);

  int a, b;
  std::cin >> a >> b;
  // a % b == 0 makes the quotient exact: a / b == 9 alone would truncate 9.5 to 9.
  bool nine = a + b == 9 || a - b == 9 || a * b == 9 || (a / b == 9 && a % b == 0);
  std::cout << (nine ? "Nine" : "Nein") << '\n';
  return 0;
}
