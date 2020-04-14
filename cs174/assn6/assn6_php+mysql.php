<?PHP
/*
**	CS174 - Assignment 6
**	Mingyun Kim
*/

require_once 'login.php';

define('ERRDB', 'Error: </br>Database access has failed.');
define('ERRNO1', 'Error: </br>Empty input submitted');
define('ERRNO2', 'Error: </br>Input is too large');

function mysql_fix_string($conn, $string) {
	if (get_magic_quotes_gpc()) $string = stripslashes($string);
	return $conn->real_escape_string($string);
}

$conn = new mysqli($hn, $un, $pw, $db);
if ($conn->connect_error) die(ERRDB);

$query = "CREATE TABLE information (
	advname VARCHAR(32) NOT NULL,
	sname VARCHAR(32) NOT NULL,
	sid CHAR(9) NOT NULL,
	ccode CHAR(5) NOT NULL
)";
$conn->query($query);

$query = "DESCRIBE information";
$result = $conn->query($query);
if (!$result) die (ERRDB);
$result->close();

echo <<<_END
<html>
	<head>
		<title>PHP + MySQL</title>
	</head>
	<body>
		<h1>
			CS174 - Assignment 6 </br>
			Mingyun Kim </br>
		</h1>
		<h2> Add Data</h2>
		<form method='post' action='assn6_php+mysql.php' enctype='multipart/form-data'>
			<pre>
			Advisor Name : <input type='text' id='aname' name='aname'></br>
			Student Name : <input type='text' id='sname' name='sname'></br>
			  Student ID : <input type='text' id='sid' name='sid' placeholder='9-digit ID'></br>
			  Class Code : <input type='text' id='code' name='code' placeholder='5-digit code'></br>
			<input type='submit' name='add' value='Add Data'>
			</pre>
		</form>
		<h2> Search Data</h2>
		<form method='post' action='assn6_php+mysql.php' enctype='multipart/form-data'>
			<pre>
			To search all, leave the box empty.</br>
	      Search by Advisor Name : <input type='text' id='keyword' name='keyword'></br>
			<input type='submit' name='search' value='Search Data'>
			</pre>
		</form>
		<hr>
_END;

if (isset($_POST['add'])) {
	$aname = mysql_fix_string($conn, $_POST['aname']);
	$sname = mysql_fix_string($conn, $_POST['sname']);
	$sid = mysql_fix_string($conn, $_POST['sid']);
	$code = mysql_fix_string($conn, $_POST['code']);
	if (!$aname || !$sname || !$sid || !$code) die (ERRNO1);
	if (strlen($aname) > 32 || strlen($sname) > 32 || strlen($sid) > 9 || strlen($code) > 5) die (ERRNO2);
	$query = "INSERT INTO information VALUES('$aname', '$sname', '$sid', '$code')";
	$result = $conn->query($query);
	if (!$result) die (ERRDB);
	echo "<pre>Data Add Completed</pre>";
}

if (isset($_POST['search'])) {
	$keyword = mysql_fix_string($conn, $_POST['keyword']);
	echo <<<_END
<table border="1" cellpadding="5" style="border-collapse: collapse; text-align: center;">
	<tr>
		<th>Advisor Name</th>
		<th>Student Name</th>
		<th>Student ID</th>
		<th>Class Code</th>
	</tr>
_END;
	if (!$keyword) $query = "SELECT * FROM information";
	else $query = "SELECT * FROM information where advname='$_POST[keyword]'";
	$result = $conn->query($query);
	if (!$result) die (ERRDB);
	for ($j = 0; $j < $result->num_rows; ++$j) {
		$result->data_seek($j);
		$row = $result->fetch_array(MYSQLI_NUM);
		echo "</tr>";
		for ($k = 0; $k < 4; ++$k) echo "<td>$row[$k]</td>";
		echo "</tr>";
	}
	echo "</table>";
	$result->close();
}
$conn->close();
echo '</body>';
echo '</html>';
?>
