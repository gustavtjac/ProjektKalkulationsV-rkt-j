// script.js

function toggleDropdown() {
    console.log("Dropdown toggled");
    const dropdown = document.getElementById("memberDropdown");
    dropdown.classList.toggle("open");
}

// Close the dropdown if clicked outside
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById("memberDropdown");
    const dropdownButton = dropdown.querySelector('.dropdown-button');

    // Check if click was outside the dropdown and button
    if (!dropdown.contains(event.target) && event.target !== dropdownButton) {
        dropdown.classList.remove("open");
    }
});