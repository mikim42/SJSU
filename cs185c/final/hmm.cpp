/*	CS 185C: Midterm 2
 *	Mingyun Kim
 */

#include <cstdlib>
#include <iostream>
#include <iomanip>
#include <map>
#include <unordered_map>
#include <vector>
#include <fstream>
#include <chrono>
#include <filesystem>
#include <sstream>

using namespace std;

string	OBSV_PATH = "./prep/observation/";
string	TEST_PATH = "./prep/test/";
string	OBSM_PATH = "./prep/observation_matrix";
string	TRAN_PATH = "./prep/transition_matrix";

string V = "abcdefghijklmnopqrstuvwxyz";
int T, N, M = V.size();
int MAX_ITER = 100;
vector<string> Q;
vector<vector<double>> A, B, ALPHA, BETA, GAMMA;
vector<vector<vector<double>>> DGAMMA;
vector<double> PI, C;
vector<int> O;
unordered_map<char, int> INDEX;
unordered_map<string, double> S;

void reset_val(void);
void load_data(string obs);
void alpha_pass(void);
void beta_pass(void);
void gamma_pass(void);
void reestimate(void);
bool stopping(void);
double score(string obs, bool print);
void printAB(void);

void die(string s);

int main(void) {
	cout << scientific << setprecision(6);
	srand(clock());
	for (const auto &family : filesystem::directory_iterator(OBSV_PATH)) {
		bool stop = true;

		reset_val();
		load_data(family.path().filename());
		cout << "Traning " << family.path().filename() << "..." << endl;
		while (stop) {
			alpha_pass();
			beta_pass();
			gamma_pass();
			reestimate();
			stop = stopping();
		}
		//printAB();
		cout << "Scoring " << family.path().filename() << "..." << endl;
		for (int i = 0; i < N; ++i) {
			if (family.path().filename() == Q[i]) cout << "\x1b[46m";
			cout << setw(35) << Q[i] << ": " << score(Q[i], false) << "\x1b[0m" << endl;
		}
	}
	return 0;
}

void reset_val(void) {
	A.clear();
	B.clear();
	ALPHA.clear();
	BETA.clear();
	GAMMA.clear();
	DGAMMA.clear();
	PI.clear();
	C.clear();
	O.clear();
	Q.clear();
}

void load_data(string obs) {
	fstream f1(TRAN_PATH, fstream::in), f2(OBSM_PATH, fstream::in);
	if (!f1 || !f2) die(string("Failed to open File"));
	string state, line1, line2;
	while (getline(f1, line1) && getline(f2, line2)) {
		istringstream l1(line1), l2(line2);
		vector<double> v1, v2;
		double d;
		l1 >> state; l2 >> state;
		Q.push_back(state);
		while (l1 >> d) v1.push_back(d); A.push_back(v1);
		while (l2 >> d) v2.push_back(d); B.push_back(v2);
	}
	f1.close();
	f2.close();
	N = Q.size();
	double sum = 0;
	for (int i = 0; i < N; ++i) {
		PI.push_back(0.01 + ((double)rand() / RAND_MAX) + ((double)rand() / RAND_MAX) * 0.2);
		sum += PI[i];
	}
	for (int i = 0; i < M; ++i)
		INDEX[V[i]] = i;
	fstream f3(OBSV_PATH + obs, fstream::in);
	string train;
	if (!f3) die(string("Failed to open File"));
	f3 >> train;
	f3.close();
	for (auto x : train) O.push_back(INDEX[x]);
	T = O.size();
	for (int i = 0; i < N; ++i) PI[i] /= sum;
	ALPHA = vector<vector<double>>(T, vector<double>(N, 0));
	BETA = vector<vector<double>>(T, vector<double>(N, 0));
	GAMMA = vector<vector<double>>(T, vector<double>(N, 0));
	DGAMMA = vector<vector<vector<double>>>(T, vector<vector<double>>(N, vector<double>(N, 0)));
	C = vector<double>(T, 0);
}

void reestimate(void) {
	for (int i = 0; i < N; ++i)
		PI[i] = GAMMA[0][i];

	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < N; ++j) {
			double numer = 0, denom = 0;
			for (int t = 0; t < T - 1; ++t) {
				numer += DGAMMA[t][i][j];
				denom += GAMMA[t][i];
			}
			if (denom == 0) cout << "ERROR: Re-estim A has NAN" << endl;
			else A[i][j] = numer / denom;
		}
	}

	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < M; ++j) {
			double numer = 0, denom = 0;
			for (int t = 0; t < T - 1; ++t) {
				if (O[t] == j) numer += GAMMA[t][i];
				denom += GAMMA[t][i];
			}
			if (denom == 0) cout << "ERROR: Re-estim B has NAN" << endl;
			else B[i][j] = numer / denom;
		}
	}
}

void alpha_pass(void) {
	C[0] = 0;
	for (int i = 0; i < N; ++i) {
		ALPHA[0][i] = PI[i] * B[i][O[0]];
		C[0] += ALPHA[0][i];
	}
	C[0] = (double)1.0 / C[0];
	for (int i = 0; i < N; ++i) ALPHA[0][i] *= C[0];
	for (int t = 1; t < T; ++t) {
		C[t] = 0;
		for (int i = 0; i < N; ++i) {
			ALPHA[t][i] = 0;
			for (int j = 0; j < N; ++j)
				ALPHA[t][i] += ALPHA[t - 1][j] * A[j][i];
			ALPHA[t][i] *= B[i][O[t]];
			C[t] += ALPHA[t][i];
		}
		C[t] = (double)1.0 / C[t];
		for (int i = 0; i < N; ++i)
			ALPHA[t][i] *= C[t];
	}
}

void beta_pass(void) {
	for (int i = 0; i < N; ++i)
		BETA[T - 1][i] = C[T - 1];
	for (int t = T - 2; t >= 0; --t) {
		for (int i = 0; i < N; ++i) {
			BETA[t][i] = 0;
			for (int j = 0; j < N; ++j)
				BETA[t][i] += A[i][j] * B[j][O[t + 1]] * BETA[t + 1][j];
			BETA[t][i] *= C[t];
		}
	}
}

void gamma_pass(void) {
	for (int t = 0; t < T - 1; ++t) {
		double denom = 0;
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j)
				denom += ALPHA[t][i] * A[i][j] * B[j][O[t + 1]] * BETA[t + 1][j];
		}
		for (int i = 0; i < N; ++i) {
			GAMMA[t][i] = 0;
			for (int j = 0; j < N; ++j) {
				if (denom == 0) cout << "DGAMMA: NAN" << endl;
				DGAMMA[t][i][j] = (ALPHA[t][i] * A[i][j] * B[j][O[t + 1]] * BETA[t + 1][j]) / denom;
				GAMMA[t][i] += DGAMMA[t][i][j];
			}
		}
	}
	double denom = 0;
	for (int i = 0; i < N; ++i)
		denom += ALPHA[T - 1][i];
	if (denom == 0) cout << "GAMMA: NAN" << endl;
	for (int i = 0; i < N; ++i)
		GAMMA[T - 1][i] = ALPHA[T - 1][i] / denom;
}

bool stopping(void) {
	static double oldLog = -numeric_limits<double>::max();
	static int iter = 0;
	double logProb = 0;

	for (int t = 0; t < T; ++t)
		logProb += log(C[t]);
	logProb = -logProb;
	++iter;
	if (iter < MAX_ITER && logProb > oldLog) {
		oldLog = logProb * 0.99;
		cout << iter << "-th iter, log: " << oldLog << endl;
		return true;
	}
	oldLog = -numeric_limits<double>::max();
	iter = 0;
	return false;
}

double score(string obs, bool print) {
	fstream f(TEST_PATH + obs, fstream::in);
	string line;
	f >> line;
	f.close();
	vector<int> o;
	for (auto x : line) o.push_back(INDEX[x]);
	int T2 = o.size();
	vector<vector<double>> alpha(T2, vector<double>(N, 0));
	vector<double> c(T2, 0);

	for (int i = 0; i < N; ++i)
		alpha[0][i] = PI[i] * B[i][o[0]];
	for (int t = 1; t < T2; ++t) {
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j)
				alpha[t][i] += alpha[t - 1][j] * A[j][i];
			alpha[t][i] *= B[i][o[t]];
		}
	}
	if (print) {
		for (auto x : alpha) {
			for (auto y : x) cout << y << " ";
			cout << endl;
		}
	}
	double s = 0;
	for (int i = 0; i < N; ++i) s += alpha[T2 - 1][i];
	return s;
}

void die(string s) {
	cout << s << endl;
	exit(1);
}

void printAB(void) {
	for (auto x : PI) cout << x << " ";
	cout << endl;
	cout << endl;
	for (auto x : A) {
		for (auto y : x) cout << y << " ";
		cout << endl;
	}
	cout << endl;
	for (auto x : B) {
		for (auto y : x) cout << y << " ";
		cout << endl;
	}
	cout << endl;
}
