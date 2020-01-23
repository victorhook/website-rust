/* With canvas you can draw: 
    - Rectangles
    - Lines
    - Arcs / Circles
    - Bezier Curves
    - Images
    - Text
*/

//var canvas = document.querySelector('canvas');
//canvas.width = window.innerWidth;
//canvas.height = window.innerHeight;
//var c = canvas.getContext('2d');

/* -- Rectangles x, y, width, height -- 
c.fillStyle = 'rgba(255, 0, 0, 0.5)'
c.fillRect(100, 100, 50, 50);
c.fillStyle = 'rgba(0, 255, 0, 0.5)'
c.fillRect(300, 100, 50, 50);
*/

/* -- Line --
c.beginPath();
c.moveTo(50, 300);
c.lineTo(300, 300);
c.strokeStyle = "#000000";
c.lineTo(300, 200);
c.strokeStyle = "#555555";
c.stroke();
*/

/* -- Arc / Circle --
for (i = 0; i < 100; i++) {
    var x = window.innerWidth * Math.random();
    var y = window.innerHeight * Math.random();
    var r = Math.random() * 255;
    var g = Math.random() * 255;
    var b = Math.random() * 255;
    c.strokeStyle = 'rgba(' + r + ", " + g + ',' + b + ', 0.8';
    c.beginPath();
    c.arc(x, y, 15, 0, Math.PI * 2, false);
    c.stroke();
}
*/

/*

function Circle(x, y, dx, dy, radius) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.radius = radius;

    this.draw = function() {
        c.beginPath();
        c.arc(this.x, this.y, this.radius, Math.PI * 2, false);
        c.strokeStyle = 'blue';
        c.stroke();
    }

    this.update = function() {

        if (this.x + this.radius >= innerWidth || this.x - this.radius <= 0) {
            this.dx = -this.dx;
        }

        if (this.y + this.radius >= innerHeight || this.y - this.radius <= 0) {
            this.dy = -this.dy;
            
        }
        this.x += this.dx;
        this.y += this.dy;
    }
}
*/
/*
for (i = 0; i < 20; i++) {
    let x = innerWidth * Math.random();
    let y = innerHeight * Math.random();
    let dx = SPEED * (.5 - Math.random());
    let dy = SPEED * (.5 - Math.random());
    circles.push(new Circle(x, y, dx, dy, RADIUS));
}

function animate() {
    requestAnimationFrame(animate);
    c.clearRect(0, 0, innerWidth, innerHeight);
    for (const circle of circles) {
        circle.draw();
        circle.update();
    }
}
*/