const canvas = document.getElementById("myCanvas");
const ctx = canvas.getContext("2d");
// рисуем заполненные фигуры
ctx.fillStyle = "rgb(77, 151, 247)"
ctx.fillRect(100, 150, 50, 100)

ctx.beginPath();
ctx.moveTo(50, 150)
ctx.lineTo(150, 150)
ctx.lineTo(150, 50)
ctx.lineTo(50, 150)
ctx.closePath();
ctx.fill()

ctx.beginPath();
ctx.arc(150, 150, 100, 0, -Math.PI/2, true)
ctx.lineTo(150, 150)
ctx.fill();


// рисуем оси и стрелки
ctx.fillStyle = "black"

ctx.beginPath()
ctx.moveTo(0,150);
ctx.lineTo(300,150);
ctx.lineTo(290,147);
ctx.moveTo(300,150);
ctx.lineTo(290, 153);
ctx.moveTo(150,300);
ctx.lineTo(150,0);
ctx.lineTo(147,10);
ctx.moveTo(150,0);
ctx.lineTo(153,10);
// рисуем пометки на абсцисе (Шаг пометок = 50)
ctx.moveTo(153,50);
ctx.lineTo(147,50);

ctx.moveTo(153,100);
ctx.lineTo(147,100);

ctx.moveTo(153,200);
ctx.lineTo(147,200);

ctx.moveTo(153,250);
ctx.lineTo(147,250);
// рисуем пометки на ординате
ctx.moveTo(50,147);
ctx.lineTo(50,153);

ctx.moveTo(100,147);
ctx.lineTo(100,153);

ctx.moveTo(200,147);
ctx.lineTo(200,153);

ctx.moveTo(250,147);
ctx.lineTo(250,153);

// Добавляем обозначения
ctx.font="12px Serif"
ctx.fillText("x", 293, 145)
ctx.fillText("y", 160, 10)
ctx.fillText("R", 248, 145)
ctx.fillText("R/2", 193, 145)
ctx.fillText("-R/2", 90, 145)
ctx.fillText("-R", 48, 145)

ctx.fillText("R", 160, 55)
ctx.fillText("R/2", 160, 105)
ctx.fillText("-R/2", 160, 205)
ctx.fillText("-R", 160, 255)

ctx.stroke();

function clock() {
  const clock = document.getElementById("clock");
  const time = get_time();
  clock.innerHTML = time;
  setTimeout("clock()", 1000);
}
clock();



if (localStorage.length > 0) {
  const table = document.getElementById("tbodyShot");
  for (let i = 1; i <= localStorage.length; i++) {
    // let key = localStorage.key(i)
    let row = localStorage.getItem(String(i));
    table.innerHTML += `${row}`
  }
}



for (let i=-5; i<=3; i++) {
  const x = document.getElementById("x");
  if (i === 0) {x.innerHTML += `<input type="button" id="${i}x" class="x" name="x" value="${i}" onclick="set_x(this)" required checked>
        `;}
  else{
  x.innerHTML += `<input type="button" id="${i}x" class="x" name="x" value="${i}" onclick="set_x(this)" required>
    `;}
}

function clear() {
  localStorage.clear();
  // alert("Таблицы очищена")
  return true;
}

// function remove(key) {
//   if (key > 0 && key <= num_row){
//     localStorage.removeItem(String(key))
//     alert(`Удалён элемент ${key}`)
//     num_row = num_row -1
//     return true;
//   }
//   alert("Номер строки не найден")
//   return false;
// }


function validate() {
  const y = document.getElementById("y")
  const error = document.getElementById("error");

  const raw = y.value.trim().replace(",", ".");
  if (!/^-?\d+((\.)\d+)?$/.test(raw)) {
    error.innerHTML = "<b>Введены некоректные данные в поле Y</b>";
    return false;
  }
  const val = Number(raw);
  if (val < -3 || val > 3) {
    error.innerHTML = "<b>Значение лежит вне указанного диапозона</b>";
    return false;
  }
  else {
    error.innerHTML = "";
    return true;}
}

let x = 0
const text_x = document.getElementById("text_x");
text_x.innerHTML = `0`;
function set_x(key){x = Number(key.value);
text_x.innerHTML = `${key.value}`;
}

function shot() {
  const y = Number(document.getElementById("y").value.replace(",", "."));
  const R = Number(document.getElementById("R").value);
  const status = document.getElementById("status");
  if (x === -5 || x === -4 || x === -3 || x === -3 || x === -2 || x === -1 || x === 0 || x === 1 || x === 2 || x === 3) {
    if ((x >= 0 && y >= 0) && (x ** 2 + y ** 2 <= R ** 2)) {
      status.innerHTML = "Попал!"
      tableShot(x, y, R, true)
      return true;
    } else if ((x <= 0 && y >= 0) && (y <= x + R)) {
      status.innerHTML = "Попал!"
      tableShot(x, y, R, true)
      return true;
    } else if ((x <= 0 && y <= 0) && (x >= -R / 2 && y >= -R)) {
      status.innerHTML = "Попал!"
      tableShot(x, y, R, true)
      return true;
    }
  } else {let text_x = document.getElementById("text_x");
  text_x.innerHTML = `Неккоректное значение x`;
  return false}
  status.innerHTML = "Не попал!"
  tableShot(x, y, R, false)
  return false;
}

function get_time() {
  const date = new Date();
  let hours = date.getHours();
  let mins = date.getMinutes();
  let secs = date.getSeconds();
  if (hours < 10) {hours = "0" + hours;}
  if (mins < 10) {mins = "0" + mins;}
  if (secs < 10) {secs = "0" + secs;}
  return `${hours}:${mins}:${secs}`;
}

function get_date() {
  const date = new Date();
  let day = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();
  if (month < 10) {month = "0" + month;}
  if (day < 10) {day = "0" + day;}
  return `${day}/${month}/${year}`;
}



function tableShot(x,y,R, ok) {
  const table = document.getElementById("tbodyShot");
  const ts = Date.now();
  const dt = new Date(ts).toLocaleString("ru-RU");
  let num_row = localStorage.length;
  num_row += 1;
  table.innerHTML += `<tr>
    <td>${num_row}</td>
    <td>${x}</td>
    <td>${y}</td>
    <td>${R}</td>
    <td>${dt}</td>
    <td>${ok}</td>
  </tr>`
  localStorage.setItem(`${num_row}`, `<tr>
    <td>${num_row}</td>
    <td>${x}</td>
    <td>${y}</td>
    <td>${R}</td>
    <td>${dt}</td>
    <td>${ok}</td>
  </tr>`)
}

document.getElementById("y").addEventListener("submit", function (e) {
  e.preventDefault(); // просто гасим submit, никакой валидации тут не запускаем
});

document.getElementById("send").addEventListener("click", function (e) {
  e.preventDefault();
  if (validate()) {
    shot();
  }
});

document.getElementById("button_clear").addEventListener("click", function () {
  clear();
  location.reload();
})



// document.getElementById("remove_btn").addEventListener("click", function () {
//   const i = document.getElementById("remove_text").value;
//   remove(Number(i));
//   location.reload();
// })

// document.getElementById("remove_text").addEventListener("submit", function () {
//   const i = document.getElementById("remove_text").value;
//   remove(Number(i));
//   // location.reload();
// })
