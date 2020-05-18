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
int		MAX_OPS = 100000;
int		NG = 1;

string	V = "abcdefghijklmnopqrstuvwxyz";

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
			string line = "";
			string nGram = "";
			int i = 0;
			while (f >> line)  {
				if (i < NG) {
					nGram += line;
					++i;
				}
				else {
					comb.push_back(nGram);
					++counter[nGram];
					nGram.clear();
					i = 0;
				}
			}
			f.close();
		}
		if (comb.empty()) continue;
		Q[state] = "";
		unordered_map<string, int> tops = sort_ops(counter);
		int i = 0;
		for (auto &x : comb) {
			if (tops[x] > 0) Q[state] += V[tops[x] - 1];
			else Q[state] += 'z';
			if (++i == MAX_OPS) break;
		}
		fstream f2(OBSP_PATH + state, fstream::out | fstream::trunc);
		fstream f3(TEST_PATH + state, fstream::out | fstream::trunc);
		if (!f2 || !f3) die(string("Failed to open file: ") + OBSP_PATH + state);
		string test = Q[state].substr(Q[state].size() / 10 * 7);
		Q[state] = Q[state].substr(0, Q[state].size() / 10 * 7);
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
		//
		int z = 0;
		for (auto &y : V) if (counter[y] == 0) counter[y]++, z++;
		//
		for (auto &y : V) {
			// non 0 on count == 0.
			// f << (counter[y] == 0 ? 1 : counter[y] / x.second.length()) << " ";
			if (counter[y] == 0) cout << "ERROR found! value NAN: observation" << endl;
			f << counter[y] / (x.second.length() + z) << " ";
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
		for (size_t i = 0; i < v.size(); ++i) {
			if (v[i] == 0) cout << "ERROR found! value NAN: transition" << endl;
			f << (v[i] / sum) << " ";
		}
		f << endl;
	}
	f.close();
}

unordered_map<string, int> sort_ops(unordered_map<string, int> counter) {
	multimap<int, string> mm;
	unordered_map<string, int> res;

	for (auto &x : counter) mm.insert(pair<int, string>(x.second, x.first));
	size_t i = 1;
	for (auto x = mm.rbegin(); x != mm.rend() && i < V.size(); ++x)
		res[x->second] = i++;
	return res;
}

void die(string msg) {
	cout << msg << endl;
	exit(1);
}
