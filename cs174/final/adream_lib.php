<?PHP
/*	CS174 - Final: Ari's Dream
 *	Mingyun Kim
 *	Zihui Chen
 */

require_once 'login.php';


/*	Defines constant strings
 *	For programmer's convenience and
 *	to make it easy to handle messages
 */

define('LOGIN_NO', 1);
define('LOGIN_USER', 2);
define('SALT', 4);

define('ERR_DB', '<h1>Error: Database access has failed</h1>');
define('ERR_AUTH', '<h1>Error: User authentication failed</h1>');
define('ERR_INPUT', '<h1>Error: Empty or Invalid Input Submitted</h1>');



ini_set('session.use_only_cookies', 1);
$conn = new mysqli($hn, $un, $pw, $db);
if ($conn->connect_error) die(to_main(ERR_DB));

$query = "CREATE TABLE accounts (
	id VARCHAR(64) NOT NULL UNIQUE,
	pw VARCHAR(64) NOT NULL,
	fname VARCHAR(64) NOT NULL,
	lname VARCHAR(64) NOT NULL,
	salt1 CHAR(8) NOT NULL,
	salt2 CHAR(8) NOT NULL
)";
$conn->query($query);

$query = "DESCRIBE accounts";
$result = $conn->query($query);
if (!$result) die(to_main(ERR_DB));
$result->close();

$query = "CREATE TABLE posts (
	id INT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
	user VARCHAR(64) NOT NULL,
	text VARCHAR(64) NOT NULL,
	img CHAR(40) NOT NULL UNIQUE
)";
$conn->query($query);

$query = "DESCRIBE posts";
$result = $conn->query($query);
if (!$result) die(to_main(ERR_DB));
$result->close();



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



function to_main($msg) {
	unset_user();
	return "<form method='post' action='adream_main.php' enctype='multipart/form-data'>".
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
	$salt1 = bin2hex(random_bytes(SALT));
	$salt2 = bin2hex(random_bytes(SALT));
	$token = hash('ripemd128', $salt1.$pw.$salt2);
	$query = "INSERT INTO accounts VALUES('$id', '$token', '$fname', '$lname', '$salt1', '$salt2')";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	return MSG_SIGNUP;
}

function auth_user($conn, $id, $pw) {
	$query = "SELECT * FROM accounts WHERE id='$id'";
	$result = $conn->query($query);
	if (!$result) die(to_main(ERR_DB));
	elseif ($result->num_rows) {
		$row = $result->fetch_array(MYSQLI_NUM);
		$result->close();
		$salt1 = $row[4];
		$salt2 = $row[5];
		$token = hash('ripemd128', $salt1.$pw.$salt2);
		if ($token == $row[1]) {
			set_user(ucfirst($row[2]).' '.ucfirst($row[3]), $row[0]);
			return true;
		}
	}
	return false;
}

function set_user($name, $user) {
	setcookie('login_auth', $name, time() + 3600 * 24, '/');
	setcookie('login_user', $user, time() + 3600 * 24, '/');
}

function unset_user() {
	setcookie('login_auth', '', time() - 3600 * 24 * 30, '/');
	setcookie('login_user', '', time() - 3600 * 24 * 30, '/');
}

function isset_user() {
	if (!isset($_COOKIE['login_auth']))
		return LOGIN_NO;
	else return LOGIN_USER;
}



function add_post($conn, $user, $text, $img) {
	$query = "INSERT INTO posts(user, text, img) VALUES('$user', '$text', '$img')";
	return $conn->query($query);
}

function print_post($conn) {
	$user = $_COOKIE['login_user'];
	$query = "SELECT * FROM posts WHERE user='$user'";
	if (!($result = $conn->query($query))) die(to_main(ERR_DB));
	if ($result->num_rows == 0) {
		echo "<h3 style='text-indent:100px;'>No posts</h3>";
		return;
	}
	echo <<<_END
		<h3 style='text-indent:100px;'>Posts</h3>
		<table border="1" cellpadding="5" style="margin-left: 100px; border-collapse: collapse;
		text-align: center; text-valign: center;">
			<tr>
				<th>Image</th>
				<th>Text</th>
			</tr>
_END;
	for ($j = 0; $j < $result->num_rows; ++$j) {
		$result->data_seek($j);
		$row = $result->fetch_array(MYSQLI_NUM);
		echo '<tr>';
		echo '<td>'.'<img src="'.$row[3].'"></td>';
		echo '<td>'."$row[2]".'</td>';
	}
	echo '</table>';
}



function html_header() {
	echo <<<_END
<html>
	<head>
		<title>Final</title>
	</head>
	<body>
		<h1>
			CS174 - Final <br>
			Mingyun Kim <br>
			Zihui Chen <br>
		</h1>
		<h2>This Website Requires Cookies</h2>
		<hr>
		<h1 style="text-indent:165px;">Ari's Dream</h1>
_END;
}

function html_footer() {
	echo <<<_END
	</body>
</html>
_END;
}

function html_login() {
	echo <<<_END
			<h2 style="text-indent:250px;">Login</h2>
		<form method='post' action='adream_main.php' enctype='multipart/form-data'>
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
	<form method='post' action='adream_main.php' enctype='multipart/form-data'>
		<input type='submit' name='logout' value='Sign Out'>
	</form>
	</div>
_END;
}

function html_register() {
	echo <<<_END
			<h2 style="text-indent:235px;">Register</h2>
		<form method='post' action='adream_main.php' enctype='multipart/form-data'>
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

function html_post() {
	echo <<<_END
		<h2 style="text-indent:120px;">Upload a file/text to post</h2>
		<form method='post' action='adream_main.php' enctype='multipart/form-data'>
			<pre>
		Text: <input type='text' size='45' maxlength='32' id='text' name='text'><br>
		      <input type='file' name='filename'><input type='submit' id='post' name='post' value='Post'>
			</pre>
		</form>
_END;
}

?>
