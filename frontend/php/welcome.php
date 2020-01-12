<?php
// PHP FUNCTION YOU WANT TO CALL
function save ($name, $email) {
  // Do your processing
  // Save to database of something
  return true;
}

// PUT THE POST VARIABLES IN
$pass = save($_POST['name'], $_POST['email']);

// RESULT
echo json_encode([
  "status" => $pass ? 1 : 0,
  "message" => $pass ? "OK" : "An error has occured"
]);
?>