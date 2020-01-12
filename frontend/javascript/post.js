const BACKEND_SERVER = '../../src/main.rs';
const END_OF_MSG = "\r\n\r\n";

const btn = document.getElementById('submit_btn');
btn.addEventListener('click', submit);

function submit(event) {
    
    var name = document.getElementById('name').value;
    var email = document.getElementById('email').value;
    var message = document.getElementById('message').value;

    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        /* Callback from server that everything went fine */
        if (req.readyState == 4 && req.status == 200) {
            
        }
    };
    /* Sends the data do the backend server */
    req.open('POST', BACKEND_SERVER, true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send("name=" + name + "&email=" + email + 
            "&message=" + message + END_OF_MSG);
}

