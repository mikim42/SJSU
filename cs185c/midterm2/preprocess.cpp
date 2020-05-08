/*	CS185C: Midterm 2
 *	Mingyun Kim
 */

#include <iostream>
#include <fstream>
#include <filesystem>
#include <unordered_map>
#include <vector>
#include <ctime>
#include <chrono>
#include <map>

using namespace std;

string	DATA_PATH = "./malicia";
string	OBSP_PATH = "./prep/observation/";
string	OBSM_PATH = "./prep/observation_matrix";
string	TRAN_PATH = "./prep/transition_matrix";
string	TEST_PATH = "./prep/test/";
int		MAX_OPS = 1000000;

string	V = "abcdefghijklmnopqrstuvwxyz0";

unordered_map<string, string> Q;

void observation(void);
void transition(void);

unordered_map<string, int> sort_ops(unordered_map<string, int> counter);
void die(string msg);

int main(void) {
	observation();
	transition();
	return (0);
}

void observation(void) {
	for (const auto &family : filesystem::directory_iterator(DATA_PATH)) {
		unordered_map<string, int> counter;
		vector<string> comb;
		string state = family.path().filename();

		for (const auto &txt : filesystem::directory_iterator(family.path())) {
			string fn = txt.path();
			fstream f(fn, fstream::in);

			if (!f) die(string("Failed to open file: ") + fn);
			string lines;
			while (f >> lines)  {
				comb.push_back(lines);
				++counter[lines];
			}
			f.close();
		}
		if (comb.empty()) continue;
		Q[state] = "";
		unordered_map<string, int> tops = sort_ops(counter);
		int i = 0;
		for (auto &x : comb) {
			if (tops[x] > 0) Q[state] += V[tops[x] - 1];
			else Q[state] += '0';
			if (++i == MAX_OPS) break;
		}
		fstream f2(OBSP_PATH + state, fstream::out | fstream::trunc);
		fstream f3(TEST_PATH + state, fstream::out | fstream::trunc);
		if (!f2 || !f3) die(string("Failed to open file: ") + OBSP_PATH + state);
		string test = Q[state].substr(Q[state].size() / 10 * 9, Q[state].size());
		Q[state] = Q[state].substr(0, Q[state].size() / 10 * 9);
		f2 << Q[state];
		f3 << test;
		f2.close();
		f3.close();
	}
	fstream f(OBSM_PATH, fstream::out | fstream::trunc);
	if (!f) die(string("Failed to open file: ") + OBSM_PATH);
	for (auto &x : Q) {
		unordered_map<char, double> counter;
		f << x.first << " ";
		for (auto &y : x.second) counter[y]++;
		for (auto &y : V) {
			f << (counter[y] == 0 ? 0 : counter[y] / x.second.length()) << " ";
		}
		f << endl;
	}
	f.close();
}

void transition(void) {
	srand(clock());
	fstream f(TRAN_PATH, fstream::out | fstream::trunc);
	if (!f) die(string("Failed to open: ") + TRAN_PATH);
	for (auto &x : Q) {
		vector<double> v;
		double sum = 0;
		for (size_t i = 0; i < Q.size(); ++i) {
			double r = 0.1 + ((double)rand() / RAND_MAX);
			v.push_back(r);
			sum += r;
		}
		f << x.first << " ";
		for (size_t i = 0; i < v.size(); ++i) f << (v[i] / sum) << " ";
		f << endl;
	}
	f.close();
}

unordered_map<string, int> sort_ops(unordered_map<string, int> counter) {
	multimap<int, string> mm;
	unordered_map<string, int> res;

	for (auto &x : counter) mm.insert(pair<int, string>(x.second, x.first));
	size_t i = 0;
	for (auto x = mm.rbegin(); x != mm.rend(); ++x) {
		res[x->second] = ++i;
		if (i == V.size() - 1) break;
	}
	return res;
}

void die(string msg) {
	cout << msg << endl;
	exit(1);
}
