<?PHP
/*	CS174 - Midterm 2: Online Virus Check
 *	Mingyun Kim
 *	Zihui Chen
 */

require_once 'vcheck_lib.php';

html_header();
$curr_user = isset_user();
$done = "";


/*	Upload Handler
 */

if ($_FILES) {
	$fname = $_FILES['filename']['name'];
	move_uploaded_file($_FILES['filename']['tmp_name'], $fname);
	if (!file_exists($fname)) die(to_main(ERR_INPUT));
	if (isset($_POST['check']))
		$done = check_mw($conn, $fname);
	else if (isset($_POST['addmw'])) {
		if (empty($mw = mysql_entities_fix_string($conn, $_POST['mwname'])))
			die(to_main(ERR_INPUT));
		$done = add_mw($conn, $fname, $mw);
	}
	else if (isset($_POST['rqf'])) {
		if (empty($mw = mysql_entities_fix_string($conn, $_POST['reqmw'])))
			die(to_main(ERR_INPUT));
		$done = request_mw($conn, $fname, $mw);
	}
	unlink($fname);
}


/*	Login Handler
 */

if (isset($_POST['logout'])) die(to_main("Successfully Signed Out<br>"));
if (isset($_POST['login'])) login_user($conn);
if (isset($_POST['reg'])) register_user($conn);


/*	Malware Handler
 */

if (isset($_POST['deletemw'])) $done = delete_mw($conn);
if (isset($_POST['acceptreq'])) $done = accept_mw($conn);
if (isset($_POST['rejectreq'])) $done = reject_mw($conn);


/*	Main Page
 */

html_checker();

if ($curr_user == LOGIN_NO) {
	html_login();
	html_register();
}
elseif ($curr_user == LOGIN_ADMIN) {
	echo "<h2 style='text-indent:100px;'>Administration Control</h2>";
	html_add();
	manage_mw($conn);
	reqs_mw($conn);
	echo '<br>';
	html_logout();
}
elseif ($curr_user == LOGIN_USER) {
	$username = $_COOKIE['login_auth'];
	echo "<h2 style='text-indent:100px;'>Welcome, $username!</h2>";
	html_request();
	echo '<br>';
	html_logout();
}

echo '<hr>';


/*	Result
 */

echo $done;

$conn->close();
print_footer();
?>
