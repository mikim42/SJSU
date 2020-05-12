<?PHP
/*	CS174 - Midterm 2: Online Virus Check
 *	Mingyun Kim
 *	Zihui Chen
 */

require_once 'login.php';


/*	Defines constant strings
 *	For programmer's convenience and
 *	to make it easy to handle messages
 */

define('SALT1', "qm&h*");
define('SALT2', "pg!@");

define('LOGIN_NO', 1);
define('LOGIN_ADMIN', 2);
define('LOGIN_USER', 3);

define('ERR_DB', '<h1>Error: Database access has failed</h1>');
define('ERR_AUTH', '<h1>Error: User authentication failed</h1>');
define('ERR_INPUT', '<h1>Error: Empty or Invalid Input Submitted</h1>');

define('MSG_SIGNUP', '<h2>User sign up has been completed. Please sign in!</h2>');
define('MSG_INFEC', '<h2>The file is infected</h2>');
define('MSG_NOINFEC', '<h2>The file is not infected by any known malware</h2>');
define('MSG_UPLOAD', '<h3>Malware upload has been completed</h3>');
define('MSG_REQUEST', '<h3>Malware request has been completed</h3>');
define('MSG_DELETE', '<h3>Malware delete has been completed</h3>');
define('MSG_ACCEPT', '<h3>Accept request has been completed</h3>');
define('MSG_REJECT', '<h3>Reject request has been completed</h3>');


/*	database
 *	Initiates database tables and
 *	checks if it is successfully created
 *	Otherwise, kills the process
 */

$conn = new mysqli($hn, $un, $pw, $db);
if ($conn->connect_error) die(to_main(ERR_DB));

$query = "CREATE TABLE accounts (
	id VARCHAR(64) NOT NULL UNIQUE,
	pw VARCHAR(64) NOT NULL,
	fname VARCHAR(64) NOT NULL,
	lname VARCHAR(64) NOT NULL
)";
$conn->query($query);

$query = "DESCRIBE accounts";
$result = $conn->query($query);
if (!$result) die(to_main(ERR_DB));
$result->close();

$query = "CREATE TABLE malwares (
	id INT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
	malware VARCHAR(64) NOT NULL,
	signature CHAR(40) NOT NULL
)";
$conn->query($query);

$query = "DESCRIBE malwares";
$result = $conn->query($query);
if (!$result) die(to_main(ERR_DB));
$result->close();

$query = "CREATE TABLE requests (
	id INT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
	malware VARCHAR(64) NOT NULL,
	signature CHAR(40) NOT NULL
)";
$conn->query($query);

$query = "DESCRIBE requests";
$result = $conn->query($query);
if (!$result) die(to_main(ERR_DB));
$result->close();


/*	Sanitizer
 *	Provided from instructor
 */

function mysql_fix_string($conn, $string) {
	if (get_magic_quotes_gpc()) $string = stripslashes($string);
	return $conn->real_escape_string($string);
}

function mysql_entities_fix_string($conn, $string) {
	return htmlentities(mysql_fix_string($conn, $string));
}

function sanitizeString($var) {
	return htmlentities(strip_tags(stripslashes($var)));
}

function sanitizeMySQL($conn, $var) {
	return sanitizeString($conn->real_escape_string($var));
}


/*	Login Handler
 *	Uses cookie to handle user information
 *	Only stores user's ID and name to reduce security problems
 *	Checks if a user signed up/in/out
 *	Checks if the user is admin or not
 */

function to_main($msg) {
	unset_user();
	return "<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>".
			$msg."<input type='submit' name='back' value='Back to Main'>"."</form>";
}

function login_user($conn) {
	$id = mysql_entities_fix_string($conn, $_POST['loginid']);
	$pw = mysql_entities_fix_string($conn, $_POST['loginpw']);
	if (empty($id) || empty($pw)) die(to_main(ERR_INPUT));
	if (!auth_user($conn, $id, $pw)) die(to_main(ERR_AUTH));
	header("Refresh:0");
}

function register_user($conn) {
	$id = mysql_entities_fix_string($conn, $_POST['regid']);
	$pw = mysql_entities_fix_string($conn, $_POST['regpw']);
	$cpw = mysql_entities_fix_string($conn, $_POST['regcpw']);
	$fname = mysql_entities_fix_string($conn, $_POST['regfname']);
	$lname = mysql_entities_fix_string($conn, $_POST['reglname']);
	if (empty($id) || empty($pw) || empty($fname) || empty($lname) || empty($cpw) || $pw != $cpw)
		die(to_main(ERR_INPUT));
	$token = hash('ripemd128', SALT1.$pw.SALT2);
	$query = "INSERT INTO accounts VALUES('$id', '$token', '$fname', '$lname')";
	if (!($result = $conn->query($query))) die(to_main(ERR_INPUT));
	return MSG_SIGNUP;
}

function auth_user($conn, $id, $pw) {
	$query = "SELECT * FROM accounts WHERE id='$id'";
	$result = $conn->query($query);
	if (!$result) die(to_main(ERR_DB));
	elseif ($result->num_rows) {
		$row = $result->fetch_array(MYSQLI_NUM);
		$result->close();
		$token = hash('ripemd128', SALT1."$pw".SALT2);
		if ($token == $row[1]) {
			if ($row[2] == "admin") set_user($row[2]);
			else set_user(ucfirst($row[2]).' '.ucfirst($row[3]));
			return true;
		}
	}
	return false;
}

function set_user($user) {
	setcookie('login_auth', $user, time() + 3600 * 24, '/');
}

function unset_user() {
	setcookie('login_auth', '', time() - 3600 * 24 * 30, '/');
}

function isset_user() {
	if (!isset($_COOKIE['login_auth']))
		return LOGIN_NO;
	if ($_COOKIE['login_auth'] == "admin")
		return LOGIN_ADMIN;
	else return LOGIN_USER;
}


/*	Malware Database Handler
 *	Retrieves/manipulates database
 *	Uses bin2hex other than sanitize function to handle non-ascii range values
 *	20 bytes signature of malware is extended 40 bytes string due to bin2hex conversion
 *	Admin can delete a malware from the database and accept/reject requests that
 *	submitted by users (putative malware files)
 */

function check_mw($conn, $fname) {
	$file = fopen($fname, "r");
	$content = fread($file, filesize($fname));
	fclose($file);
	$content = bin2hex($content);
	$query = "SELECT * FROM malwares";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	for ($j = 0; $j < $result->num_rows; ++$j) {
		$result->data_seek($j);
		$row = $result->fetch_array(MYSQLI_NUM);
		if (($needle = strstr($content, $row[2])))
			return MSG_INFEC."<h3>Malware: $row[1]</h3>";
	}
	return MSG_NOINFEC;
}

function add_mw($conn, $fname, $mw) {
	$file = fopen($fname, "r");
	$sig = fread($file, 20);
	fclose($file);
	$sig = bin2hex($sig);
	if (strlen($sig) != 40) die(to_main(ERR_INPUT));
	$query = "INSERT INTO malwares(malware, signature) VALUES('$mw', '$sig')";
	if(!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_UPLOAD;
}

function request_mw($conn, $fname, $mw) {
	$file = fopen($fname, "r");
	$sig = fread($file, 20);
	fclose($file);
	$sig = bin2hex($sig);
	if (strlen($sig) < 40) return '<h3>The file is too small to be infected</h3>';
	$query = "INSERT INTO requests(malware, signature) VALUES('$mw', '$sig')";
	if(!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_REQUEST;
}

function delete_mw($conn) {
	$id = mysql_entities_fix_string($conn, $_POST{'deletemw']);
	$query = "DELETE FROM malwares WHERE id=$id";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_DELETE;
}

function accept_mw($conn) {
	$mw = mysql_entities_fix_string($conn, $_POST{'acceptmw']);
	$sig = mysql_entities_fix_string($conn, $_POST{'acceptsig']);
	$req = mysql_entities_fix_string($conn, $_POST{'acceptreq']);
	$query = "INSERT INTO malwares(malware, signature) VALUES('$mw', '$sig')";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	$query = "DELETE FROM requests WHERE id=$req";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_ACCEPT;
}

function reject_mw($conn) {
	$req = mysql_entities_fix_string($conn, $_POST['rejectreq']);
	$query = "DELETE FROM requests WHERE id=$req";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_REJECT;
}

function manage_mw($conn) {
	$query = "SELECT * FROM malwares";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	if ($result->num_rows == 0) {
		echo "<h3 style='text-indent:100px;'>No Malware Data</h3>";
		return;
	}
	echo <<<_END
		<h3 style='text-indent:100px;'>Malware Management</h3>
		<table border="1" cellpadding="5" style="margin-left: 100px; border-collapse: collapse;
		text-align: center; text-valign: center;">
			<tr>
				<th>Malware Name</th>
				<th>Malware Signature</th>
				<th>Delete</th>
			</tr>
_END;
	for ($j = 0; $j < $result->num_rows; ++$j) {
		$result->data_seek($j);
		$row = $result->fetch_array(MYSQLI_NUM);
		echo '<tr>';
		echo '<td>'."$row[1]".'</td>';
		echo '<td>'."$row[2]".'</td>';
		echo "
			<td>
			<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
				<input type='hidden' name='deletemw' id='deletemw' value='$row[0]'>
				<input type='submit' name='delete' value='Delete'>
			</form>
			</td></tr>
		";
	}
	echo '</table>';
}

function reqs_mw($conn) {
	$query = "SELECT * FROM requests";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	if ($result->num_rows == 0) {
		echo "<h3 style='text-indent:100px;'>No Request Data</h3>";
		return;
	}
	echo <<<_END
		<h3 style='text-indent:100px;'>Malware Requests</h3>
		<table border="1" cellpadding="5" style="margin-left: 100px; border-collapse: collapse;
		text-align: center; text-valign: center;">
			<tr>
				<th>Malware Name</th>
				<th>Malware Signature</th>
				<th>Accept</th>
				<th>Reject</th>
			</tr>
_END;
	for ($j = 0; $j < $result->num_rows; ++$j) {
		$result->data_seek($j);
		$row = $result->fetch_array(MYSQLI_NUM);
		echo '<tr>';
		echo '<td>'."$row[1]".'</td>';
		echo '<td>'."$row[2]".'</td>';
		echo "
	<td>
	<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
		<input type='hidden' name='acceptreq' id='acceptreq' value='$row[0]'>
		<input type='hidden' name='acceptmw' id='acceptmw' value='$row[1]'>
		<input type='hidden' name='acceptsig' id='acceptsig' value='$row[2]'>
		<input type='submit' name='accept' value='Accept'>
	</form>
	</td>";
		echo "
			<td>
			<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
				<input type='hidden' name='rejectreq' id='rejectreq' value='$row[0]'>
				<input type='submit' name='reject' value='Reject'>
			</form>
			</td></tr>
		";
	}
	echo '</table>';
}


/*	HTML
 *	Separates the HTML part to increase overall readibility of the code
 */

function html_header() {
	echo <<<_END
<html>
	<head>
		<title>Midterm 2</title>
	</head>
	<body>
		<h1>
			CS174 - Midterm 2 <br>
			Mingyun Kim <br>
			Zihui Chen <br>
		<hr>
		<h1 style="text-indent:165px;">Online Virus Check</h1>
_END;
}

function print_footer() {
	echo '</body>';
	echo '</html>';
}

function html_login() {
	echo <<<_END
			<h2 style="text-indent:250px;">Login</h2>
		<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
			<pre>
		      ID : <input size='40' maxlength='32' type='text' id='loginid' name='loginid'><br>
		Password : <input size='40' maxlength='32' type='password' id='loginpw' name='loginpw'><br>
				<input type='submit' name='login' value='Sign In'>
			</pre>
		</form>
_END;
}

function html_logout() {
	echo <<<_END
	<div style="margin-left:250px";>
	<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
		<input type='submit' name='logout' value='Sign Out'>
	</form>
	</div>
_END;
}

function html_register() {
	echo <<<_END
			<h2 style="text-indent:235px;">Register</h2>
		<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
			<pre>
	      First Name : <input size='40' maxlength='32' type='text' id='regfname' name='regfname'><br>
	       Last Name : <input size='40' maxlength='32' type='text' id='reglname' name='reglname'><br>
		      ID : <input size='40' maxlength='32' type='text' id='regid' name='regid'><br>
		Password : <input size='40' maxlength='32' type='password' id='regpw' name='regpw'><br>
	Confirm Password : <input size='40' maxlength='32' type='password' id='regcpw' name='regcpw'><br>
				<input type='submit' name='reg' value='Sign Up'>
			</pre>
		</form>
_END;
}

function html_checker() {
	echo <<<_END
		<h2 style="text-indent:215px;">Virus Check</h2>
		<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
			<pre>
		      Upload a file to check virus<br>
		   <input type='file' name='filename'><input type='submit' id='check' name='check' value='Check'>
			</pre>
		</form>
		<hr>
_END;
}

function html_add() {
	echo <<<_END
		<h3 style='text-indent:100px;'>Add Malware</h3>
		<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
			<pre>
		      Upload a file to add new malware<br>
		Malware Name : <input size='40' maxlength='32' type='text' id='mwname' name='mwname'><br>
		   <input type='file' name='filename'><input type='submit' id='addmw' name='addmw' value='Add'>
			</pre>
		</form>
_END;
}

function html_request() {
	echo <<<_END
		<h2 style="text-indent:215px;">Contribute</h2>
		<form method='post' action='vcheck_main.php' enctype='multipart/form-data'>
			<pre>
		      Upload a file to request new malware<br>
		Malware Name : <input size='40' maxlength='32' type='text' id='reqmw' name='reqmw'><br>
		   <input type='file' name='filename'><input type='submit' id='rqf' name='rqf' value='Request'>
			</pre>
		</form>
_END;
}
?>
