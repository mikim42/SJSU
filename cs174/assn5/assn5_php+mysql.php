<?PHP
/*
**	CS174 - Assignment 5
**	Mingyun Kim
*/

require_once 'login.php';

define('ERRNO1', 'Error: Name not specified\n');
define('ERRNO2', 'Error: File does not exist\n');
define('ERRNO3', 'Error: File(content) is empty\n');
define('ERRNO4', 'Error: File(content) is too large (max: 1024)\n');
define('ERRNO5', 'Error: Data upload failed\n');

function fileUpload($name, $filename, $conn, $tb) {
	if ($name == '') return ERRNO1;
	if (!file_exists($filename)) return ERRNO2;
	$lines = file($filename);
	$text = '';
	foreach ($lines as $line) $text .= $line;
	if (strlen($text) == 0 || strlen($text) > 1024)
		return strlen($text) == 0 ? ERRNO3 : ERRNO4;
	$result = $conn->query("INSERT INTO $tb(name, content) values('$name', '$text')");
	if (!$result) return ERRNO5;
	return 'Data upload completed</br>';
}

$conn = new mysqli($hn, $un, $pw, $db);
if ($conn->connect_error) die($conn->connect_error);

echo <<<_END
<html>
	<head>
		<title>PHP + MySQL</title>
	</head>
	<body>
		<h1>
			CS174 - Assignment 5 </br>
			Mingyun Kim </br>
		</h1>
		<form method='post' action='assn5_php+mysql.php' enctype='multipart/form-data'>
			Name: <input type='text' id='textin' name='textin'>
			<input type='file' name='filename'><input type='submit' value='Upload'>
		</form>
_END;

if ($_FILES) {
	$fname = $_FILES['filename']['name'];
	move_uploaded_file($_FILES['filename']['tmp_name'], $fname);
	echo fileUpload($_POST['textin'], $fname, $conn, $tb).'<br/>';
	unlink($fname);
}

$result = $conn->query("SELECT * FROM $tb");
if (!$result) die($conn->error);
echo '</br>DATABASE:</br>';
for ($i = 0; $i < $result->num_rows; $i++) {
	$result->data_seek($i);
	$row = $result->fetch_array(MYSQLI_ASSOC);
	echo '<strong>Name</strong>: '.$row['name'].' | <strong>Content</strong>: '.$row['content'].'</br>';
}
$result->close();
$conn->close();

echo '</body></html>';
?>
