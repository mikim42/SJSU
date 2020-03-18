<?PHP
/*
**	CS174 - Assignment 2
**	Mingyun Kim
*/

function validateSubtractive($prev, $val) {
	if ($prev == 1 && ($val == 5 || $val == 10))
			return true;
	if ($prev == 10 && ($val == 50 || $val == 100))
			return true;
	if ($prev == 100 && ($val == 500 || $val == 1000))
			return true;
	return false;
}

function getValue($c) {
	switch ($c) {
	case 'I': return 1;
	case 'V': return 5;
	case 'X': return 10;
	case 'L': return 50;
	case 'C': return 100;
	case 'D': return 500;
	case 'M': return 1000;
	default:  return NULL;
	}
}

function romanToArabic($roman) {
	if ($roman == NULL)
		return 0;

	$prev = getValue($roman[0]);
	$split = str_split($roman, 1);
	$cnt = -1;

	foreach ($split as $c) {
		$val = getValue($c);
		$val == $prev ? $cnt++ : $cnt = 0;
		if ($val == NULL || $cnt == 3)
			return NULL;
		if ($prev < $val) {
			if (!validateSubtractive($prev, $val))
				return NULL;
			$n -= $prev;
			$tmp = $val - $prev;
		}
		else
			$tmp = $val;
		if ($n != 0 && $tmp > $n)
			return NULL;
		$n += $tmp;
		$prev = $val;
	}
	return $n;
}

function tester() {
	$test = "";
	print "Is \"\" equal to 0?\n";
	if (assert(0 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "III";
	print "Is III equal to 3?\n";
	if (assert(3 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "IIII";
	print "Is IIII an error input?\n";
	if (assert(NULL == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "IV";
	print "Is IV equal to 4?\n";
	if (assert(4 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "VI";
	print "Is VI equal to 6?\n";
	if (assert(6 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "MCMXC";
	print "Is MCMXC equal to 1990?\n";
	if (assert(1990 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "DCCLXXXIX";
	print "Is DCCLXXXIX equal to 789\n";
	if (assert(789 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "MMMCMXCIX";
	print "Is MMMCMXCIX equal to 3999\n";
	if (assert(3999 == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "IXA";
	print "Is IXA an error input?\n";
	if (assert(NULL == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "IXM";
	print "Is IXM an error input?\n";
	if (assert(NULL == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";

	$test = "IXXL";
	print "Is IXXL an error input?\n";
	if (assert(NULL == romanToArabic($test)))
		print "Yes, test passed\n";
	else
		print "No, test failed\n";
}

print "CS174 - Assignment 2\n";
print "Mingyun Kim\n";
print "This algorithm follows \"Subtractive\" rule\n";
tester();
?>
