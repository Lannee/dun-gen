window.onload = function () {
    drawBeginnigGraph("canvas")
}

// const HIDDEN_X = document.getElementById("hidden-form:hidden-x");
// const HIDDEN_Y = document.getElementById("hidden-form:hidden-y");
// const HIDDEN_R = document.getElementById("hidden-form:hidden-r");
// const HIDDEN_BUTTON = document.getElementById("hidden-form:hidden-send");

export function drawBeginnigGraph(canvasIdStr) {
    let canvas = document.getElementById(canvasIdStr)

    if (canvas === null)
        return false;

    let ctx = canvas.getContext('2d');
    
    ctx.globalCompositeOperation = "source-over";

    ctx.beginPath();

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // setting blue color
    ctx.strokeStyle = "#3399ff";
    ctx.fillStyle = "#3399ff";

    // filling area
    // rectangle
    ctx.fillRect(canvas.width / 2, canvas.height / 2, -canvas.width / 3, -canvas.height / 6);
    ctx.moveTo(canvas.width / 2, canvas.height / 2);
    // 1/4 of circle
    ctx.arc(canvas.width / 2, canvas.height / 2, canvas.width / 6, -1.57, 0, 0);
    ctx.fill();
    // triangle
    ctx.moveTo(canvas.width / 2, canvas.height / 2);
    ctx.lineTo(canvas.width / 2, canvas.height / 1.5);
    ctx.lineTo(canvas.width / 6, canvas.height / 2);
    ctx.lineTo(canvas.width / 2, canvas.height / 2);
    ctx.fill();

    // setting black color
    ctx.fillStyle = "black";
    ctx.font = "12px serif";

    // vertical 'R' marks
    ctx.fillText("R", canvas.width / 1.95, canvas.height / 5.5);
    ctx.fillText("R/2", canvas.width / 1.93, canvas.height / 2.85);
    ctx.fillText("-R/2", canvas.width / 1.93, canvas.height / 1.475);
    ctx.fillText("-R", canvas.width / 1.93, canvas.height / 1.184);

    // horizontal 'R' marks
    ctx.fillText("-R", canvas.width / 6.7, canvas.height / 2.05);
    ctx.fillText("-R/2", canvas.width / 3.38, canvas.height / 2.05);
    ctx.fillText("R/2", canvas.width / 1.59, canvas.height / 2.05);
    ctx.fillText("R", canvas.width / 1.23, canvas.height / 2.05);
    ctx.stroke();

    ctx.beginPath();
    ctx.strokeStyle = "black";
    // graph arrows
    // vertical arrow
    ctx.moveTo(canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2, canvas.height);
    ctx.moveTo(canvas.width / 1.95, canvas.height * 0.03);
    ctx.lineTo(canvas.width / 2, 0);
    ctx.moveTo(canvas.width / 2.05, canvas.height * 0.03);
    ctx.lineTo(canvas.width / 2, 0);
    // horizontal arrow
    ctx.moveTo(0, canvas.height / 2);
    ctx.lineTo(canvas.width, canvas.height / 2);
    ctx.moveTo(canvas.width * 0.97, canvas.height / 1.95);
    ctx.lineTo(canvas.width, canvas.height / 2);
    ctx.moveTo(canvas.width * 0.97, canvas.height / 2.05);
    ctx.lineTo(canvas.width, canvas.height / 2);

    // R unit marks
    // vertical marks
    ctx.moveTo(canvas.width / 2.04, canvas.height / 1.20);
    ctx.lineTo(canvas.width / 1.96, canvas.height / 1.20);
    ctx.moveTo(canvas.width / 2.04, canvas.height / 1.5);
    ctx.lineTo(canvas.width / 1.96, canvas.height / 1.5);
    ctx.moveTo(canvas.width / 2.04, canvas.height / 3);
    ctx.lineTo(canvas.width / 1.96, canvas.height / 3);
    ctx.moveTo(canvas.width / 2.04, canvas.height / 6);
    ctx.lineTo(canvas.width / 1.96, canvas.height / 6);

    // horizontal marks
    ctx.moveTo(canvas.width / 1.2, canvas.height / 2.04);
    ctx.lineTo(canvas.width / 1.2, canvas.height / 1.96);
    ctx.moveTo(canvas.width / 1.5, canvas.height / 2.04);
    ctx.lineTo(canvas.width / 1.5, canvas.height / 1.96);
    ctx.moveTo(canvas.width / 3, canvas.height / 2.04);
    ctx.lineTo(canvas.width / 3, canvas.height / 1.96);
    ctx.moveTo(canvas.width / 6, canvas.height / 2.04);
    ctx.lineTo(canvas.width / 6, canvas.height / 1.96);
    ctx.stroke();
}

function clearCanvas() {
    let canvas = document.getElementById("canvas"),
        ctx = canvas.getContext('2d');

    ctx.clearRect(0, 0, canvas.width, canvas.width)
    drawBeginnigGraph("canvas")
}

// document.getElementById("canvas").addEventListener("click", function (evt) {
//     cavas = document.getElementById("canvas")
//     const rect = canvas.getBoundingClientRect();
//     const userX = evt.clientX - rect.left - canvas.width / 2;
//     const userY = (evt.clientY - rect.top - canvas.height / 2) * -1;

//     triggerRequest({
//         x: userX,
//         y: userY
//     });
// })

