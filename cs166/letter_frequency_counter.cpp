#include <iostream>
#include <fstream>

using namespace std;

char roton(char c, int rot) {
	int res = c + rot;

	if (res < 97)
		res += 26;
	else if (res > 122)
		res -= 26;
	return (char)res;
}

int main(int ac, char **av) {
	if (ac < 2) {
		cout << "Usage: ./lfc <file>" << endl;
		return 0;
	}

	fstream cipher(av[1]);
	if (!cipher.is_open()) {
		cout << "Failed to open " << av[1] << endl;
		return 0;
	}
	int cnt[26] = {0};
	string line;
	string ciphertext;
	while (getline(cipher, line)) {
		ciphertext += line;
		for (auto x : line) {
			char c = x | 32;
			cnt[c - 97]++;
		}
	}
	cipher.close();

	int most = 0;
	for (int i = 1; i < 26; i++) {
		if (cnt[most] < cnt[i])
			most = i;
	}
	fstream res("crack_result.txt", fstream::in | fstream::out | fstream::trunc);
	if (!res.is_open()) {
		cout << "Failed to create result file" << endl;
		return 0;
	}
	string mcl = "eariotns";
	for (auto x : mcl) {
		int rot = x - 97 - most;
		for (auto y : ciphertext) {
			res << roton(y, rot);
		}
		res << endl << endl;
	}
	res.close();
	cout << "Done" << endl;
	return 0;
}
