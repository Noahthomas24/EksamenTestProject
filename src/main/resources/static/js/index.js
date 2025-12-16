// HERO SLIDER

const hero = document.getElementById("hero");




document.addEventListener('DOMContentLoaded', () => {
    const lightbox = document.getElementById('lightbox');
    const lightboxImg = document.getElementById('lightbox-img');
    const closeBtn = document.querySelector('.close');
    const cards = document.querySelectorAll('.zoomable');

    // Åbn lightbox
    cards.forEach(card => {
        card.addEventListener('click', () => {
            // Vi henter stien direkte fra vores nye data-img attribut
            const imgUrl = card.getAttribute('data-img');

            if (imgUrl) {
                console.log("Åbner billede:", imgUrl); // Tjek konsollen (F12) hvis det driller
                lightboxImg.src = imgUrl;
                lightbox.style.display = 'flex';
            } else {
                console.log("Fejl: Dette kort mangler en data-img attribut!");
            }
        });
    });

    // Luk funktion
    function closeLightbox() {
        lightbox.style.display = 'none';
        lightboxImg.src = '';
    }

    closeBtn.addEventListener('click', closeLightbox);

    lightbox.addEventListener('click', (e) => {
        if (e.target === lightbox) {
            closeLightbox();
        }
    });
});



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
