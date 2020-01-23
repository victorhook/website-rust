/* -------- CONSTANS ---------- */

const WIDTH = 700;
const HEIGHT = 500;
const FILL_COLOR = 'gray';
const HIGHLIGHT_COLOR = 'red';
const TEXT_COLOR = 'black';
const SPEED = 40;
const ANIMATION_SPEED_LIMIT = 60;
const DEFAULT_DELAY = 50;

const bubbleSortJavaText = "for (int i = 0; i < n - 1; i++) {\n" + 
                            "    for (int j = i + 1; j < n; j++) {\n" + 
                            "        if (array[j] < array[i]) {\n" + 
                            "            int temp = array[i];\n" + 
                            "            array[i] = array[j];\n" + 
                            "            array[j] = temp;\n" + 
                            "        }\n" + 
                            "    }\n" + 
                            "}";

/* Adding event-handlers */
document.querySelector('button').addEventListener('click', shuffle);
document.getElementById('sort').addEventListener('click', bubbleSort);

const sizeSlider = document.getElementById('sizeSlider');
sizeSlider.addEventListener('change', sizeSliderCallback);

const valueSlider = document.getElementById('valueSlider');
valueSlider.addEventListener('change', valueSliderCallback);

const speedSlider = document.getElementById('speedSlider');
speedSlider.addEventListener('change', speedSliderCallback);

//document.getElementById('debug').addEventListener('click', ad);

/* canv is the main object for painting and the variable 'c'
   is the 2D-context which is used for updating graphics. */
const canv = document.querySelector('canvas');
canv.width = WIDTH;
canv.height = HEIGHT;
c = canv.getContext('2d');

/* Setting textbox values, temporary ATM */
const textBox = document.querySelector('textarea');
textBox.rows = 10;
textBox.cols = 50;
textBox.value = bubbleSortJavaText;
textBox.disabled = true;

/* -------- GLOBAL VARIABLES ---------- */
let min = 10;                 // Minimum value for a number
let max = 100;                // Maximum value for a number
let arraySize = sizeSlider.value;   // Total number of items in the array.
let numberTextSize = 20;

// The speed of how fast the switching of two object occurs
let speed = speedSlider.value;
speed = 40;
// Animation delay
let delay = DEFAULT_DELAY;

// low, high and goalX are used when two objects switch place.
// There must indeed be a smoother way for this, but it works forn now 
let low = null;
let high = null;
let goalX = null;

/* ------- CLASSES --------- */

class BubbleStates {
    // This is quite a temporary fix, but the class is responsible for
    // behaving like a bubblesort. It works like a State machine, where
    // each "advance()" moves the algorithm one step forward.
    
    // TODO: Generalize this with a static class or something like that
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

        // There's two states:
        //  1. Comparing two values
        //  2. Switching positiong of two values
        
        if (this.compare) {
            if (this.rect2.value < this.rect1.value) {
                await this.switchPos();     // Switch position of the numbers! 
                
            }
            this.compare = false;           // Change state
        } 
        else {
            if (this.i < this.n) {
                this.oldRect2 = this.rect2;
    
                if (this.k == this.n) {          // The inner loop has reached its end
                    this.oldRect1 = this.rect1;
                    this.i++;
                    this.k = this.i + 1;        // Increase the outer loop by 1, reset inner to
                }                               // outer + 1
                else {
                    this.k++;                   // Increment inner loop like normal
                }
            } 
            this.compare = true;                // Change state
        }

        // "Un-Highlights" de old values and highlights the newly selected ones
        if (this.i < this.n) {                  
            this.oldRect1.unHighlight();
            this.oldRect2.unHighlight();

            this.rect1 = this.arr[this.i];
            this.rect2 = this.arr[this.k];
            this.rect1.highlight();
            this.rect2.highlight();
            c.stroke();
        } 
        else {  // Sorting is done
            this.finished = true;
        }
    }

    /* Switches position of two values in the array */
    async switchPos() {
        this.arr[this.i] = this.rect2;
        this.arr[this.k] = this.rect1;
        low = this.rect1.x < this.rect2.x ? this.rect1 : this.rect2;
        high = this.rect1.x > this.rect2.x ? this.rect1 : this.rect2;
        goalX = high.x;

        /* There's a limit of where the animation wont look nice if there's
           too many values or the speed is too high. In this case, the objects
           simply switch place instantly instead of doing a moving animation */
        if (speed >= ANIMATION_SPEED_LIMIT) {
            high.x = low.x;
            low.x = goalX;
            display();
        } else {
            animate();
        }
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

    /* Each number is represented by its value and the height is picked accordingly.
       The rectangles is given an absolute x & y -value which it's drawned relative to.
       This makes moving the rectangles very easy */
    constructor(value, width, x, y) {
        this.value = value;
        this.width = width;
        this.height = -this.value * 3;
        this.x = x;
        this.y = y;
        this.highlighted = false;
    }

    /* Marks the number */
    highlight() {
        this.highlighted = true;
        this.draw();
    }

    /* Unmarks the number */
    unHighlight() {
        this.highlighted = false;
        this.draw();
    }

    /* Draws the rectangle with the appropiate color (depending on if it's
       highlighted or not, and the correct value                        */
    draw() {
        c.beginPath();
        c.fillStyle = this.highlighted ? HIGHLIGHT_COLOR : FILL_COLOR;
        c.fillRect(this.x, this.y, this.width, this.height);

        c.fillStyle = TEXT_COLOR;
        c.textAlign = 'center';
        c.font = numberTextSize + 'px Courier';
        c.fillText(this.value, this.x + (this.width / 2), 
                this.y + (this.height / 2));
        c.stroke();
    }
}

/* ---------- FUNCTIONS ----------------- */

/* Shuffles the array in random order */
function shuffle() {
    for (i = arr.length - 1; i > 0; i--) {
        let randomNumber = Math.floor(Math.random() * (i));
        let temp = arr[i].x;
        arr[i].x = arr[randomNumber].x;
        arr[randomNumber].x = temp;
    }
    display();
}

/* Performs switching animation of two objects */
function animate(timestamp) {

    let id = requestAnimationFrame(animate);
    if (low.x >= goalX) {
        cancelAnimationFrame(id);
    }
    else {
        // Correction if the distance to move is less than the speed (step size)
        speed = goalX - low.x < speed ? goalX - low.x : SPEED;
        
        low.x += speed;
        high.x -= speed;
        display();
    }
}

/* Clears the canvas and repaints */
function display() {
    c.clearRect(0, 0, WIDTH, HEIGHT);       // Clears the canvas
    for (const rect of arr) {
        rect.draw();                        // Re-draws all the objects
    }
    c.stroke();
}

/* Creates an array with random nubmers of [MIN - MAX], numbers size */
function createArray(min, max, numbers) {
    let arr = [];
    for (i = 0; i < numbers; i++) {
        let randomNumber = min + Math.floor(Math.random() * (max - min)) + 1;
        arr.push(new Number(randomNumber, WIDTH / numbers, i * WIDTH / numbers, HEIGHT));
    }
    return arr;
}

/* Runs the bubbleSort algorithm */
function runBubblesort() {
    if (bubble.finished) {
        clearInterval(id);
    } else {
        bubble.advance();
    }
}

/* Updates the array of numbers */
function updateArray() {
    arr = createArray(min, max, arraySize);
    numberTextSize = 20 - arraySize / 5;
    bubble = new BubbleStates(arr);
    display();
}

/* Callback from button. 
    TODO: Need generilasation */
function bubbleSort() {
    id = setInterval(runBubblesort, delay);
}


/* Callback functions from input */
function valueSliderCallback() {
    max = valueSlider.value;
    updateArray();
}

function sizeSliderCallback() {
    arraySize = sizeSlider.value;
    updateArray();
}

function speedSliderCallback() {
    speed = speedSlider.value;
    delay = DEFAULT_DELAY - speed * 1.5;
    console.log(speed);
}


/* Init */
let id;
let arr = createArray(min, max, arraySize);
let bubble = new BubbleStates(arr);
display();
