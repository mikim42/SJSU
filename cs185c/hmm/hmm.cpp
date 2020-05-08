#include <iostream>
#include <iomanip>

using namespace std;

/*	CS 185C - Assignment #3 - HMM: Alpha Pass/Beta Pass
 *	Mingyun Kim
 */

double	A[2][2] = {
	{0.7, 0.3},
	{0.4, 0.6}
};
double	B[2][3] = {
	{0.1, 0.4, 0.5},
	{0.7, 0.2, 0.1}
};
double	PI[2] = {0.6, 0.4};
int		O[4] = {0, 1, 0, 2};
int		N = 2;
int		T = 4;

double	alpha_pass(void) {
	double	alpha[T][N];

	for (int i = 0; i < N; i++)
		alpha[0][i] = PI[i] * B[i][O[0]];

	for (int t = 1; t < T; t++) {
		for (int i = 0; i < N; i++) {
			double	sum = 0;
			for (int j = 0; j < N; j++)
				sum += alpha[t - 1][j] * A[j][i];
			alpha[t][i] = B[i][O[t]] * sum;
		}
	}
	cout << "Alpha Pass:  [HOT]" << "        [COLD]" << endl;
	for (int t = 0; t < T; t++) {
		cout << "O[" << t << "]: " << O[t] << " | ";
		for (int n = 0; n < N; n++) {
			cout << fixed << setprecision(8) << alpha[t][n] << " | ";
		}
		cout << endl;
	}
	return 0;
}

double	beta_pass(void) {
	double	beta[T][N];

	for (int i = 0; i < N; i++)
		beta[T - 1][i] = 1;

	for (int t = T - 2; t >= 0; t--) {
		for (int i = 0; i < N; i++) {
			beta[t][i] = 0;
			for (int j = 0; j < N; j++)
				beta[t][i] += A[i][j] * B[j][O[t + 1]] * beta[t + 1][j];
		}
	}
	cout << "Beta Pass:   [HOT]" << "        [COLD]" << endl;
	for (int t = 0; t < T; t++) {
		cout << "O[" << t << "]: " << O[t] << " | ";
		for (int n = 0; n < N; n++) {
			cout << fixed << setprecision(8) << beta[t][n] << " | ";
		}
		cout << endl;
	}
	return 0;
}

int main(void) {
	alpha_pass();
	beta_pass();
	return 0;
}
