class AppHeader extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
      <header>
      <div class="logo">B-Craft</div>
        <nav>
            <a href="index.html">Forside</a>
            <a href="ydelser.html">Ydelser</a>
            <a href="portefolje.html">Portefølje</a>
            <a href="aboutUs.html">Om os</a>
            <a href="kontakt.html">Kontakt</a>
        </nav>
      </header>
    `;
    }
}
customElements.define('app-header', AppHeader);

class AppFooter extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
      <footer>
  
  
  <div class="footerMainDiv">
      <div class="aboutUs-column">
        <h1 class="strong-footer">Kort om os</h1> 
        <h3>
         
        Mit navn er Benny Thomasen og jeg er en dedikeret bygningssnedker med base midt i Jylland. Mit fokus ligger på kreativitet, bæredygtighed og genanvendelse i alt, hvad jeg gør. Jeg tilbyder skræddersyede løsninger til mine kunder og er engageret i at facilitere undervisning og samskabelse med børn og unge.

Med 5 år som B-craft, har jeg bygget lidt af hvert. Jeg stræber efter at skabe og montere unikke og funktionelle løsninger, der imødekommer dine behov. Mit fokus på kvalitet, pålidelighed og samarbejde gør mig til et naturligt valg for de fleste projekter for både private og erhverv/institutioner. 
        </h3>
      </div>
    
      
    
      <div class="social-media-container">  
          <div class="socials-column-text-box" style="align-content: center">
                  <h1> Følg os på sociale medier </h1>
                </div>
                
          <div class="socials-column-row">
          
              <a href="https://www.facebook.com/bcraft.dk?locale=da_DK" target="_blank">
                <div class="image-container-socials">
                  <img src="/img/fb.png">
                </div>
              </a>
        
              <a href="https://www.instagram.com/bcraft.dk/" target="_blank">
                <div class="image-container-socials">
                  <img src="/img/insta.png">
                </div>
              </a>
        
        
              <a href="https://www.linkedin.com/in/benny-thomasen/" target="_blank">
                <div class="image-container-socials">
                  <img src="/img/linkedin.png">
                </div>
              </a>
            </div>
             
        </div>
    
      <div class="contact-container">
      <div>
        <h1> Kontakt mig </h1>
        <h2> B-Craft </h2>
        <h2> Tlf: +45 24 25 34 60 </h2>
        <h2> E-mail: info@b-craft.dk </h2>  
        </div>
      </div>
      
      
</div>
      </footer>
    `;
    }
}
customElements.define('app-footer', AppFooter);
