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

const WIDTH = 700;
const HEIGHT = 500;

document.querySelector('button').addEventListener('click', shuffle);
document.getElementById('debug').addEventListener('click', animate);

const canv = document.querySelector('canvas');
canv.width = WIDTH;
canv.height = HEIGHT;
c = canv.getContext('2d');

const FILL_COLOER = 'blue';
const BORDER_COLOR = 'black';
const HIGHLIGHT_COLOR = 'red';
const TEXT_COLOR = 'black';

let min = 10;
let max = 100;
let numbers = 20;

//let arr = createArray(min, max, numbers);
//display();


class BubbleSort {
    /* 
    */
    FINAL_STATE = 10;
    
    constructor() {
        this.state = 0;
    }

    next() {
        this.state = (this.state < FINAL_STATE) ? this.state + 1 : 0;
    }

}



let left = new Number(20, 50, 100, 500);
let right = new Number(20, 50, 200, 500);

right.draw();
left.draw();

let speed = 2;
let low = null;
let high = null;
let goalX = null;


function animate(timestamp) {
    let id = requestAnimationFrame(animate);
    if (low.x >= goalX) {
        cancelAnimationFrame(id);
    }
    else {
        low.x += speed;
        high.x -= speed;
        c.clearRect(0, 0, WIDTH, HEIGHT);
        low.draw();
        high.draw();
    }
}

function switchPos(rect1, rect2) {
    low = rect1.x < rect2.x ? rect1 : rect2;
    high = rect1.x > rect2.x ? rect1 : rect2;
    goalX = high.x;
    animate();
}

//switchPos(left, right);


function shuffle() {
    for (i = arr.length - 1; i > 0; i--) {
        let randomNumber = Math.floor(Math.random() * (i));
        let temp = arr[i].x;
        arr[i].x = arr[randomNumber].x;
        arr[randomNumber].x = temp;
    }
    display();
}

function bubbleSort() {
    for (i = 0; i < arr.length - 1; i++) {
        arr[i].mark();

        for (k = i + 1; k < arr.length; k++) {
            arr[k - 1].unmark();
            arr[k].mark()

            if (arr[k].value < arr[i].value) {
                let temp = arr[i];

                arr[i] = arr[k];            // SWITCH
                arr[k] = arr[i];            // SWITCH
            }
        }

        arr[i].unmark();
    }
}

/* The total ammount of iterations is given by
   (n - 1) + Sum^n_k=1 (n - k)              */
function getIterations(arr) {
    let n = arr.length;
    let iterations = n - 1;
    for (k = 1; k < n; k++) {
        iterations += n - k;
    }
    return iterations;
}

class BubbleStates {

    constructor(arr) {
        console.log("Hey");
        this.states = getIterations(arr);
        
        console.log(this.states);
    }

}

let arr = [];
for (i = 0; i < 30; i++) {
    arr.push(3);
}
console.log(getIterations(arr));

function display() {
    c.clearRect(0, 0, WIDTH, HEIGHT);
    for (const rectangle of arr) {
        rectangle.draw();
    }
}

function Number(value, width, x, y) {
    this.value = value;
    this.width = width;
    this.height = -this.value * 3;
    this.x = x;
    this.y = y;
    this.marked = false;
    
    this.draw = function() {
        c.beginPath();
        c.fillStyle = FILL_COLOER;
        c.fillRect(this.x, this.y, this.width, this.height);
        c.lineWidth = 3;
        c.strokeStyle = this.marked ? HIGHLIGHT_COLOR : BORDER_COLOR;
        c.strokeRect(this.x, this.y, this.width, this.height);

        c.fillStyle = TEXT_COLOR;
        c.textAlign = 'center';
        c.font = '20px Courier';
        c.fillText(this.value, this.x + (this.width / 2), 
                this.y + (this.height / 2));
        c.stroke();
    }
}

function createArray(min, max, numbers) {
    let arr = [];
    for (i = 0; i < numbers; i++) {
        let randomNumber = min + Math.floor(Math.random() * (max - min)) + 1;
        arr.push(new Number(randomNumber, WIDTH / numbers, i * WIDTH / numbers, HEIGHT));
    }
    return arr;
}