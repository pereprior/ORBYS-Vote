// Función que genera un id aleatorio para los popups
function generateId() { return 'popup_' + Math.random().toString(36).substr(2, 9); }

// Función que crea un popup con un mensaje
function createInfoPopup(icon, message, buttonText, showImmediately = false) {
    return new Popup({
        id: generateId(),
        title: " ",
        textColor: "#FFFFFF",
        widthMultiplier: getPopupSizeResponsive(),
        backgroundColor: "#010101",
        content: `<img src="../images/${icon}" alt="ERROR" class="logo">
                                <span><strong>${message}</strong></span>
                                <button id="closeButton" type="submit">${buttonText}</button>`,
        textShadow: "0 0 .3em #000000bb",
        allowClose: false,
        showImmediately: showImmediately,
        loadCallback: () => {
            document
                .getElementById("closeButton")
                .addEventListener("click", () => {
                    // Si el navegador lo permite, cerramos la ventana
                    window.close();
                    // Si no, redirigimos a la página principal de Google
                    window.location.href = 'https://www.google.com';
            });
        }
    });
}

// Función que crea un popup con un mensaje que requiere de una confirmación
function createConfirmPopup(message, buttonRefuseText, buttonAcceptText, infoPopup) {
    const popup = new Popup({
        id: generateId(),
        title: " ",
        textColor: "#FFFFFF",
        widthMultiplier: getPopupSizeResponsive(),
        backgroundColor: "#010101",
        content: `
            <img src="../images/alert.svg" alt="ALERT" class="logo">
            <span><strong>${message}</strong></span>
            <button id="btn-refuse">${buttonRefuseText}</button>       <button id="btn-accept">${buttonAcceptText}</button>`,
        textShadow: "0 0 .3em #000000bb",
        allowClose: false,
        loadCallback: () => {
            document
                .getElementById("btn-refuse")
                .addEventListener("click", () => {
                    // Si el usuario no acepta, mostramos un mensaje informativo
                    infoPopup.show();
                    popup.hide();
            });

            document
                .getElementById("btn-accept")
                .addEventListener("click", () => {
                    // Si el usuario acepta, volvemos a la página anterior
                    showElements();
                    popup.hide();
            });
        },
    });
    return popup;
}

// Función para escalar los popups según la orientación y el tamaño de la pantalla
function getPopupSizeResponsive() {
    let multiplier;
    if (window.innerWidth > window.innerHeight) {
        if (window.innerWidth > 800) {
            // Responsive para monitores
            multiplier = 0.7;
        } else {
            // Responsive para movil en orientación horizontal
            multiplier = 0.5;
        }
    } else {
        if (window.innerWidth > 600) {
            // Responsive para tablets
            multiplier = 0.75;
        } else {
            // Responsive para movil en orientación vertical
            multiplier = 0.85;
        }
    }
    return multiplier;
}

// Función que oculta o muestra un elemento por su id
function setElementVisibilityById(id, action) {
    const element = document.getElementById(id);
    if (element) {
        if (action === 'hide') {
            element.classList.add('hidden');
        } else if (action === 'show') {
            element.classList.remove('hidden');
        }
    }
}

// Función que oculta los principales elementos de la página
function hideElements() {
    setElementVisibilityById('myForm', 'hide');
    setElementVisibilityById('triangle', 'hide');
    setElementVisibilityById('questionForm', 'hide');
}

// Función que muestra los principales elementos de la página
function showElements() {
    setElementVisibilityById('myForm', 'show');
    setElementVisibilityById('triangle', 'show');
    setElementVisibilityById('questionForm', 'show');
}

window.onload = function() {
    // Deshabilitamos el menú contextual en las imágenes
    document.addEventListener('contextmenu', function(e) {
        if (e.target.tagName === 'IMG')
            e.preventDefault();
    }, false);
};