<?PHP
/*
**	CS174 - Assignment 1
**	Mingyun Kim
*/

function isPrime($primes, $n)
{
	foreach ($primes as $p) {
		if ($n % $p == 0)
			return false;
	}
	return true;
}

function findPrimes($n)
{
	$primes = array();

	if ($n >= 2)
		array_push($primes, 2);

	for ($i = 3; $i <= $n; $i += 2) {
		if (isPrime($primes, $i)) {
			array_push($primes, $i);
		}
	}
	//$primes = implode(", ", $primes);
	return $primes;
}

function tester()
{
	$primes = findPrimes(10);
	//print_r($primes);
	$test = array(2, 3, 5, 7);
	print "Is output from findPrimes(10) equal to \"2, 3, 5, 7\"?\n";
	if (assert($test == $primes))
		print "Test passed\n";
	else
		print "Test failed\n";

	$primes = findPrimes(100);
	$test = array(
		2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
		41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
	);
	print
		"Is output from findPrimes(100) equal to\n" .
		"\"2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,\n" .
		"43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97\"?\n";
	if (assert($test == $primes))
		print "Test passed\n";
	else
		print "Test failed\n";
}
print "CS174 - Assignment 1\n";
print "Mingyun Kim\n";
tester();
?>
