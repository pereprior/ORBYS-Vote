
document.getElementById('myForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var choice = document.querySelector('input[name="choice"]:checked').value;
    console.log(choice);
});