import axeSrc from '../img/axe.png';
import fissureSrc from '../img/fissure.png';
import { drawBeginnigGraph } from "./canvas"

export function drawAnimatedRotatedPoint(xValue, yValue, rValue, imgHeight, imageSrc, offsetX, offsetY, rotationAngle, ctx) {
    let canvas = document.getElementById("canvas");

    let canvas1 = document.getElementById("canvas1"),
        ctx1 = canvas1.getContext('2d');

    let img = new Image()
    img.onload = function () {
        ctx1.clearRect(0, 0, canvas.width, canvas.height);
        let xOnCanvas = canvas.width / 2 + canvas.width / 3 * (xValue / rValue)
        let yOnCanvas = canvas.width / 2 - canvas.width / 3 * (yValue / rValue)

        let imageScale = imgHeight / img.height
        ctx.save()
        ctx.translate(xOnCanvas, yOnCanvas); // move to point

        ctx.setTransform(imageScale, 0, 0, imageScale, xOnCanvas, yOnCanvas); // sets scale and origin

        ctx.rotate(rotationAngle);
        ctx.drawImage(img, -img.width / 2 - offsetX, -img.height / 2 - offsetY);
        ctx.restore()
    };
    img.src = imageSrc
}

export function drawPoint(xValue, yValue, rValue, isHit, imageSrc, rotationAngle, canvasID, isAnimated) {
    let axeHeight = 40 // need to fix
    let fissureHeight = 25 // need to fix

    let canvas = document.getElementById(canvasID),
        ctx = canvas.getContext('2d');

    if (axeSrc == imageSrc) {
        if (isAnimated) {
            drawAnimatedRotatedPoint(xValue, yValue, rValue, axeHeight, imageSrc, 0, 0, rotationAngle, ctx, isAnimated)
        } else {
            drawAnimatedRotatedPoint(xValue, yValue, rValue, axeHeight, imageSrc, 0, 0, rotationAngle, ctx, isAnimated)
        }
    } else {
        drawAnimatedRotatedPoint(xValue, yValue, rValue, fissureHeight, imageSrc, - fissureHeight * 5, - fissureHeight * 5, rotationAngle, ctx, isAnimated)
    }
}

export function drawIsHitPoint(xValue, yValue, rValue, isHit, permanentCanvasID, animatedCanvasID) {
    let rValueScale = 3
    let xValueRotate = xValue - rValue * rValueScale

    let frameCount = 80
    let frameRestCount = frameCount
    let angle = 0
    let rotation

    setTimeout(drawThrow)

    function drawThrow() {
        angle = (Math.random() * (23 - 2) + 2 + angle) % 360
        rotation = angle * Math.PI / 180

        drawPoint(xValueRotate, yValue, rValue, isHit, axeSrc, rotation, animatedCanvasID, true)

        frameRestCount--
        xValueRotate = xValueRotate + rValue / frameCount * rValueScale
        if (frameRestCount != 0) {
            setTimeout(drawThrow, 10)
        } else {
            drawPoint(xValue, yValue, rValue, isHit, fissureSrc, rotation, permanentCanvasID, false)
            drawIsHit()
        }
    }

    function drawIsHit() {
        let yValueRotate = yValue
        if (isHit) {
            drawPoint(xValueRotate, yValue, rValue, isHit, axeSrc, rotation, permanentCanvasID, false)
        } else {
            frameRestCount = frameCount
            setTimeout(drawNotHit)
        }

        function drawNotHit() {
            xValueRotate -= rValue / frameCount * rValueScale / 2
            yValueRotate -= ((xValue - xValueRotate) ** 2) / frameCount * 4

            angle = (Math.random() * (23 - 2) + 2 + angle) % 360
            let rotation = angle * Math.PI / 180
            drawPoint(xValueRotate, yValueRotate, rValue, isHit, axeSrc, rotation, animatedCanvasID, true)

            frameRestCount--
            if (frameRestCount != 0) {
                setTimeout(drawNotHit, 10)
            } else {
                let canvas = document.getElementById("canvas1"),
                    ctx1 = canvas.getContext('2d');

                ctx1.clearRect(0, 0, canvas.width, canvas.height);
            }
        }
    }
}