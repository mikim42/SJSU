<?PHP
/*
**	CS174 - Assignment 3
**	Mingyun Kim
*/

define("ERRNO1", "File Does Not Exist\n");
define("ERRNO2", "File Contains Other Than 1000 Characters\n");
define("ERRNO3", "File Contains Other Than Numeric\n");

function fact($n) {
	$sum = 1;
	for ($i = 1; $i <= $n; $i++)
		$sum *= $i;
	return $sum;
}

function sumFact($n) {
	$sum = 0;
	while ($n > 0) {
		$tmp = fact($n % 10);
		$sum += $tmp;
		$n = ($n - $n % 10) / 10;
	}
	return $sum;
}

function fileUpload($filename) {
	if (!file_exists($filename)) {
		print "ERROR: ".ERRNO1;
		return NULL;
	}
	$lines = file($filename, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
	$text = "";
	foreach ($lines as $line)
		$text .= $line;
	if (strlen($text) != 1000) {
		print "ERROR: ".ERRNO2;
		return NULL;
	}
	$top = 0;
	for ($i = 4; $i < 1000; $i++) {
		if (!is_numeric($text[$i]) ||
			!is_numeric($text[$i - 1]) || !is_numeric($text[$i - 2]) ||
			!is_numeric($text[$i - 3]) || !is_numeric($text[$i - 4])) {
			print "ERROR: ".ERRNO3;
			return NULL;
		}
		$top = max($top, $text[$i] * $text[$i - 1] * $text[$i - 2] *
			$text[$i - 3] * $text[$i - 4]);
	}
	return sumFact($top);
}

function tester() {
	print "case 1:\n";
	if (($res = fileUpload("in.txt")) !== NULL)
		print "Test Passed: ".$res."<br/>\n";
	else
		print "Test Failed<br/>\n";
	print "case 2:\n";
	if (($res = fileUpload("in2.txt")) !== NULL)
		print "Test Passed: ".$res."<br/>\n";
	else
		print "Test Failed<br/>\n";
	print "case 3:\n";
	if (($res = fileUpload("in3.txt")) !== NULL)
		print "Test Passed: ".$res."<br/>\n";
	else
		print "Test Failed<br/>\n";
	print "case 4:\n";
	if (($res = fileUpload("in4.txt")) !== NULL)
		print "Test Passed: ".$res."<br/>\n";
	else
		print "Test Failed<br/>\n";
	print "case 5:\n";
	if (($res = fileUpload("in5.txt")) !== NULL)
		print "Test Passed: ".$res."<br/>\n";
	else
		print "Test Failed<br/>\n";
}

echo <<<_END
<html>
	<head>
		<title>PHP Form Upload</title>
	</head>
	<body>
		<h1>
			CS174 - Assignment 3 </br>
			Mingyun Kim </br>
		</h1>
		<form method="post" action="assn3_fileUpload.php" enctype="multipart/form-data">
			Select a txt File:
			<input type="file" name="filename" size="1000"><input type="submit" value="Upload">
		</form>
		<form method="post" action="assn3_fileUpload.php" enctype="multipart/form-data">
			Test Examples:
			<input type="submit" name="tester" value="Run Tester">
		</form>
_END;

if (isset($_POST['tester']))
	tester();

if ($_FILES) {
	$name = $_FILES['filename']['name'];
	move_uploaded_file($_FILES['filename']['tmp_name'], $name);
	echo fileUpload($name)."<br/>";
}
echo "</body></html>";
?>
