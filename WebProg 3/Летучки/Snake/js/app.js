let pushed = []

function push(elem) {
  const val = Number(elem.textContent.trim());

  if (!pushed.includes(val)) {
    pushed.push(val);
    coloring();
  } else {return;}

  if (pushed.length >3) {
    pushed.shift()
  }

}

function coloring() { // работает с pushed
  let cell = document.getElementById(`cell${pushed.at(-1)}`);
  cell.style.backgroundColor = "white";
  if (pushed.length < 4 ) {return;}
  cell = document.getElementById(`cell${pushed[0]}`);
  cell.removeAttribute("style");


}


