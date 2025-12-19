// REVEAL ON SCROLL
function revealElements() {
    const reveals = document.querySelectorAll(".reveal");

    reveals.forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight - 120) {
            el.classList.add("visible");
        }
    });
}

window.addEventListener("scroll", revealElements);
revealElements();
