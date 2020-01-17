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
document.getElementById('sort').addEventListener('click', bubbleSort);

const canv = document.querySelector('canvas');
canv.width = WIDTH;
canv.height = HEIGHT;
c = canv.getContext('2d');

const FILL_COLOR = 'blue';
const HIGHLIGHT_COLOR = 'red';
const TEXT_COLOR = 'black';




//let left = new Number(20, 50, 100, 500);
//let right = new Number(20, 50, 200, 500);
//right.draw();
//left.draw();

let min = 10;
let max = 100;
let numbers = 10;

let speed = 7;
let low = null;
let high = null;
let goalX = null;




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

function _bubbleSort() {
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



class BubbleStates {

    constructor(arr) {
        this.arr = arr;
        this.states = this.getIterations(this.arr);
        this.n = arr.length - 1;
        this.i = 0;
        this.k = 0;
        this.finished = false;

        this.rect1 = this.arr[this.i];
        this.rect2 = this.arr[this.k];
        this.oldRect1 = this.rect1;
        this.oldRect2 = this.rect2;
        this.compare = false;

    }

    async advance() {

        /* The algorithm bellow updates the variables i & k accordingly:
                    for i = 0; i < n - 1; i++ 
                        for k = i + 1; k < n; k++
        */
        if (!this.finished) {

            if (this.compare) {
                if (this.rect2.value < this.rect1.value) {
                    await this.switchPos();
                }
                this.compare = false;
            } 
            else {
                if (this.i < this.n) {
                    this.oldRect2 = this.rect2;
        
                    if (this.k == this.n) {
                        this.oldRect1 = this.rect1;
                        this.i++;
                        this.k = this.i + 1;
                    }
                    else {
                        this.k++;
                    }
                } 
                else {
                    this.finished = true;
                    return;
                }
                this.compare = true;
            }

            this.oldRect1.unHighlight();
            this.oldRect2.unHighlight();

            this.rect1 = this.arr[this.i];
            this.rect2 = this.arr[this.k];
            this.rect1.highlight();
            this.rect2.highlight();
            c.stroke();
       }

    }

    async switchPos() {
        this.arr[this.i] = this.rect2;
        this.arr[this.k] = this.rect1;
        low = this.rect1.x < this.rect2.x ? this.rect1 : this.rect2;
        high = this.rect1.x > this.rect2.x ? this.rect1 : this.rect2;
        goalX = high.x;
        animate();
    }

    /* The total ammount of iterations is given by
    (n - 1) + Sum^n_k=1 (n - k)              */
    getIterations(arr) {
        let n = arr.length;
        let iterations = n - 1;
        for (let k = 1; k < n; k++) {
            iterations += n - k;
        }
        return iterations;
    }

}

class Number {

    constructor(value, width, x, y) {
        this.value = value;
        this.width = width;
        this.height = -this.value * 3;
        this.x = x;
        this.y = y;
        this.highlighted = false;
    }

    highlight() {
        this.highlighted = true;
        this.draw();
    }

    unHighlight() {
        this.highlighted = false;
        this.draw();
    }

    draw() {
        c.beginPath();
        c.fillStyle = this.highlighted ? HIGHLIGHT_COLOR : FILL_COLOR;
        c.fillRect(this.x, this.y, this.width, this.height);

        c.fillStyle = TEXT_COLOR;
        c.textAlign = 'center';
        c.font = '20px Courier';
        c.fillText(this.value, this.x + (this.width / 2), 
                this.y + (this.height / 2));
        c.stroke();
    }
}

function animate(timestamp) {
    let id = requestAnimationFrame(animate);
    if (low.x >= goalX) {
        cancelAnimationFrame(id);
    }
    else {
        low.x += speed;
        high.x -= speed;
        c.clearRect(0, 0, WIDTH, HEIGHT);
        for (const rect of arr) {
            rect.draw();
        }
        c.stroke();
    }
}



let arr = createArray(min, max, numbers);
display();
let bubble = new BubbleStates(arr);

function ad() {
    bubble.advance();
}




document.getElementById('debug').addEventListener('click', ad);

function display() {
    c.clearRect(0, 0, WIDTH, HEIGHT);
    for (const rectangle of arr) {
        rectangle.draw();
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

async function bubbleSort() {
    for (i = 0; i < 10; i++) {
        bubble.advance();
    }   
}
