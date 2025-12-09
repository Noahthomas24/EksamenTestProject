// HERO SLIDER

const hero = document.getElementById("hero");

const heroImages = [
    "images/hero1.jpg",
    "images/hero2.jpg",
    "images/hero3.jpg"
];

let heroIndex = 0;

// Update hero background
function updateHero() {
    hero.style.backgroundImage = `url('${heroImages[heroIndex]}')`;
    hero.style.transition = "background-image 1s ease-in-out";
}

// Next slide
function nextHero() {
    heroIndex = (heroIndex + 1) % heroImages.length;
    updateHero();
}

// Previous slide
function prevHero() {
    heroIndex = (heroIndex - 1 + heroImages.length) % heroImages.length;
    updateHero();
}

// Auto-slide every 5 seconds
setInterval(nextHero, 5000);

// Click arrows
document.getElementById("heroNextBtn").addEventListener("click", nextHero);
document.getElementById("heroPrevBtn").addEventListener("click", prevHero);

// Init first image
updateHero();


// PARALLAX EFFECT
window.addEventListener("scroll", () => {
    const scrollPos = window.scrollY;
    hero.style.backgroundPositionY = scrollPos * 0.4 + "px";
});


// REVEAL ON SCROLL
function revealElements() {
    const reveals = document.querySelectorAll(".reveal");

    for (let el of reveals) {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight - 120) {
            el.classList.add("visible");
        }
    }
}

window.addEventListener("scroll", revealElements);
revealElements();



// SMOOTH SCROLL CTA

document.getElementById("heroCtaBtn").addEventListener("click", (e) => {

    }
);
