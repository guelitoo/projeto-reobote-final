document.addEventListener("DOMContentLoaded", () => {
  // ---------------------------
  // Elementos
  // ---------------------------
  const botoes = document.querySelectorAll(".carrossel .btn-cta");
  const btnLogin = document.querySelector(".btn-login");
  const btnCadastro = document.querySelector(".btn-outline");
  const userCircle = document.querySelector(".user-circle");
  const logoLink = document.querySelector(".logo a");

  // Carousel elements
  const slides = Array.from(document.querySelectorAll(".carrossel .slide"));
  const prevBtn = document.querySelector(".carrossel .prev");
  const nextBtn = document.querySelector(".carrossel .next");
  const dots = Array.from(document.querySelectorAll(".carrossel .dot"));

  // Counters
  const counters = Array.from(document.querySelectorAll(".counter"));

  // ---------------------------
  // Sessão / UI header
  // ---------------------------
  const usuarioLogadoStr = localStorage.getItem("usuarioLogado");
  const usuario = usuarioLogadoStr ? JSON.parse(usuarioLogadoStr) : null;
  const tipoUsuario = usuario?.tipoUsuario || null;

  if (!usuario) {
    if (btnLogin) btnLogin.style.display = "inline-block";
    if (btnCadastro) btnCadastro.style.display = "inline-block";
    if (userCircle) userCircle.style.display = "none";
  } else {
    if (btnLogin) btnLogin.style.display = "none";
    if (btnCadastro) btnCadastro.style.display = "none";
    if (userCircle) userCircle.style.display = "flex";
  }

  // Ajusta todos os botões do carrossel
  botoes.forEach(botao => {
    if (!botao) return;
    if (tipoUsuario === "admin" || tipoUsuario === "funcionario") {
      botao.textContent = "Gerenciar pedidos";
      botao.href = "./pages/funcionario.html";
    } else if (tipoUsuario === "cliente") {
      botao.textContent = "Solicitar orçamento";
      botao.href = "./pages/orçamento.html";
    } else {
      botao.textContent = "Solicitar orçamento";
      botao.href = "./pages/login.html";
    }
  });

  // Logo -> sempre manda para index (garante recarregar index.js)
  if (logoLink) {
    logoLink.addEventListener("click", (e) => {
      e.preventDefault();
      window.location.href = "./index.html";
    });
  }

  // ---------------------------
  // CARROSSEL (funcional e resiliente)
  // ---------------------------
  let current = 0;
  let autoplayTimer = null;
  const AUTOPLAY_INTERVAL = 5000;

  // Safety: ensure slides/dots exist
  function showSlide(index) {
    if (!slides.length) return;
    current = (index + slides.length) % slides.length;
    slides.forEach((s, i) => {
      s.classList.toggle("active", i === current);
    });
    // update dots if exist
    if (dots.length) {
      dots.forEach((d, i) => d.classList.toggle("active", i === current));
    }
  }

  function go(dir) {
    showSlide(current + dir);
  }

  function startAutoplay() {
    stopAutoplay();
    autoplayTimer = setInterval(() => go(1), AUTOPLAY_INTERVAL);
  }

  function stopAutoplay() {
    if (autoplayTimer) {
      clearInterval(autoplayTimer);
      autoplayTimer = null;
    }
  }

  // Attach controls with guards
  if (prevBtn) prevBtn.addEventListener("click", () => { go(-1); startAutoplay(); });
  if (nextBtn) nextBtn.addEventListener("click", () => { go(1); startAutoplay(); });
  if (dots.length) {
    dots.forEach((d, i) => d.addEventListener("click", () => { showSlide(i); startAutoplay(); }));
  }

  // Pause autoplay on mouse enter, resume on leave (desktop friendly)
  const carouselEl = document.querySelector(".carrossel");
  if (carouselEl) {
    carouselEl.addEventListener("mouseenter", stopAutoplay);
    carouselEl.addEventListener("mouseleave", startAutoplay);
  }

  // Initialize carousel (ensure first slide has .active)
  if (slides.length && !slides.some(s => s.classList.contains("active"))) {
    slides[0].classList.add("active");
    if (dots[0]) dots[0].classList.add("active");
  }
  showSlide(current);
  startAutoplay();

  // ---------------------------
  // CONTADORES (somente quando visível)
  // ---------------------------
  function animateCounter(counterEl) {
    const target = Number(counterEl.getAttribute("data-target")) || 0;
    const isPercent = counterEl.hasAttribute("data-is-percent");
    const hasPlus = counterEl.hasAttribute("data-plus");
    let currentVal = 0;
    const duration = 1200; // ms
    const start = performance.now();

    function step(now) {
      const elapsed = now - start;
      const progress = Math.min(elapsed / duration, 1);
      const value = Math.floor(progress * target);
      if (isPercent) counterEl.textContent = value + "%";
      else if (hasPlus) counterEl.textContent = "+" + value;
      else counterEl.textContent = value;
      if (progress < 1) requestAnimationFrame(step);
      else {
        // Ensure final value exact
        if (isPercent) counterEl.textContent = target + "%";
        else if (hasPlus) counterEl.textContent = "+" + target;
        else counterEl.textContent = target;
      }
    }
    requestAnimationFrame(step);
  }

  if (counters.length) {
    const section = document.getElementById("contadores");
    if (section && "IntersectionObserver" in window) {
      const observer = new IntersectionObserver((entries, obs) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            counters.forEach(c => animateCounter(c));
            obs.disconnect();
          }
        });
      }, { threshold: 0.4 });
      observer.observe(section);
    } else {
      // Fallback: animate immediately
      counters.forEach(c => animateCounter(c));
    }
  }

  // ---------------------------
  // Exibir ano no rodapé
  // ---------------------------
  const yearSpan = document.getElementById("year");
  if (yearSpan) yearSpan.textContent = new Date().getFullYear();
});
